package scalabc.ui.examples

import org.eclipse.swt.SWT // - for scoped access
import org.eclipse.swt.SWT._ //{FILL, PUSH, Selection, Traverse}
import org.eclipse.swt.widgets // - for scoped access
import org.eclipse.swt.widgets._ //{Button, Composite, Text}
import com.diffplug.common.swt // - for scoped access
import com.diffplug.common.swt._ //{Coat, Layouts, Shells, SwtRx}

object TestApp extends App {
  Shells.builder(SWT.DIALOG_TRIM, TestAppImpl.testCoat)
    .setTitle("Confirm operation")
    .setSize(500, 300) // set the width, pack height to fit contents
    .openOnDisplayBlocking()

}

object TestAppImpl {
  val testCoat: Coat = testCoatImpl; //TODO: make work?: (testCoatImpl _)

  def testCoatImpl(cmp: Composite): Unit = {
    Layouts.setGrid(cmp).numColumns(2)

    val text1 = new Text(cmp, SWT.NONE)
    Layouts.setGridData(text1).grabExcessHorizontalSpace(true).horizontalAlignment(FILL)
    val text2 = new Text(cmp, SWT.NONE)
    Layouts.setGridData(text2).grabExcessHorizontalSpace(true).horizontalAlignment(FILL)

    val t1Box = SwtRx.textImmediate(text1)
    val t2Box = SwtRx.textConfirmed(text2)

    val btn = new Button(cmp, PUSH)
    btn.setText("Push me. and then just touch me. Until I get my.")
    Layouts.setGridData(btn).horizontalSpan(2).horizontalAlignment(FILL).grabExcessHorizontalSpace(true)

    t1Box.set("Hello, ")
    t2Box.set("World!")

    btn.addListener(Selection, e => println("Click!"))

    cmp.addListener(Traverse, e => {
      if (e.detail == TRAVERSE_ESCAPE) {
        e.doit = false
        cmp.getShell.close() //TODO: practice: do it with executor
      }
    })
  }
}