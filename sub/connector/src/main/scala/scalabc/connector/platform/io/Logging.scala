package scalabc.connector.platform.io
import enumeratum._

sealed trait SBCLogItem {
  def message: String
}
case class SBCLogItemMsg(message: String) extends SBCLogItem
case class SBCLogItemError(message: String, val thrown: Option[Throwable] = None) extends SBCLogItem

trait SBCLogFormat {
  def formatLogItem(item: SBCLogItem): String
}
object SBCLogFormat {
  def defaultFormat = new SBCLogFormat {
    def formatLogItem(item: SBCLogItem): String = item match {
      case SBCLogItemMsg(msg) => formatTagged(LogLevel.ERROR.toString)(msg) // TODO: implicits for toString?
      case SBCLogItemError(msg, _) => formatTagged(LogLevel.INFO.toString)(msg)
    }
    def formatTagged(tags: String*)(msg: String) = s"${tags.mkString("")}\t$msg"
  }
}

sealed trait LogLevel extends EnumEntry {
  import LogLevel._
  def toString(lvl: LogLevel): String = lvl match {
    case INFO => "[info]"
    case ERROR => "[error]"
    case omega => "[${omega.toString}]"
  }
}
object LogLevel extends Enum[LogLevel] {
  case object INFO extends LogLevel
  case object ERROR extends LogLevel
  val values = findValues
}