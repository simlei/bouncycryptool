//package org.jcryptool
//
//import sbt._
//object JCTDetection {
//
//  val jctManagerVersion = "0.1-SNAPSHOT"
//  val jctTargetManagerVersion = "0.1-SNAPSHOT"
//  val sbtOsgiManagerVersion = "0.4.1.5-SNAPSHOT"
//
//  private[this] def rootSearch(currentDir: File): Seq[File] = Option(currentDir.getParentFile).map(p => rootSearch(p) :+ currentDir).getOrElse(Seq())
//  val identification = ".project-root.id"
//  val cfgFiles: Seq[Option[File]] = rootSearch(file(".").getAbsoluteFile) map (_ * identification) map (_.get.headOption)
//  val cfgFile = cfgFiles.flatten.lastOption.getOrElse(sys.error(s"No file '$identification' found in the parent directories."))
//  val masterDir = cfgFile.getParentFile.getAbsoluteFile
//  val pluginDir = (masterDir / "internal" / "sbt-jcryptool-manager").getAbsoluteFile
//  val p2TargetPluginDir = (masterDir / "internal" / "sbt-jcryptool-target-manager").getAbsoluteFile
//  val sbtosgijar = (masterDir / "internal" / "sbt-osgi.jar").getAbsoluteFile
//  val sbtOsgiPluginDir = (masterDir / "internal" / "sbt-osgi-manager")
//  if(pluginDir.exists()) pluginDir else sys.error(s"The directory for the managing plugin configured in '$cfgFile' -- '$pluginDir' -- does not exist.")
//
//  val jctPlugin = ProjectRef(pluginDir, "sbt-jcryptool-manager")
//  val jctTargetPlugin = ProjectRef(p2TargetPluginDir, "sbt-jcryptool-target-manager")
//  val sbtOsgiPlugin = ProjectRef(sbtOsgiPluginDir, "sbt-osgi-manager")
//
//  val jctPluginDep = addSbtPlugin("org.jcryptool" % "sbt-jcryptool-manager" % jctManagerVersion)
//  val jctTargetPluginDep = addSbtPlugin("org.jcryptool" % "sbt-jcryptool-target-manager" % jctTargetManagerVersion)
//  val sbtOsgiDep = addSbtPlugin("org.digimead" % "sbt-osgi-manager" % sbtOsgiManagerVersion)
//}
