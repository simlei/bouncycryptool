package scalabc.connector.app

import com.diffplug.common.swt.Coat
import org.eclipse.swt.widgets.{Composite, Control}
import org.eclipse.ui.part.ViewPart

import scalabc.ui.abstractions.RCPParented

package object views {

  class RCPView(coat: Coat) extends ViewPart with RCPParented {

    override final def createPartControl(composite: Composite): Unit = {
      receiveRoot(composite)
      coat.putOn(composite)
    }

    override def setFocus(): Unit = this.root.setFocus()
  }

}
