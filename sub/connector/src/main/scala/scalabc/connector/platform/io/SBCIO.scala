package scalabc.connector.platform.io

import java.io.{InputStream, OutputStream}

import com.typesafe.scalalogging.Logger

trait SBCIO {
  def in: InputStream
  def out: OutputStream
  def logger: SBCLogger
}

trait SBCLogger {
  def log(item: SBCLogItem, requestDisplay: Boolean = false)
  def logMsg(msg: String, requestDisplay: Boolean = false) = log(SBCLogItemMsg(msg), requestDisplay)
  def logError(msg: String, e: Option[Throwable], requestDisplay: Boolean = false) = log(SBCLogItemError(msg, e), requestDisplay)
}
object SBCLogger {
  object fallback extends SBCLogger {
    val slf4jLogger = Logger[SBCLogger]
    override def log(item: SBCLogItem, requestDisplay: Boolean): Unit = item match {
      case SBCLogItemMsg(msg) => slf4jLogger.info(msg)
      case SBCLogItemError(msg, Some(thr)) => slf4jLogger.error(msg, thr)
      case SBCLogItemError(msg, _) => slf4jLogger.error(msg)
    }
  }
}