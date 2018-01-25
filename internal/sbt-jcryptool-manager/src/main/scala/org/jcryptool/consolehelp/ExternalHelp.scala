package org.jcryptool.consolehelp

import de.simlei.multimanager.ConsoleProjectUtils
import org.jcryptool.projectapi.APIModel.{API_Command, helpAbstractions}
import org.jcryptool.projectapi.{ConnectorJCTPluginAPI, JCTCoreTychoBuildAPI, JCT_API, TargetplatformToMavenAPI}
import org.jcryptool.structure.JCTLayout
import sbt.io.IO

// ---------- INFRASTRUCTURE CODE PART ------------


// this trait describes how to annotate objects with help -- by giving them '.help' object members
trait HasHelp[Helped] {
  def help: Unit // this method is called by the user and prints help to the console
  def helpedToString: String // this method is useful for other parts of the project, to change the toString representation of objects that have help
}

// this case class models how the programmer enters the help, see object "ExternalHelp" for examples
case class Help[HelpSubject](
                         subject: HelpSubject,              // stores the subject for which to provide help
                         helpContent: String,          // provide a help content String here!
                         toStr: Option[String] = None  // when set to Some("...content..."), it replaces toString

                       ) extends HasHelp[HelpSubject]{

  // a conversion method for
  def helpedToString: String = toStr.getOrElse{
    val oldToStr = subject.toString
    if(oldToStr.contains("$@")) {
      val neww = oldToStr.takeWhile(_ != '@')
      s"$neww | ${helpContent.takeWhile(_ != '\n')}"
    } else {
      s"$oldToStr | ${helpContent.takeWhile(_ != '\n')}"
    }
  }

  // this prints the help content and 65-char-wide horizontal lines.
  override def help = println("-" * 65 + "\n" + helpContent + "\n" + "-" * 65)
}

// ---------- INJECTED HELP PART ------------

/* The object "ExternalHelp" gets imported into the console.
   This means, any implicit conversions to Help objects will provide, for any object that "fits",
   an object member '.help' and augmented toString representation. We can use that to define help without
   writing everything in the code that concerns itself with the structural parts, which makes everything cleaner.

   For inspirations for new conversions, look into the file org.jcryptool.structure.JCTLayout
   to see which types and variables there are, and define your own implicit help specifications!
*/
object ExternalHelp {
  import helpAbstractions._

  // This represents the help for the "jct" object, the main entry point in the console.
  // Any object can be annotated like this!
  implicit def jctHelp(jct: JCTLayout) = Help[JCTLayout](
    jct,
    """This is the JCrypTool and BouncyCrypTool description object and main entry point to the project model.
      |
      |It contains nested description objects of all modelled parts of JCrypTool as modelled in
      |the class "org.jcryptool.structure.JCTLayout" in the bouncycryptool manager plugin.
      |
      |If you need help about an object, try a dot after it, press "TAB" and see if there is
      |a ".help" entry -- the same way you found this help, probably. Tab completion and the help
      |commands are the way to discover what this console has to offer.
      |
      |The main projects usually have a nested "projects" variable, from where subprojects can be reached, e.g.:
      |> jct.projects.core
      |
      |Many projects, like the root ("jct") and the core repository, have useful APIs to call,
      |named "api_xyz". Look for them when using tab completion while typing. e.g.:
      |> jct.projects.core.<TAB> -> "api_build" is in the list, the API to build the JCT product.
      |
      |For example, the descriptor object and its directory for this abovementioned manager plugin,
      |which provides this console and its help, can be reached via:
      |> jct.projects.bouncycryptool.projects.jctManager.dir
      |
      |It is part of the bouncycryptool repository, in the subfolder /internal/sbt-jcryptool-manager,
      |but you get to see the absolute location on your machine this way.
      |Not all projects are modelled yet, just the ones necessary for now, but this is easy to change,
      |and easy to give them their own APIs :)
      |
      |The main point for useful automations is supposed to be "jct.api". It has a ".help" member, check it out!
      |
      |As a last point, here are some shortcuts defined for you to make things faster and help you understand
      |the structure of this console project a bit better. You can use either the identifiers on the
      |left or on the right, the result is identical.
    """.stripMargin + "\n" + ConsoleProjectUtils.printImportedVals
  )

