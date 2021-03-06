import sbt._

object Dependencies {


  lazy val version = new {
    val scalaTest = "3.0.1"
    val scalaCheck = "1.13.4"

    val monocle = "1.5.0-cats-M1"
    val goggles = "1.0"

    val cats = "1.0.0-MF"
    val circe = "0.9.0-M1"

    val scalalogging = "3.7.2"
    val enumeratum = "1.5.12"
    val betterfiles = "3.2.0"
    val javacompat = "0.8.0"

    val eff = "4.6.0"
    val sourcecode = "0.1.4"
    val pureconfig = "0.8.0"
    val circeconfig = "0.3.0"
    val refined = "0.8.4"
    val attoparser = "0.6.1-M1"

    val bouncycastle = "1.59"
    val ammonite = "1.0.3"
    val durianswt = "3.0.0.BETA2"

    val scala_Rx = "0.26.5"
    val monix = "3.0.0-M1"
    val liRx = "0.3.2"

    val jctworkspace = "0.22-SNAPSHOT"
  }

  lazy val library = new {

    // TEST

    val scalactic = "org.scalactic" %% "scalactic" % version.scalaTest
    val test = "org.scalatest" %% "scalatest" % version.scalaTest
    val check = "org.scalacheck" %% "scalacheck" % version.scalaCheck
    val testlibs = Seq(scalactic, test, check)

    // OPTICS

    val monocle = Seq(
      "com.github.julien-truffaut" %% "monocle-core" % version.monocle,
      "com.github.julien-truffaut" %% "monocle-macro" % version.monocle
    )

    val goggles = Seq(
      "com.github.kenbot" %% "goggles-dsl" % version.goggles,
      "com.github.kenbot" %% "goggles-macros" % version.goggles
    )

    val optics = monocle //++ goggles

    // FUNCTIONAL

    val helpers = Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % version.scalalogging,
      "ch.qos.logback" % "logback-classic" % "1.1.3",
      "com.beachape" %% "enumeratum" % version.enumeratum,
      "com.github.pathikrit" %% "better-files" % version.betterfiles,
      "org.scala-lang.modules" % "scala-java8-compat_2.12" % version.javacompat
    )

    val fctHelpers = Seq(
      "com.lihaoyi" %% "sourcecode" % version.sourcecode

    //      "org.typelevel" %% "cats-core" % version.cats,
    //      "io.circe" %% "circe-core" % version.circe,
    //      "io.circe" %% "circe-generic" % version.circe,
    //      "io.circe" %% "circe-parser" % version.circe,

    //      "org.tpolecat" %% "atto-core" % version.attoparser,

    //      "com.github.pureconfig" %% "pureconfig" % version.pureconfig,
    //      "io.circe" %% "circe-config" % version.circeconfig,

    //      "org.atnos" %% "eff" % version.eff,

    //      "eu.timepit" %% "refined" % version.refined,
    //      "eu.timepit" %% "refined-pureconfig" % version.refined
    )

    // CRYPTO

    val bouncycastle = Seq(
      "org.bouncycastle" % "bcprov-jdk15on" % version.bouncycastle,
      "org.bouncycastle" % "bcpkix-jdk15on" % version.bouncycastle
    //      ,"org.bouncycastle" % "bcprov-ext-jdk15on" % version.bouncycastle
    )

    // UI

    val ammonite = Seq("com.lihaoyi" % "ammonite" % version.ammonite cross CrossVersion.patch)

    val reactiveAndUI = Seq(
      "com.diffplug.durian" % "durian-swt" % version.durianswt // contains javaRx
//      "org.scala-lang.modules" %% "scala-async" % "0.9.6",
//      "io.monix" %% "monix" % version.monix,
//      "com.lihaoyi" %% "scalarx" % version.liRx
    )

  }

  import library._

  val connectorDependencies: Seq[ModuleID] = testlibs ++ helpers ++ reactiveAndUI// ++ fctHelpers
  val uiDependencies: Seq[ModuleID] = testlibs ++ helpers ++ reactiveAndUI ++ ammonite// ++ fctHelpers ++ optics
  val logicDependencies: Seq[ModuleID] = testlibs ++ helpers// ++ fctHelpers ++ optics
  val cryptoDependencies: Seq[ModuleID] = testlibs ++ helpers ++ bouncycastle// ++ fctHelpers
  val toolsDependencies: Seq[ModuleID] = testlibs ++ helpers ++ reactiveAndUI ++ ammonite // ++ optics ++ fctHelpers

  val jctPlatformDependency: ModuleID = "org.jcryptool" %% "bouncycryptool-platform" % "0.1.0-SNAPSHOT" classifier("assembly")
  //TODO: add enumeratum

}
