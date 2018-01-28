package scalabc.connector.app

import com.diffplug.common.swt.Coat
import org.eclipse.swt.widgets.Shell

import scalabc.connector.app.coats._
import scalabc.connector.app.operations.BCOperationsService
import scalabc.connector.platform.{Editors, RCP}
import scalabc.connector.platform.io.{SBCIO, SBCLogger}

trait AppInterface {
  def services: Services
  def rcp: RCP
  def io: SBCIO = rcp.io
  def editors: Editors = rcp.editors
}

trait Services {
  def bcOps: BCOperationsService
  def coats: Coats

  def dispose(): Unit
}

trait Coats {
  def algoViewCoat: Coat
}

class BouncyCrypToolApp(override val rcp: RCP) extends AppInterface {
  val services = BCTServices()

  def dispose(): Unit = {
    rcp.io.logger.logMsg("Shutting down BCT app")
    services.dispose()
  }

  rcp.io.logger.logMsg("Started BCT app")
}
object BouncyCrypToolApp {
  private var appSingleton: Option[BouncyCrypToolApp] = None
  def start(rcp: RCP) = {
    BouncyCrypToolApp.appSingleton = Some(new BouncyCrypToolApp(rcp))
  }
  def stop() = {
    bct.dispose()
    BouncyCrypToolApp.appSingleton = null
  }
  def bct = appSingleton.getOrElse(sys.error("The BCT app was not yet started up, or already shut down."))
  def logger = appSingleton.map(_.rcp.io.logger).getOrElse(SBCLogger.fallback)
  def services = bct.services
}

case class BCTServices() extends Services {

  override def bcOps: BCOperationsService = new BCOperationsService
  override def coats: Coats = BCTCoats()

  override def dispose(): Unit = {
    BouncyCrypToolApp.logger.logMsg("Shutting down BCT services...")
    //TODO: put off: meaningful lifecycle stuff in implementor
  }

  BouncyCrypToolApp.logger.logMsg("Started BCT services!")
}

case class BCTCoats() extends Coats {
  override def algoViewCoat: Coat = AlgorithmViewCoat
}
