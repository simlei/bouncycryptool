import sbt._
import sbt.osgi.manager._
import org.jcryptool.osgi._

organization := "org.jcryptool"
name := "bouncycryptool-platform"
scalaVersion := "2.12.4"
version := "1.0.0-SNAPSHOT"

//autoScalaLibrary := false // TODO: build: enable this to get rid of scala-lang dependencies. but doesn't work right now with sbt-assembly

OSGiManager
enablePlugins(JCTTargetManager)

publishMavenStyle := true