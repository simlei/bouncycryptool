package scalabc.connector.app.coats

import java.util
import java.util.function.Consumer
import java.util.{List, Optional}

import com.diffplug.common.rx.{Rx, RxBox}
import com.diffplug.common.swt.jface.{ColumnViewerFormat, ViewerMisc}
import com.diffplug.common.swt._
import com.diffplug.common.tree.{TreeNode, TreeStream}
import org.eclipse.jface.viewers.{ArrayContentProvider, TableViewer, TreeViewer}
import org.eclipse.swt.SWT._
import org.eclipse.swt.widgets._
import org.jcryptool.bouncycastle.core.algorithms.AlgorithmReg
import org.jcryptool.bouncycastle.core.operation.digest.DigestSpec

import scala.collection.JavaConverters._
import scala.compat.java8.OptionConverters._

object AlgorithmViewCoat extends Coat {

  val testData: Seq[TreeNode[DigestSpec]] = {
    var lastNode: TreeNode[DigestSpec] = null
    var rootNode: TreeNode[DigestSpec] = null
    var seq: Seq[TreeNode[DigestSpec]] = Seq()
    AlgorithmReg.getInstanceDefault.getSpecs_digest.asScala.foreach{ spec =>
      var myNode = new TreeNode[DigestSpec](lastNode, spec)
      seq = seq :+ myNode
      if(lastNode == null) {
        rootNode = myNode
      }
      lastNode = myNode
    }
    seq
  }

  private class MyTable(val cmp: Composite, val style: Int) {
    val wrapCmp = new Composite(cmp, NONE)
    Layouts.setGridData(wrapCmp).grabExcessHorizontalSpace(true).grabExcessVerticalSpace(true).horizontalAlignment(FILL).verticalAlignment(FILL)
    Layouts.setFill(wrapCmp)

    val format: ColumnViewerFormat[TreeNode[DigestSpec]] = ColumnViewerFormat.builder[TreeNode[DigestSpec]]
    format.setStyle(style | FULL_SELECTION)
    format.addColumn.setText("Digest ID").setLabelProviderText{_.getContent.id_name}
    format.addColumn.setText("Engine").setLabelProviderText(_.getContent.engine.toString)
    // create a table
    val table: TableViewer = format.buildTable(new Composite(wrapCmp, BORDER))
    table.setContentProvider(new ArrayContentProvider)
    val listInput: util.List[TreeNode[DigestSpec]] = testData.asJava
    table.setInput(listInput)
    // and a tree
//    tree = format.buildTree(new Composite(wrapCmp, BORDER))
//    ViewerMisc.setTreeContentProvider(tree, TreeNode.treeDef)
//    tree.setInput(testData)

  }

  override def putOn(cmp: Composite): Unit = {
    Layouts.setGrid(cmp).numColumns(1)

    val lbl = new Label(cmp, NONE)
    Layouts.setGridData(lbl).grabExcessHorizontalSpace(true).horizontalAlignment(FILL)

//    val t1Box = SwtRx.textImmediate(text1)

    val table = new MyTable(cmp, SINGLE)
//    val lh: LayoutsGridData = Layouts.setGridData(table.table.getTable)
//      .grabExcessHorizontalSpace(true).grabExcessVerticalSpace(true)
//      .horizontalAlignment(FILL).verticalAlignment(FILL)

    val box: RxBox[Optional[TreeNode[DigestSpec]]] =
      ViewerMisc.singleSelection(table.table)

    val reactor: Consumer[Optional[TreeNode[DigestSpec]]] = (x: Optional[TreeNode[DigestSpec]]) => {
      println(x.asScala.map[String](_.getContent.id_name).getOrElse("None selected"))
    }
    Rx.subscribe(box, reactor)

  }
}
