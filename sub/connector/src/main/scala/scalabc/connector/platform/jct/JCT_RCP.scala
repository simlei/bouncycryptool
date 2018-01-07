package scalabc.connector.platform.jct

import scalabc.connector.platform._
import scalabc.connector.platform.io.SBCIO
import scalabc.connector.platform.jct.editors.JCTEditors

object JCT_RCP extends RCP {
  override val io: SBCIO = super.io //TODO: fix JCT logger and use it.
  override val editors = JCTEditors
  val jctEds = scalabc.connector.platform.jct.editors.jctMan
}


