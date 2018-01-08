name := "sbt-jcryptool-target-manager"

// For information on how to use this plugin, see the accompanying Readme.md document.
organization := "org.jcryptool"
name := "sbt-jcryptool-target-manager"
description := "Management and scripts of the jcryptool project"

scalaVersion := "2.10.6"
version  := "0.1.1-SNAPSHOT"

licenses += ("BSC 2-clause", url("https://opensource.org/licenses/BSD-2-Clause"))

sbtPlugin := true
publishMavenStyle := false

// parent plugins:

val osgiPlugin = ProjectRef(file("./sbt-osgi-manager"), "sbt-osgi-manager")
dependsOn(osgiPlugin)