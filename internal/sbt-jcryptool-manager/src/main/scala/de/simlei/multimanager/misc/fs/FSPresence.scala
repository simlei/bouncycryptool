package de.simlei.multimanager.misc.fs

import de.simlei.multimanager.Utils
import sbt._

trait FSPresence { self =>
//  def file: File
  def apply(): self.type = validate match {
    case Pass => this
    case e:Error => sys.error(s"Could not get $this: ${e.errors}")
  }
  def asOption: Option[self.type] = validate match {case Pass => Some(this); case _ => None}
  def validatedOrBail: self.type = validate match {
    case Pass => this;
    case Error(errs) => sys.error(Utils.makeExceptionMessage(s"'$this' is not valid. You have probably not downloaded or specified everything correctly. Those were the problematic files: ", errs.toSeq.map(pair => s"${pair._1} -> 'Predicate ${pair._2}' failed")))
  }
  def validate: PassOrError // either Pass[T] or Fail(Map[FSPresence[_], String])
}

trait FSHasRootFile extends FSPresence {
  def file: File
}

trait FSJust extends FSHasRootFile {
  def spec: Spec
  override def validate: PassOrError = spec.check(this)
}

trait FSEnsemble extends FSPresence {
  def fsEntries: Seq[FSPresence] = Seq()

  override def validate: PassOrError = ((fsEntries) map (_.validate)).foldLeft[PassOrError](Pass)((r1, r2) => r1.append(false)(r2))
}

trait FSEnsembleDir extends FSEnsemble with FSHasRootFile {
  def dir: File
  override final def file = dir
  override def fsEntries: Seq[FSPresence] = super.fsEntries :+ DirPresence(dir)
}

trait FSFile extends FSJust {
  override def spec: Spec = Spec.isFile
}

trait FSDir extends FSJust {
  override def spec: Spec = Spec.isDir
  def dir: File
  override final def file = dir
}

case class FilePresence(file: File) extends File(file.getAbsolutePath) with FSFile
object FilePresence {
  implicit def conv(f: File) = FilePresence(f)
}
case class DirPresence(dir: File) extends File(dir.getAbsolutePath) with FSDir
object DirPresence {
  implicit def conv(f: File) = DirPresence(f)
}

trait Spec {
  def name: String
  def check(fs: FSPresence): PassOrError
}
object Spec {
  def childOf(parent: File) = GenSpec(s"is child of $parent", file => IO.relativize(parent, file).isDefined)
  def hasChildNamed(pattern: String) = GenSpec(s"has direct child named $pattern", file => (file * pattern).get.headOption.isDefined)
  def exists = GenSpec("exists", _.exists())
  def isFile = exists and GenSpec("is file", _.exists())
  def isDir = exists and GenSpec("is directory", _.exists())

  implicit class SpecOps(spec: Spec) {
    def and(other: Spec) = CombinedSpec(spec, other, failfast = true)
    def also(other: Spec) = CombinedSpec(spec, other, failfast = false)
  }
}

trait FSpec extends Spec {
  def fileCheck: File => Boolean
  final override def check(fs: FSPresence): PassOrError = fs match {
    case j:FSHasRootFile => if(fileCheck(j.file)) Pass else Error(fs, this)
    case _ => Pass
  }
}

sealed trait PassOrError {
  def append(failfast: Boolean)(other: => PassOrError): PassOrError
}
case class Error(errors: Map[FSPresence, Spec]) extends PassOrError {
  override def append(failfast: Boolean)(other: => PassOrError): PassOrError = {
    if(failfast) this else {
      other match {
        case Pass => this;
        case f2:Error => Error(this.errors ++ f2.errors)
      }
    }
  }
}
case object Pass extends PassOrError {
  override def append(failfast: Boolean)(other: => PassOrError): PassOrError = other
}

object Error {
  def apply(subject: FSPresence, spec: Spec): Error = Error(Map(subject -> spec))
}

case class CombinedSpec(s1: Spec, s2: Spec, failfast: Boolean) extends Spec {
  override def name: String = toSeq.mkString("(", " and ", ")")
  //    override def check: FSPresence => FailOrPass = Spec.checkSeq()
  def toSeq: Seq[Spec] = (s1, s2) match {
    case (spec1: CombinedSpec, spec2: CombinedSpec) => spec1.toSeq ++ spec2.toSeq
    case (spec1: CombinedSpec, spec2) => spec1.toSeq :+ spec2
    case (spec1, spec2: CombinedSpec) => spec1 +: spec2.toSeq
    case (spec1, spec2) => Seq(spec1, spec2)
  }

  override def check(fs: FSPresence): PassOrError = toSeq.foldLeft(Pass: PassOrError)((fop: PassOrError, spec: Spec) => fop.append(failfast)(spec.check(fs)))
}
case class GenSpec(name: String, fileCheck: File => Boolean) extends FSpec {
  override def toString: String = s"Spec($name)"
}