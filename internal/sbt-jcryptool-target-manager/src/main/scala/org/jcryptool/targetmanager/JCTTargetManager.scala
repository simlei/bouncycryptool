package org.jcryptool.target

import sbt.{Def, _}
import org.jcryptool.osgi._
import sbt.Keys.{resolvers, _}
import sbt.complete.DefaultParsers.Space
import sbt.complete.{DefaultParsers, Parser}
import sbt.complete.DefaultParsers._
import sbt.osgi.manager._

import scala.util.Try

object JCTTargetManager extends AutoPlugin {

//  override def trigger = allRequirements // auto-activation

  object cmds {
    import JCTTargetManager.autoImport._

    object commandDef {
      def predicated[S](p: Parser[S])(test: S => Boolean, errorMessage: String): Parser[S] = p flatMap { s =>
        val tested = test(s)
        if(tested) {
          success(s)
        } else {
          failure(errorMessage)
        }
      }

      type SourceParser[T <: P2Source] = Parser[Parser[T]]

      sealed trait P2Source {
        def setting: Def.Setting[_]
      }
      case class WebSource(u: URL) extends P2Source {
        override def setting = target_resolver_location_web := Some(u.toString)
      }
      case class LocalSource(f: File) extends P2Source {
        override def setting = target_resolver_location_locallyBuilt := Some(f)
      }

      object P2Source {
        val stage1Parser: SourceParser[P2Source] = Space ~> (LocalSource.parser | WebSource.parser)
        val stage2Parser = stage1Parser.flatMap(identity)

      }

      object WebSource {
        val id = "webSource"
        val webUrlParser: Parser[URL] = mapOrFail(Uri(Set(new java.net.URI("https://simlei.github.io/jcryptool-p2/"))))(_.toURL)
        val settingsParserWeb: Parser[WebSource] =
          predicated[URL](webUrlParser)(((u: URL) => u.getProtocol != null && u.getProtocol.contains("http")), "Not a valid directory for a local p2 update site").map(WebSource(_))
        val parser: SourceParser[WebSource] = token(id) ^^^ ( Space ~> settingsParserWeb )
      }

      object LocalSource {
        val id = "localSource"
        val fileParser: Parser[File] = DefaultParsers.fileParser(file("."))
        val settingsParserLocal: Parser[LocalSource] =
          predicated[File](fileParser)(((repoFile: File) => repoFile.exists() && repoFile.isDirectory), "Not a valid web url for a p2 update site").map(LocalSource(_))
        val parser: SourceParser[LocalSource] = token(id) ^^^ (Space ~> settingsParserLocal)
      }

      val configParser: State => Parser[Seq[Def.Setting[_]]] = _ => P2Source.stage2Parser map (result => Seq(result.setting))

      val configAction: (State, Seq[Def.Setting[_]]) => State = (state, settings) => {
        Project.extract(state).append(settings, state)
      }
      def configActionAndThen(cmds: Seq[String]): (State, Seq[Def.Setting[_]]) => State = (state, settings) => {
        val newState = state.copy(remainingCommands = cmds ++ state.remainingCommands)
        configAction(newState, settings)
      }

    }

    import commandDef._

    val configCmd = Command("configure")(configParser)(configAction)

    val testCmd = Command("test-platform")(configParser)(configActionAndThen(
      Seq("osgiResolve", "assembly")))

    val publishLocalCommand = Command("publish-jct-local-maven")(configParser)(configActionAndThen(
      Seq("osgiResolve", "compile", "publishM2")))

    val publishGlobalCmd = Command("publish-jct-global-maven")(configParser)(configActionAndThen(
      Seq("osgiResolve", "compile", "publishM2"))); //TODO: set up maven publishing

//    object p2 {
//      val setBundleDependencies: Command = {
//        val action: (State, Seq[String]) => State = (state: State, args: Seq[String]) => {
//          Project.extract(state).append(Seq(p2Dependencies := args) ++ OSGiManager, state)
//        }
//        Command.args("set-p2-bundles", "<p2 bundle id>")(action)
//      }
//    }

  }

  object autoImport {
    val target_resolver_location_locallyBuilt = settingKey[Option[File]]("points to the locally-built p2 update site, most probably the freshest snapshot")
    val target_resolver_location_web = settingKey[Option[String]]("points to a web url where the p2 repository is hosted")

    val target_resolver_defaultStrategy = settingKey[Option[Resolver]]("the resolver, following the default strategy of choosing the coreBuiltResolver over the web-based one")
    val target_resolver_locallyBuilt = settingKey[Option[Resolver]]("the resolver, using the locally-built repository (core.releng/target/repository) without going through the web option")
    val target_resolver_web = settingKey[Option[Resolver]]("the resolver, using the web- and not the locally-built repository even if it exists")

    val target_resolver = settingKey[Option[Resolver]]("the p2 resolver setting")
    val p2Dependencies = settingKey[Seq[String]]("p2 dependencies to the JCT target platform and update site")

  }
  import autoImport._

  val no_resolver_set_msg: String = "No p2 resolver was set; please report a bug or, if you are a build-level developer, set the keys target_resolver_location_web and target_resolver_location_locallyBuilt (optional)"

  lazy val defaultSettings: Seq[Def.Setting[_]] = Seq(
    p2Dependencies := Seq(),
    libraryDependencies in OSGiConf := {
      val p2D = p2Dependencies.value
      p2D map (dependencies.bundleDependency(_))
    },
    target_resolver_location_locallyBuilt := None,
    target_resolver_location_web := None,
    target_resolver_defaultStrategy := {target_resolver_locallyBuilt.value.orElse(target_resolver_web.value)},
    target_resolver_locallyBuilt := target_resolver_location_locallyBuilt.value.map(dir => typeP2("locally-built p2 update site" at "file://" + dir)),
    target_resolver_web := target_resolver_location_web.value.map(url => typeP2("Remote JCT P2 update site" at url)),
    target_resolver := target_resolver_defaultStrategy.value,

    resolvers in OSGiConf ++= target_resolver.value.map(Seq(_)).getOrElse(Nil),

    commands ++= Seq(cmds.configCmd, cmds.testCmd, cmds.publishLocalCommand, cmds.publishGlobalCmd)
  )

  override def projectSettings: Seq[Def.Setting[_]] = defaultSettings

}