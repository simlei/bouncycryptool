package scalabc.connector.app.legacy

import org.eclipse.ui.{IPageLayout, IPerspectiveFactory}
class BCTPerspective extends IPerspectiveFactory {
  override def createInitialLayout(layout: IPageLayout): Unit = {}
}
object BCTPerspective {
  def id = "org.jcryptool.bouncycryptool.plugin.perspective"
}



