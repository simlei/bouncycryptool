package org.jcryptool.projectapi

import de.simlei.multimanager.misc.fs.{DirPresence, FilePresence}
import de.simlei.multimanager.projects.flavor.SBTProject
import org.jcryptool.projectapi.APIModel._
import org.jcryptool.structure.BouncyCryptoolRepo
import sbt._

trait TargetplatformToMavenAPI {
  import TargetplatformToMavenAPI._
  import subApis._

  def defaults: TargetplatformDefaults
  def onLocalBuild: LocalSrcApi
  def onWebHostedBuild: WebSrcApi

}
object TargetplatformToMavenAPI {

  object subApis {
    trait TargetplatformDefaults {
      val webP2Repository: String
      val localP2Repository: File
    }

    trait LocalSrcApi {
      val testResolve: LocalTestCmd
      val resolveAndPublish: LocalResolveCmd
    }

    trait WebSrcApi {
      val testResolve: WebTestCmd
      val resolveAndPublish: WebResolveCmd
    }
  }

  case class Src(remoteName: String, tpe: String) extends ArgSpec {
    override def name: String = remoteName match {
      case webSrc.remoteName => "p2RepoURL"
      case localSrc.remoteName => "localP2File"
      case _ => ""
    }
    override def description: String = remoteName match {
      case webSrc.remoteName => "web p2 repository"
      case localSrc.remoteName => "locally-built p2 repository"
      case _ => ""
    }
  }
  val webSrc = Src("webSource", "String")
  val localSrc = Src("localSource", "File")


  abstract class ApiCmd[ArgT](val cmdName: String, val remoteCmdName: String, val src: Src) extends API_Command {
    override def signatures: Map[Signature, String] = Map(
      Signature(cmdName, Seq()) -> s"uses the default ${src.description} for JCrypTool",
      Signature(cmdName, Seq(src)) -> s"specify a custom ${src.description}"
    )
    def apply(source: ArgT): Unit = cmdExecutor(argToCmdArg(source))
    def apply(): Unit = apply(defaultValue)
    def makeSbtCmd(arg: String) = s"${this.remoteCmdName} ${this.src.name} $arg"

    def defaultValue: ArgT
    def argToCmdArg(arg: ArgT): String
    def cmdExecutor(cmdString: String): Unit
  }
  abstract class LocalResolveCmd extends ApiCmd[File]("resolveAndPublish", "publish-jct-local-maven", localSrc)
  abstract class LocalTestCmd extends ApiCmd[File]("testResolve", "test-platform", localSrc)
  abstract class WebResolveCmd extends ApiCmd[String]("resolveAndPublish", "publish-jct-local-maven", webSrc)
  abstract class WebTestCmd extends ApiCmd[String]("testResolve", "test-platform", webSrc)

}

case class TargetplatformToMavenAPIImpl(targetPlatformProject: BouncyCryptoolRepo#BCTSubProject) extends TargetplatformToMavenAPI {
  import TargetplatformToMavenAPI._
  import subApis._

  private val proj = targetPlatformProject

  override def defaults: TargetplatformDefaults = new TargetplatformDefaults {
    override val localP2Repository = proj.jctMetaProj.jctLayout.projects.core.api_build.outputs.built_product.p2Dir
    override val webP2Repository: String = proj.jctMetaProj.jctLayout.snapshotUpdateSite.url
  }

  override def onLocalBuild: LocalSrcApi = new LocalSrcApi {
    override val resolveAndPublish = new LocalResolveCmd {
      override def defaultValue: sbt.File = defaults.localP2Repository
      override def argToCmdArg(arg: sbt.File): String = DirPresence(arg).validatedOrBail.getAbsolutePath
      override def cmdExecutor(cmdString: String): Unit = runTargetPlatformProjectCmd(cmdString)
    }
    override val testResolve: LocalTestCmd = new LocalTestCmd {
      override def defaultValue: sbt.File = defaults.localP2Repository
      override def argToCmdArg(arg: sbt.File): String = DirPresence(arg).validatedOrBail.getAbsolutePath
      override def cmdExecutor(cmdString: String): Unit = runTargetPlatformProjectCmd(cmdString)
    }
  }

  override def onWebHostedBuild: WebSrcApi = new WebSrcApi {
    override val resolveAndPublish: WebResolveCmd = new WebResolveCmd {
      override def defaultValue: String = defaults.webP2Repository
      override def argToCmdArg(arg: String): String = arg
      override def cmdExecutor(cmdString: String): Unit = runTargetPlatformProjectCmd(cmdString)
    }
    override val testResolve: WebTestCmd = new WebTestCmd {
      override def defaultValue: String = defaults.webP2Repository
      override def argToCmdArg(arg: String): String = arg
      override def cmdExecutor(cmdString: String): Unit = runTargetPlatformProjectCmd(cmdString)
    }
  }

  def runTargetPlatformProjectCmd(cmd: String): Unit = proj.asInstanceOf[SBTProject].bct_api_sbt.remoteCall(cmd)

}