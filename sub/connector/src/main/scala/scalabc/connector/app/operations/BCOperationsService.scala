package scalabc.connector.app.operations

import scalabc.connector.app.BouncyCrypToolApp
import scalabc.connector.platform.{EditorKind, Editors}
import scalabc.connector.app.BouncyCrypToolApp._

final class BCOperationsService {

  def sudoExecuteThrowback(cmd: String): Unit = {
    BouncyCrypToolApp.logger.logMsg(s"opening editor with content $cmd")
    bct.rcp.editors.editorDefaultBlank(EditorKind.defaultText).replacePending(cmd)
  }

  BouncyCrypToolApp.logger.logMsg("Started BCT Operations service!")
}