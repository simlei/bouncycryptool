package scalabc.connector.app



import org.eclipse.core.runtime.FileLocator
import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.Platform
import org.eclipse.jface.resource.{ImageDescriptor, ImageRegistry}
import org.eclipse.swt.graphics.Image
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.{Bundle, BundleContext}

import scalabc.connector.platform.jct.{JCT_RCP}
import java.net.URL

import scalabc.connector.app.legacy.LegacyBCTPStuff


// the constructor here is more or less the main class for the application.Nothing
// it is called via reflection by the JCrypTool RCP framework
class BouncyCrypToolPlugin extends AbstractUIPlugin {
  private[this] var app: Option[BouncyCrypToolApp] = None

  @throws[Exception]
  override def start(context: BundleContext): Unit = {
    super.start(context)
    this.app = Some(new BouncyCrypToolApp(JCT_RCP))
  }
  @throws[Exception]
  override def stop(context: BundleContext): Unit = {
    super.stop(context)
  }
  override protected def initializeImageRegistry(registry: ImageRegistry): Unit = legacyStuff.initializeImageRegistry(registry)

  val legacyStuff = new LegacyBCTPStuff(this)
  BouncyCrypToolPlugin.singleton = this
}
object BouncyCrypToolPlugin {
  val PLUGIN_ID = "org.jcryptool.bouncycryptool.plugin"
  var singleton: BouncyCrypToolPlugin = null
}

