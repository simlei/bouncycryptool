package scalabc.connector.app.views

import org.eclipse.jface.action.{Action, IMenuManager, IToolBarManager, Separator}
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.swt.SWT
import org.eclipse.swt.events.SelectionAdapter
import org.eclipse.swt.events.SelectionEvent
import org.eclipse.swt.layout.GridLayout
import org.eclipse.swt.widgets._
import org.eclipse.ui.part.ViewPart

import scalabc.connector.app.BouncyCrypToolApp
import scalabc.connector.app.BouncyCrypToolPlugin

object OperationEditorView {
  /**
    * The ID of the view as specified by the extension.
    */
  val ID = "org.jcryptool.bouncycryptool.plugin.views.OperationEditorView"

  private def leg = BouncyCrypToolPlugin.singleton.legacyStuff
}

class OperationEditorView()

/**
  * The constructor.
  */
  extends ViewPart {

  private var action_runAlgorithm: Action = null
  private var action_saveAsFavorite: Action = null
  private var action_saveAsFile: Action = null
  private var action_loadFromFile: Action = null
  private var pageComp: Composite = null
  private var lblMain: Label = null
  private var textCmd: Text = null
  private var runBtn: Button = null

  private def getIconForRunAlgo = OperationEditorView.leg.getImageD(OperationEditorView.leg.IMG_RUN)

  private def getIconForFavoriteAction = OperationEditorView.leg.getImageD(OperationEditorView.leg.IMG_STAR)

  private def getIconForSaveAction = OperationEditorView.leg.getImageD(OperationEditorView.leg.IMG_SAVE)

  private def getIconForLoadAction = OperationEditorView.leg.getImageD(OperationEditorView.leg.IMG_OPEN)

  /**
    * This is a callback that will allow us to create the viewer and initialize
    * it.
    */
  def createPartControl(parent: Composite): Unit = {
    makeActions()
    pageComp = new Composite(parent, SWT.NONE)
    pageComp.setLayout(new GridLayout(3, false))
    lblMain = new Label(pageComp, SWT.NONE)
    lblMain.setText("Enter a command: ")
    textCmd = new Text(pageComp, SWT.NONE)
    runBtn = new Button(pageComp, SWT.PUSH)
    runBtn.setText("Click me!")
    runBtn.addSelectionListener(new SelectionAdapter() {
      override def widgetSelected(e: SelectionEvent): Unit = {
        BouncyCrypToolApp.bct.services.bcOps.sudoExecuteThrowback("Look, I can write JCT Editors.\n" + textCmd.getText)
      }
    })
    contributeToActionBars()
  }

  private def contributeToActionBars(): Unit = {
    val bars = getViewSite.getActionBars
    fillLocalPullDown(bars.getMenuManager)
    fillLocalToolBar(bars.getToolBarManager)
  }

  private def fillLocalPullDown(manager: IMenuManager): Unit = {
    manager.add(action_runAlgorithm)
    manager.add(new Separator())
    manager.add(action_saveAsFavorite)
    manager.add(new Separator())
    manager.add(action_saveAsFile)
    manager.add(action_loadFromFile)
  }

  private def fillLocalToolBar(manager: IToolBarManager): Unit = {
    manager.add(action_runAlgorithm)
    manager.add(new Separator())
    manager.add(action_saveAsFavorite)
    manager.add(new Separator())
    manager.add(action_saveAsFile)
    manager.add(action_loadFromFile)
  }

  private def makeActions(): Unit = {
    action_runAlgorithm = new Action() {
      override def run(): Unit = {
        showMessage("Action 2")
      }
    }
    action_runAlgorithm.setText("Run algorithm")
    action_runAlgorithm.setToolTipText("Run the algorithm in the editor")
    action_runAlgorithm.setImageDescriptor(getIconForRunAlgo)
    action_saveAsFavorite = new Action() {
      override def run(): Unit = {
        showMessage("Action 1")
      }
    }
    action_saveAsFavorite.setText("Save as favorite")
    action_saveAsFavorite.setToolTipText("Save the action in this editor as a favorite")
    action_saveAsFavorite.setImageDescriptor(getIconForFavoriteAction)
    action_saveAsFile = new Action() {
      override def run(): Unit = {
        showMessage("Action 3")
      }
    }
    action_saveAsFile.setText("Save as file")
    action_saveAsFile.setToolTipText("Save the action in this editor as a file")
    action_saveAsFile.setImageDescriptor(getIconForSaveAction)
    action_loadFromFile = new Action() {
      override def run(): Unit = {
        showMessage("Action 4")
      }
    }
    action_loadFromFile.setText("Load from file")
    action_loadFromFile.setToolTipText("Load an operation from a file")
    action_loadFromFile.setImageDescriptor(getIconForLoadAction)
  }

  private def showMessage(message: String): Unit = {
    MessageDialog.openInformation(pageComp.getShell, "Bouncycastle Algorithms", message)
  }

  override def setFocus(): Unit = {
    pageComp.setFocus
  }
}