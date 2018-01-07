package scalabc.connector.platform.jct.editors


import scala.collection.JavaConverters._
import better.files._

import scala.util.Try
import scalabc.connector.platform._

import java.io.{ByteArrayInputStream, InputStream, File => JFile}

import org.eclipse.core.runtime.FileLocator
import org.eclipse.ui._
import org.jcryptool.core.operations.editors.{EditorsManager}
import org.jcryptool.core.operations.util.PathEditorInput
import org.jcryptool.editor.text.editor.JCTTextEditor
import org.jcryptool.core.operations.{IOperationsConstants, OperationsPlugin}
import org.jcryptool.core.util.directories.DirectoryService
import org.jcryptool.core.operations.editors.{Messages => AbstractEditorMsg}
import org.jcryptool.core.util.constants.{IConstants => JCTConstants}

object JCTEditors extends Editors {
  override def getAllManaged: Seq[JCTEditor] = jctMan.getEditorReferences.asScala.map(JCTEditor.ofExistingRef) //TODO: put off: is equaility consistent? no...

  def getActive: Option[JCTEditor] =
    Option(jctMan.getActiveEditorReference).flatMap(
      activeRef => getAllManaged.filter(
        (ed: JCTEditor) => ed.display.asInstanceOf[JCTEdDisplay].ref.equals(activeRef)
      ).headOption
    )

  override def editor(file: File, kind: EditorKind, pendingContent: Option[Array[Byte]] = None): JCTEditor = JCTEditor.ofReusing(file, kind, pendingContent)
  override def editorDefaultBlank(kind: EditorKind): Editor = JCTEditor.ofFresh(createSampleBlankFile, kind)
  override def editorDefaultSample(kind: EditorKind): Editor = JCTEditor.ofFresh(createSampleTempFile, kind)

  // -- JCT specific

  def templateFileContent: Array[Byte] = File(FileLocator.toFileURL(OperationsPlugin.getDefault().getBundle().getEntry("/")).getFile() + "templates" + JFile.separatorChar + AbstractEditorMsg.AbstractEditorService_3).byteArray //TODO: put off: generalize JCT templates
  def templateFilePrefix: String = AbstractEditorMsg.AbstractEditorService_2
  def templateFileExt = JCTConstants.TXT_FILE_TYPE_EXTENSION

  def createSampleTempFile: File = Editors.createTempOutputFile(templateFilePrefix, templateFileExt, templateFileContent)
  def createSampleBlankFile: File = Editors.createTempOutputFile(templateFilePrefix, templateFileExt)

  // -- helper methods

  def fileFromInput(input: IEditorInput): File = Try {
    input.asInstanceOf[PathEditorInput].getPath.toFile().toScala
  }.getOrElse(sys.error(s"could not get file from input ${input}"))
  def fileFromRef(ref: IEditorReference): File = Try {
    ref.getEditorInput.asInstanceOf[PathEditorInput].getPath.toFile().toScala
  }.getOrElse(sys.error(s"could not get file from IEditorRef ${ref}"))

  def contentFromRef(ref: IEditorReference): Array[Byte] = jctMan.getContentInputStream(ref.getEditor(false)).bytes.toArray
  def editorInputFromFile(file: File) = new PathEditorInput(new org.eclipse.core.runtime.Path(file.toJava.getAbsolutePath))
}


case class JCTEditor(
                      val file: File,
                      val display: JCTEdDisplay
                    ) extends Editor {
  def isDisposed = Try{display.ref.getEditor(false)}.isSuccess //TODO: later: test & improve editor resource management

  override def replacePending(is: InputStream): Unit = {
    val newContent = is.bytes.toArray
    display.display(new ByteArrayInputStream(newContent))
  }
  override def pendingContent: Option[Array[Byte]] = Try{JCTEditors.contentFromRef(display.ref)}.toOption

  def ref: IEditorReference = display.ref
  def isOrphan = File(DirectoryService.getTempDir()).isParentOf(this.file) //TODO: put off: check by writability
}
object JCTEditor {
  import JCTEditorKind._
  import JCTEditors._

  def ofFresh(file: File, kind: EditorKind, pendingContent: Option[Array[Byte]] = None): JCTEditor = {
    val input = editorInputFromFile(file)
    val openedPart = PlatformUI.getWorkbench.getWorkbenchWindows()(0).getActivePage.openEditor(input, kind.rcpId)
    JCTEditor.ofExistingRef(jctMan.getActiveEditorReference) // TODO: get from the actual object above, e.g. ((PartSite) part.getSite()).getPartReference()
  }
  def ofReusing(file: File, kind: EditorKind, pendingContent: Option[Array[Byte]] = None): JCTEditor = {
    def adaptability(file: File, kind: EditorKind)(ed: JCTEditor): Boolean = (! ed.isDisposed) && (ed.file, ed.display.kind) == (file, kind)
    def adaption(content: Array[Byte])(ed: JCTEditor): JCTEditor = { //TODO: put off: further generalize this adaption (content does mot matter) for reusing editors
      val contentMatches = ed.pendingContent.map(_ == content).getOrElse(false)
      if(! contentMatches) ed.replacePending(content)
      ed
    }
    JCTEditors.getAllManaged.filter(adaptability(file, kind)).headOption.map(ed => pendingContent.map(adaption(_)(ed)).getOrElse(ed)).getOrElse(ofFresh(file, kind))
  }
  def ofExistingRef(ref: IEditorReference): JCTEditor = {
    val jctEdPart = ref.getEditor(false)
    JCTEditor(fileFromRef(ref), JCTEdDisplay.ofExistingRef(ref))
  }
}

class JCTEdDisplay(val ref: IEditorReference) extends EditorDisplay {
  def bringToFront(): Unit = {
    ref.getEditor(true)
  }
  def kind: EditorKind = JCTEditorKind.idToEditorKind(ref.getEditor(false).getEditorSite.getId)
  def display(is: InputStream): Unit = {
    val editorPart = ref.getEditor(false)
    val service = EditorsManager.getInstance().getServiceFor(editorPart)
    service.setContentOfEditor(editorPart, is)
  }
}
object JCTEdDisplay {
  def ofExistingRef(ref: IEditorReference): JCTEdDisplay = new JCTEdDisplay(ref)
}

object JCTEditorKind {
  val kinds = Set(EditorKind.defaultText, EditorKind.hex)

  implicit def jctEditorKindToId(kind: EditorKind): RCPId = kind match {
      //TODO: put off: couple editor ids more elegant
    case _:BinaryKind => IOperationsConstants.ID_HEX_EDITOR
    case _:TextKind => JCTTextEditor.ID
    case _ => sys.error("no matching RCP id found for editor kind")
  }
  def idToEditorKind(id: String) = kinds.filter(_.rcpId == id).headOption
    .getOrElse(sys.error(s"No JCT editor kind for id $id"))


}
