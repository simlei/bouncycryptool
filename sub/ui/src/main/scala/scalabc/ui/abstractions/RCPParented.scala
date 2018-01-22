package scalabc.ui.abstractions

import org.eclipse.swt.widgets.Composite

trait RCPParented {

  private var rcpRoot: Option[Composite] = None
  def receiveRoot(root: Composite) = {
    this.rcpRoot = Some(root)
  }
  def root = rcpRoot.getOrElse(sys.error(s"$this has not yet been initialized by the RCP!"))

}
