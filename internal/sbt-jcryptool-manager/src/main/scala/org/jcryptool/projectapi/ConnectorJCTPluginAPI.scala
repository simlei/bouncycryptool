package org.jcryptool.projectapi

import org.jcryptool.structure.ScalaToJCTConnectorPlugin

trait ConnectorJCTPluginAPI {

}

case class ConnectorJCTPluginAPIImpl(bctPluginProject: ScalaToJCTConnectorPlugin) extends ConnectorJCTPluginAPI {
  val proj: ScalaToJCTConnectorPlugin = bctPluginProject

}