package scalabc.connector.app.legacy

import org.eclipse.core.runtime.{FileLocator, Platform}
import org.eclipse.jface.resource.{ImageDescriptor, ImageRegistry}
import org.eclipse.swt.graphics.Image
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.Bundle

import scalabc.connector.app.BouncyCrypToolPlugin

class LegacyBCTPStuff(val bctPlugin: BouncyCrypToolPlugin) {

  val IMG_STAR = "star"
  val IMG_SAVE = "save"
  val IMG_OPEN = "open"
  val IMG_GEAR = "gear"
  val IMG_STATUS_WARNING = "status_warning"
  val IMG_STATUS_COMPLETE = "status_complete"
  val IMG_SAMPLE = "sample"
  val IMG_RUN = "run"
  val IMG_KEY = "key"
  val IMG_FOLDER = "folder"
  val IMG_EDIT = "edit"
  val IMG_BC = "bc"
  val IMG_BC_DIGEST = "bc_digest"

  def initializeImageRegistry(registry: ImageRegistry): Unit = {
    val bundle = Platform.getBundle(BouncyCrypToolPlugin.PLUGIN_ID)
    registerImg(registry, bundle, IMG_EDIT, "icons/edit.gif")
    registerImg(registry, bundle, IMG_FOLDER, "icons/folder.gif")
    registerImg(registry, bundle, IMG_KEY, "icons/key.gif")
    registerImg(registry, bundle, IMG_RUN, "icons/run.gif")
    registerImg(registry, bundle, IMG_SAMPLE, "icons/sample.gif")
    registerImg(registry, bundle, IMG_STATUS_COMPLETE, "icons/complete_status.gif")
    registerImg(registry, bundle, IMG_STATUS_WARNING, "icons/warning_status.gif")
    registerImg(registry, bundle, IMG_GEAR, "icons/gear.png")
    registerImg(registry, bundle, IMG_OPEN, "icons/open.png")
    registerImg(registry, bundle, IMG_SAVE, "icons/save.png")
    registerImg(registry, bundle, IMG_STAR, "icons/star.png")
    registerImg(registry, bundle, IMG_BC, "icons/bc.png")
    registerImg(registry, bundle, IMG_BC_DIGEST, "icons/bc_digest.png")
  }

  private def registerImg(registry: ImageRegistry, bundle: Bundle, IMAGE_ID: String, fullPath: String): Unit = {
    registry.put(IMAGE_ID, ImageDescriptor.createFromURL(FileLocator.find(bundle, new org.eclipse.core.runtime.Path(fullPath), null)))
  }
  def getImageDescriptor(path: String): ImageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(BouncyCrypToolPlugin.PLUGIN_ID, path)
  def getImage(id: String): Image = bctPlugin.getImageRegistry.get(id)
  def getImageD(id: String) = bctPlugin.getImageRegistry.getDescriptor(id)
}