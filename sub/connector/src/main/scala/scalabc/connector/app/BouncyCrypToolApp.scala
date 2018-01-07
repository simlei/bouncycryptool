package scalabc.connector.app

import com.diffplug.common.swt.Coat

import scalabc.connector.app.operations.BCOperationsService
import scalabc.connector.platform.RCP
import scalabc.connector.platform.io.SBCIO

class BouncyCrypToolApp(val rcp: RCP) {

  def startNonblocking(): Unit = {
    BouncyCrypToolApp.appSingleton = this //TODO: put off: generalize app lifecycles
    // TODO: fire it up scotty
  }
}

object BouncyCrypToolApp extends AppInterface {
  var appSingleton: BouncyCrypToolApp = null
  override val services: Services = new Services {}
}

trait AppInterface {
  def services: Services

  def eclipsePlugin = BouncyCrypToolPlugin.singleton //TODO: put off: deprecate

  def rcp: RCP = BouncyCrypToolApp.appSingleton.rcp
  def io: SBCIO = BouncyCrypToolApp.appSingleton.rcp.io
}

trait Services {
  val bcOps = new BCOperationsService
  val coats = new Coats {}
}

trait Coats {
  def editorPlaygroundCoat: Coat = scalabc.ui.examples.TestAppImpl.testCoat
}
