package org.jcryptool.manager

import sbt.Keys._
import sbt._

object ConnectorProject {

  object ConnectorKeys {
    val applyEclipseTemplate = taskKey[Unit]("writes custom eclipse files")

    val templateDirectory = settingKey[File]("the directory with template files")
    val managedJarsDirectory = settingKey[File]("directory where the sbt-managed libraries get resolved into")
    val eclipseProjectId = settingKey[String]("the eclipse project id")
    val pdeActivatorClass = settingKey[String]("the eclipse plugin activator class extending an eclipse plugin class e.g. AbstractUIPlugin")
    val binSourceClasspath = taskKey[Seq[(File, Option[File])]]("pair of binary jar files and optional sources to be included in the classpath")
    val pdeImportedPackagesList = settingKey[Seq[String]]("imported packages for the manifest file (so eclipse can work out P2 dependencies)")
    val pdeRequiredBundlesList = settingKey[Seq[String]]("required bundles for the manifest file (so eclipse can work out P2 dependencies)")
  }
  import ConnectorKeys._

  def defaultConnectorSettings: Seq[Setting[_]] = GeneralScalaProject.defaultBuildSettings ++ Seq(
    retrieveManaged := true,
    templateDirectory := baseDirectory.value / "eclipse_template",
    managedJarsDirectory := baseDirectory.value / "lib_managed",
    eclipseProjectId := organization.value + "." + name.value,
    binSourceClasspath := {
      val managedDir = managedJarsDirectory.value
      val jars = ( managedDir ** "*.jar") --- ( managedDir ** "srcs" ** "*.jar")
      jars.get.map(jar => (jar, None))
    }

  )

}
