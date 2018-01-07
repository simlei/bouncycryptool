package scalabc.connector.platform


import java.nio.charset.Charset
import java.nio.file.Path

import java.io.{ByteArrayInputStream, InputStream, File => JFile}
import better.files._

import scala.util.Try

trait Editors {
  def getAllManaged: Seq[Editor]
  def getManaged(file: File): Option[Editor] = {
    getAllManaged.filter(ed => ed.file == file).headOption
  }

  def getActive: Option[Editor]

  def setActive(ed: Editor) = ed.display.bringToFront()
  def setActive(file: File, kind: EditorKind): Unit = getAllManaged.filter((e: Editor) => e.file==file && e.display.kind == kind).headOption.foreach{_.display.bringToFront()}
  def setActive(file: File): Unit = getAllManaged.filter((e: Editor) => e.file==file).headOption.foreach{_.display.bringToFront()}
  def setActiveJavaFile(file: JFile): Unit = setActive(file.toScala)

  def editor(file: File, kind: EditorKind, pendingContent: Option[Array[Byte]] = None): Editor
  def editorDefaultBlank(kind: EditorKind = EditorKind.defaultText): Editor
  def editorDefaultSample(kind: EditorKind = EditorKind.defaultText): Editor
}
object Editors {

  var sessionTakenFiles: Set[File] = Set()

  def defaultTempDir = File(System.getProperty("java.io.tmpdir"))
  def nextFile(baseDir: File, name: String, ext: String, nrDigits: Int = 3): File = {
    for (nr <- Range(1, 9999)) {
      val counter = "0000000" + nr takeRight nrDigits
      val file = File(baseDir, s"$name$counter.$ext")
      if(! file.exists && ! sessionTakenFiles.contains(file)) {
        sessionTakenFiles = sessionTakenFiles + file
        return file
      }
    }
    sys.error("too many files in temporary directory already. Please clean them up.")
  }
  def createTempOutputFile(name: String, extension: String, content: Array[Byte] = Array(), baseDir: File = defaultTempDir): File = {
    createTempOutputFile(nextFile(baseDir, name, extension), content)
  }
  def createTempOutputFile(file: File, content: Array[Byte]): File = {
    file.writeByteArray(content)
    file.deleteOnExit
    file
  }
}

trait EditorKind
object EditorKind {
  val defaultText = TextKind(defaultCharset)
  val hex = BinaryKind(16)
}
case class BinaryKind(base: Int) extends EditorKind
case class TextKind(charset: Charset) extends EditorKind

trait EditorDisplay {
  def bringToFront(): Unit
  def kind: EditorKind
  def display(is: InputStream)
}

trait Editor {
  def file: File
  def display: EditorDisplay
  def pendingContent: Option[Array[Byte]] // reads the editor's visible content in disk-storage form

  final def fileContent: Option[Array[Byte]] = Try{file.byteArray}.toOption // file may not exist or been changed
  private[this] val fallbackContent: Array[Byte] = pendingContent.orElse(fileContent).getOrElse(Array()) // better than just using Array() as fallback
  def freshestContent: Array[Byte] = pendingContent.orElse(fileContent).getOrElse(fallbackContent)

  def replacePending(is: InputStream): Unit
  def replacePending(ba: Array[Byte]): Unit = replacePending(new ByteArrayInputStream(ba))
  def replacePending(s: String)(implicit charset:Charset = defaultCharset): Unit = replacePending(s.getBytes(charset))
}