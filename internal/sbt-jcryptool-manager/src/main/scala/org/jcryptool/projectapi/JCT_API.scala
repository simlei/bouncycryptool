package org.jcryptool.projectapi

import org.jcryptool.consolehelp.HasHelp
import org.jcryptool.projectapi.APIModel.{API_Command, API_Spec, Signature}
import org.jcryptool.structure.JCTLayout

trait JCT_API extends API_Spec {
  import org.jcryptool.projectapi.JCT_API._

  override def allCommands = Seq("buildResolveAndPublish" -> buildResolveAndPublish)
  override def allSubAPIs = Seq("api_core_build"->api_core_build, "api_platformExtractor"->api_platformExtractor)

  def api_core_build: JCTCoreTychoBuildAPI
  def api_platformExtractor: TargetplatformToMavenAPI

  def buildResolveAndPublish: BuildAndResolveToBouncyCryptoolCmd

}
object JCT_API {
  abstract class BuildAndResolveToBouncyCryptoolCmd extends API_Command {
    override def cmdName: String = "buildResolveAndPublish"
    override def signatures: Map[APIModel.Signature, String] = Map(
      Signature(cmdName, Seq()) -> "Just call me, this is an automation command"
    )
    def apply(): Unit
  }
}

case class JCT_API_Implementation(jctLayout: JCTLayout) extends JCT_API {
  import org.jcryptool.projectapi.JCT_API._

  override def api_core_build: JCTCoreTychoBuildAPI = jctLayout.projects.core.api_build
  override def api_platformExtractor: TargetplatformToMavenAPI = jctLayout.projects.bouncycryptool.projects.jctPlatformExtractor.api

  override def buildResolveAndPublish: BuildAndResolveToBouncyCryptoolCmd = new BuildAndResolveToBouncyCryptoolCmd {
    override def apply(): Unit = {
      api_core_build.buildJCT()
      api_platformExtractor.onLocalBuild.testResolve()
    }
  }
}