package scalabc.connector.app.coats

import com.diffplug.common.swt.{Coat, Layouts, LayoutsGridData, SwtRx}
import org.eclipse.swt.SWT
import org.eclipse.swt.SWT.{FILL, PUSH, Selection}
import org.eclipse.swt.widgets.{Button, Composite, Text}

import scalabc.connector.app.BouncyCrypToolApp

object Testcoat1 extends Coat {
  override def putOn(cmp: Composite): Unit = {
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

    btn.addListener(Selection, e => BouncyCrypToolApp.logger.logMsg(s"button click $e received"))
  }
}
