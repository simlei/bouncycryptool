package de.simlei.multimanager.projects

import de.simlei.multimanager.misc.fs.FSPresence
import sbt._

//trait ProjectFinder {
//  def findExisting(refDir: File): Option[File]
//}

//trait FinderByNameAndContent extends ProjectFinder {
//  def fileFilter: FileFilter
//  val foundFunction: File => Option[File]
//
//  override def findExisting(refDir: File): Option[File] = {
//    (refDir ** fileFilter).get.flatMap(foundFunction(_)).headOption
//  }
//}
//
//case class Finder(fileFilter: FileFilter, override val foundFunction: File => Option[File], recursive: Boolean = true) extends FinderByNameAndContent {
//  override def findExisting(refDir: File) = {
//    if(recursive) {
//      super.findExisting(refDir)
//    } else {
//      (refDir * fileFilter).get.flatMap(foundFunction(_)).headOption
//    }
//  }
//}
//object Finder {
//  def parentDirFunction: File => Option[File] = f => Option(f.getParentFile)
//  def contentMatchesId(id: String): File => Option[File] = (f: File) => {
//    if(IO.read(f).trim == id) {
//      Some(f)
//    } else None
//  }
//  def contentMatchesIdThenParent(id: String): File => Option[File] = (f: File) => {
//    if(IO.read(f).trim == id) {
//      Option(f.getParentFile)
//    } else None
//  }
//  def takeIfExists: File => Option[File] = Option(_)
//}

trait PresenceSpec[Pres <: FSPresence] { // specifies depending on a root directory
  def presenceConstructor: File => Pres
  def findExisting(refDir: File): Option[File]
  def searchRoot: File
  def defaultSubpath: String
  final def defaultFile: File = searchRoot / defaultSubpath

  def findRoot: Option[File] = findExisting(searchRoot)
  def findOrBail(): File = findExisting(searchRoot).getOrElse(sys.error(s"Spec: $this not found in $searchRoot."))

  def optionalVerifiedPresence(): Option[Pres] = findExisting(searchRoot).flatMap(presenceConstructor(_).asOption)
  def findPresenceOrBail(): Pres = presenceConstructor(findOrBail())
  def findVerifiedPresenceOrBail(): Pres = presenceConstructor(findOrBail()).apply()
}

case class ChildPatternSpec[Pres <: FSPresence](searchRoot: File, childPattern: String, presenceConstructor: File => Pres) extends PresenceSpec[Pres] {
  //TODO: better cleanup of patterns
  if(childPattern.contains("/")) sys.error("ChildPatternSpec does not accept patterns for nested folders. Only direct children specs allowed!")
  override def defaultSubpath: String = childPattern.replaceAllLiterally("*", "")
  override def findExisting(refDir: sbt.File) = (refDir * childPattern).get.headOption
}

case class SubpathSpec[Pres <: FSPresence](searchRoot: File, subpath: String, presenceConstructor: File => Pres) extends PresenceSpec[Pres] {
  //TODO: better cleanup of patterns
  override def defaultSubpath: String = subpath
  override def findExisting(refDir: sbt.File) = (searchRoot / subpath).get.headOption
}