  // This represents the help for the target platform commands
  // (in the console, on jct.projects.bouncycryptool.projects.jctPlatformExtractor.api)
  implicit def targetplatformLocalTest(localPublishPlatformCmd: TargetplatformToMavenAPI.LocalResolveCmd) =
    helpAbstractions.consoleCmdHelp(
      localPublishPlatformCmd,
      "Resolves JCrypTool and its target platform binaries locally for use in the bouncyCrypTool Scala project",

      """The local platform has to be built before. Either you use a built p2 repository from earlier, or build anew
        |to use your newest changes to JCT core or crypto-repository code.
        |Building the JCT platform is done with Maven and its Eclipse Tycho plugin through the JCT core project. The result
        |folder named "repository" there is the input for this method.
        |
        |However, this project has automations for this. Look into the central API help, jct.api.help, for more information.
      """.stripMargin.stripLineEnd // TODO: weekend: complete help
    )
  implicit def targetplatformLocalTest(localPublishPlatformCmd: TargetplatformToMavenAPI.LocalTestCmd) =
    helpAbstractions.consoleCmdHelp(
      localPublishPlatformCmd,
      "Tests the resolution of JCrypTool and its target platform binaries for use in the bouncyCrypTool Scala project",

      """The local platform has to be built before. Either you use a built p2 repository from earlier, or build anew
        |to use your newest changes to JCT core or crypto-repository code.
        |Building the JCT platform is done with Maven and its Eclipse Tycho plugin through the JCT core project.
        |
        |However, this project has automations for this. Look into the central API help, jct.api.help, for more information.
      """.stripMargin.stripLineEnd // TODO: weekend: complete help
    )
  implicit def targetplatformLocalTest(localPublishPlatformCmd: TargetplatformToMavenAPI.WebResolveCmd) =
    helpAbstractions.consoleCmdHelp(
      localPublishPlatformCmd,
      "Resolves JCrypTool and its target platform binaries from a web snapshot (i.e. weekly or nightly build) for use in the bouncyCrypTool Scala project",

      """This may fail if you don't have an active internet connection. Also, the web-hosted target platform may
        |have outdated code, not reflecting changes you may have made locally. If that is a problem, you may use
        |the commands in the sister API, "onLocalBuild". Alternatively, look into the central API help, jct.api.help, for more information.
      """.stripMargin.stripLineEnd
    )

  implicit def targetplatformLocalTest(localPublishPlatformCmd: TargetplatformToMavenAPI.WebTestCmd) =
    helpAbstractions.consoleCmdHelp(
      localPublishPlatformCmd,
      "Tests the resolution of JCrypTool and its target platform binaries from a web snapshot (i.e. weekly or nightly build) for use in the bouncyCrypTool Scala project",

      """This may fail if you don't have an active internet connection. Also, the web-hosted target platform may
        |have outdated code, not reflecting changes you may have made locally. If that is a problem, you may use
        |the commands in the sister API, "onLocalBuild". Alternatively, look into the central API help, jct.api.help, for more information.
      """.stripMargin.stripLineEnd
    )

  implicit def buildJCT(localPublishPlatformCmd: JCTCoreTychoBuildAPI.LocalP2BuildCmd) =
    helpAbstractions.consoleCmdHelp(
      localPublishPlatformCmd,
      "Builds the JCrypTool product and its P2 update site and returns the Product descriptor object",

      """This utilises Maven and Tycho. You must have Maven 3.0 and Tycho 1.0 installed on your system for this to work.
        |Please also note, that you need ca. 3.5 GB RAM if this is the first time you run it.
This may fail if you don't have an active internet connection. Also, the web-hosted target platform may
        |have outdated code, not reflecting changes you may have made locally. If that is a problem, you may use
        |the commands in the sister API, "onLocalBuild". Alternatively, look into the central API help, jct.api.help, for more information.
        |This automation is used in the central jct API ("jct.api") for working together with the BouncyCrypTool target platform extraction routines.
      """.stripMargin.stripLineEnd
    )

