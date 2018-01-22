package scalabc.connector.app.views

import org.eclipse.ui.part.ViewPart
import org.eclipse.swt.widgets.Composite

import scalabc.connector.app.BouncyCrypToolApp

object AlgorithmsView {
  val ID = "org.jcryptool.bouncycryptool.plugin.AlgorithmsView"
}
class AlgorithmsView() extends
  RCPView(BouncyCrypToolApp.services.coats.editorPlaygroundCoat)