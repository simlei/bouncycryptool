import sbt.{Resolver, _}
import sbt.Keys._
import org.jcryptool.manager.JCrypToolManager.autoImport._

object Settings {

  lazy val settingsBuild = Seq(
    scalaVersion := "2.12.4",
    organization := "org.jcryptool"
  )

  lazy val projectsSettings = Seq(
      publishMavenStyle := true,
      scalacOptions += "-Yrangepos",
      scalacOptions += "-Ypartial-unification",
      addCompilerPlugin("org.scalamacros" %% "paradise" % "2.1.0" cross CrossVersion.patch),
      resolvers += Resolver.mavenLocal
    )

  // ==== projects ================================================
  lazy val rootSettings: Seq[Setting[_]] = projectsSettings ++ Seq(
    name := "bouncycryptool",
    publishArtifact in Test := false
  )

  lazy val connectorSettings: Seq[Setting[_]] = projectsSettings ++ Seq(
    name := "bouncycryptool.connector",
    libraryDependencies ++= Dependencies.connectorDependencies,
    libraryDependencies += Dependencies.jctPlatformDependency
  )

  lazy val cryptoSettings: Seq[Setting[_]] = projectsSettings ++ Seq(
    name := "bouncycryptool.crypto",
    libraryDependencies ++= Dependencies.cryptoDependencies)

  lazy val uiSettings: Seq[Setting[_]] = projectsSettings ++ Seq(
    name := "simlei.ui",
    libraryDependencies ++= Dependencies.uiDependencies,
    libraryDependencies += Dependencies.jctPlatformDependency
  )

  lazy val logicSettings: Seq[Setting[_]] = projectsSettings ++ Seq(
    name := "simlei.logic",
    libraryDependencies ++= Dependencies.logicDependencies)

  lazy val toolsSettings: Seq[Setting[_]] = projectsSettings ++ Seq(
    name := "simlei.tools",
    libraryDependencies ++= Dependencies.toolsDependencies)

  // ==== scopes, tasks ================================================

  lazy val interactiveExecutionSettings = Seq(
    fork := true,
    connectInput := true,
    outputStrategy := Some(StdoutOutput),
    parallelExecution := false
  )

}
