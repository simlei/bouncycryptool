package scalabc.connector.app.operations

import scalabc.connector.platform.{EditorKind, Editors}
import scalabc.connector.app.BouncyCrypToolApp._

final class BCOperationsService {

  def sudoExecuteThrowback(cmd: String): Unit = {
    println("Opening editor for content: " + cmd)
    rcp.editors.editorDefaultBlank(EditorKind.defaultText).replacePending(cmd)
  }

  def printHello() = {
    println("Hello printed by operations service")
  }

  println("creating BouncyCastle Operations service!")
}