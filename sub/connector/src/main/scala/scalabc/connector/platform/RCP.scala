package scalabc.connector.platform

import scalabc.connector.platform.io._


trait RCP { //TODO: put off: rcp extraction: name it "Environment"
  def editors: Editors
  def io: SBCIO = new SBCIO {
    override def in = System.in
    override def out = System.out
    override def logger = SBCLogger.fallback
  }
}


trait RCPResourcing { // marker trait
  // access to RCP object only via the connection object!
  def dispose() = {}
}
trait RCPLifecycle extends RCPResourcing {
  def setup() = {} //CONTRACT: // no access to the connection implicit!
}


trait RCPId {
  def rcpId: String
}
object RCPId {
  implicit def apply(id: String) = new RCPId {
    override def rcpId: String = id
  }
}