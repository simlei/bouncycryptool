package org.jcryptool.manager

import sbt._
import sbt.Keys._

object GeneralScalaProject {
  def defaultBuildSettings: Seq[Setting[_]] = Seq(
  )
  def defaultProjectSettings: Seq[Setting[_]] = Seq(
  )
}
// old typelevel stuff
//    crossScalaVersions := Seq("2.10.6", "2.11.11", "2.12.4", "2.13.0-M2"),
//    scalaOrganization := "org.typelevel",
//scalaVersion := "2.12.4"//-bin-typelevel-4"