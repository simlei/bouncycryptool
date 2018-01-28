package org.jcryptool.projectapi

import org.jcryptool.projectapi.APIModel._
import org.jcryptool.structure.ScalaToJCTConnectorPlugin
import sbt.Keys.version
import sbt.ThisBuild

trait ConnectorJCTPluginAPI extends API_Spec {
  import ConnectorJCTPluginAPI._

  override def allCommands: Seq[(String, APIModel.API_Command)] = Seq(
    "updatePlugin" -> updatePlugin
  )
  override def allSubAPIs: Seq[(String, API_Spec)] = Seq()

  val updatePlugin: ZeroArgAPICmd
}
object ConnectorJCTPluginAPI {
  val updateCmdDescr = "Resolves BouncyCrypTool and updates the JCT core project"

  def updateCmdFunctionality(proj: ScalaToJCTConnectorPlugin)(): Unit = {
    proj.bct_api_sbt.remoteCall(s"updatePlugin ${proj.jctMetaProj.jctLayout.bctExtracted.get(version in ThisBuild)}")
  }
  case class ConnectorUpdateCmd(bctPluginProject: ScalaToJCTConnectorPlugin) extends ZeroArgAPICmd("updatePlugin", updateCmdDescr, updateCmdFunctionality(bctPluginProject) _)

}
case class ConnectorJCTPluginAPIImpl(bctPluginProject: ScalaToJCTConnectorPlugin) extends ConnectorJCTPluginAPI {
  val proj: ScalaToJCTConnectorPlugin = bctPluginProject
  override val updatePlugin: ZeroArgAPICmd = ConnectorJCTPluginAPI.ConnectorUpdateCmd(proj)
}