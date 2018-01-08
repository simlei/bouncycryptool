package org.jcryptool.projectapi

import org.jcryptool.projectapi.APIModel.{API_Command, Signature}
import org.jcryptool.structure.JCTLayout

trait JCT_API {
  import org.jcryptool.projectapi.JCT_API._

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
  override def api_platformExtractor: TargetplatformToMavenAPI = jctLayout.projects.bouncycryptool.projects.jctPlatformExtractor.api_jctplatform

  override def buildResolveAndPublish: BuildAndResolveToBouncyCryptoolCmd = new BuildAndResolveToBouncyCryptoolCmd {
    override def apply(): Unit = {
      api_core_build.buildJCT
      api_platformExtractor.onLocalBuild.testResolve
    }
  }
}