  implicit def buildResolveAndPublishJCT(localPublishPlatformCmd: JCT_API.BuildAndResolveToBouncyCryptoolCmd) =
    helpAbstractions.consoleCmdHelp(
      localPublishPlatformCmd,
      "Builds the JCrypTool product and its P2 update site and returns the Product descriptor object",

      """This command sequentially executes:
        |  1) building JCrypTool from source with Maven/Tycho
        |  2) Extracting these binaries from that product with a specialized sbt plugin (jct.projects.bouncycryptool.projects.jctPlatformExtractor)
        |  3) Publishing those binaries in JAR format to a local maven repository.
        |
        |This is kind of a replacement for the 'resolve target platform' step inside the Eclipse IDE; it is necessary for
        |the bouncycryptool project, as it builds upon Scala (you live in a Scala world inside this console!), which does
        |not work very well with Eclipse repositories, so it needs help. Please refer to the methods in the sub-APIs for more information.
      """.stripMargin.stripLineEnd
    )

  implicit def targetplatformLocalApi(localSrcApi: TargetplatformToMavenAPI.subApis.LocalSrcApi) =
    helpAbstractions.apiHelp(localSrcApi,
      "sub-API of the target platform to Maven/Bouncycryptool extractor, working on a web-hosted p2 repository.",
      "Methods to resolve the binaries of JCT/core and JCT/crypto from the hard drive state into Maven/Ivy so Scala/SBT and the BouncyCryptool can use them."
    )

  implicit def targetplatformWebApi(webSrcApi: TargetplatformToMavenAPI.subApis.WebSrcApi) =
    helpAbstractions.apiHelp(webSrcApi,
      "sub-API of the target platform to Maven/Bouncycryptool extractor, working on a locally-built p2 repository",
      "Methods to resolve the binaries of JCT/core and JCT/crypto from a web snapshot into Maven/Ivy so Scala/SBT and the BouncyCryptool can use them."
    )

  implicit def targetplatformApi(tpApi: TargetplatformToMavenAPI) =
    helpAbstractions.apiHelp(tpApi,
      "API of the target platform to Maven/Bouncycryptool extractor",
      "Methods to resolve the binaries of JCT/core and JCT/crypto into Maven/Ivy so Scala/SBT and the BouncyCryptool can use them."
    )

  implicit def buildApi(mvnTychoApi: JCTCoreTychoBuildAPI) =
    helpAbstractions.apiHelp(mvnTychoApi,
    "API for the JCT product build, in jct.projects.core",
      "Build JCT to the default directory, or to one of your liking. Make your own weekly build!"
    )

  implicit def connectorApi(api: ConnectorJCTPluginAPI) =
    helpAbstractions.apiHelp(api,
      "API for the BCT connector plugin in jct.projects.core",
      "There is a single command to automate the retrieval of BCT binaries from local and remote maven repositories"
    )

  implicit def jctApi(api: JCT_API) =
    helpAbstractions.apiHelp(api,
      "API of the root jct structure object.",
      """This is supposed to be the gathering point for many automations.
        |
        |Currently, it has one: 'buildResolveAndPublish', automating the following steps:
        |  1) building JCrypTool from source with Maven/Tycho
        |  2) Extracting these binaries from that product with a specialized sbt plugin (jct.projects.bouncycryptool.projects.jctPlatformExtractor)
        |  3) Publishing those binaries in JAR format to a local maven repository.
        |
        |This is kind of a replacement for the 'resolve target platform' step inside the Eclipse IDE; it is necessary for
        |the bouncycryptool project, as it builds upon Scala (you live in a Scala world inside this console!), which does
        |not work very well with Eclipse repositories, so it needs help.
        |Also, the APIs of the subprojects that are needed are "pulled up" for convenience, so you can access them directly.
        |These are the equivalent API paths deeper down the project structure vs. the pulled up aliases:
        |   jct.api.api_core_build         <-> jct.projects.core.api_build
        |   jct.api.api_platformExtractor  <-> jctLayout.projects.bouncycryptool.projects.jctPlatformExtractor.api
        |
        |In a more broad sense, this API and this console in general stands for unlimited possibilities w.r.t. automation
        |and JCrypTool. You can readily use any code of the bouncycryptool project here, and write your own. This console knows
        |very well about the project structure of JCrypTool and thus anything can be automated. Stay tuned :)
        |""".stripMargin.stripLineEnd
    )


}

