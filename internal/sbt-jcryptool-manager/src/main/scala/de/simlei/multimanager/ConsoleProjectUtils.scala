package de.simlei.multimanager

import org.jcryptool.structure.JCTLayout
import sbt._
import sbt.Keys._

import scala.collection.GenTraversableOnce

object ConsoleProjectUtils {
  val addSettings: Seq[Def.Setting[_]] = Seq(
    commands ++= Seq(interactiveConsoleCmd, noninteractiveConsoleCmd, clearInitialCommands)
  )

  val valsForConsole: Seq[(String, String)] = Seq(
    "jct" -> "bctLayout.eval",
    "core" -> "jct.projects.core",
    "crypto" -> "jct.projects.crypto",
    "bouncycryptool" -> "jct.projects.bouncycryptool",
    "targetPlatformExtractor" -> "jct.projects.bouncycryptool.projects.jctPlatformExtractor",
    "api_bctPlugin" -> "jct.projects.core.projects.bctConnector.api",
    "api_build" -> "jct.projects.core.api_build",
    "api_p2Extractor" -> "jct.projects.bouncycryptool.projects.jctPlatformExtractor.api"
  )

  def initCmdsSett(initCmds: String) = Seq(initialCommands in consoleProject := initCmds)

  def clearInitialCommands = Command.command("clearInitialCommands") { state =>
    Project.extract(state).append(initCmdsSett(""), state)
  }
  def runAndResetConsole(initialCommands: Seq[String]): State => State = { state =>
    val withInitialCmds = Project.extract(state).append(initCmdsSett(initialCommands.mkString("\n")), state)
    withInitialCmds.copy(remainingCommands =
      Exec("consoleProject", None) +: Exec("clearInitialCommands", None) +: withInitialCmds.remainingCommands
    )
  }

  def interactiveConsoleCmd = Command.args("startJCTConsole", "[initial command(s)]"){ case (state, args) =>
    val initialCmds = Seq(
      cslInit_globalBindings(valsForConsole),
      cslInit_banners("jct.projects.bouncycryptool.files.initializationBanner", "jct.projects.bouncycryptool.files.welcomeContent", 500),
      cslInit_jlineUtilCommands,
      cslInit_externalHelpImport
    ) ++ args
    runAndResetConsole(initialCmds)(state)
  }

  def noninteractiveConsoleCmd = Command.single("inJCTConsole"){ case (state, arg) =>
    val initialCmds = Seq(
      cslInit_globalBindings(valsForConsole),
      cslInit_jlineUtilCommands,
      cslInit_externalHelpImport
    ) :+ arg :+ "println(\"Finished; shutting down... the TrapExitSecurityException is normal & due to a workaround.\")" :+ "exit"
    runAndResetConsole(initialCmds)(state)
  }

  def printImportedVals: String = {
    def expandStr(str: String, i: Int) = str + " "*i
    val maxW = valsForConsole.map(_._1).foldLeft(0)((max: Int, s: String) => if(s.size > max) s.size else max)

    valsForConsole.drop(1).map{ case (now: String, normally: String) =>
      s"  ${expandStr(now, maxW - now.size)}  <--is normally-- $normally"
    }.mkString("\n")
  }

  def spinupMessageCmd(welcomeBannerIdentifier: String, spinupDurationMillis: Int) =
    s"""
      |import scala.concurrent.{Future => ConcurrentTask}           // rename
      |      import scala.concurrent.ExecutionContext.Implicits.global
      |      val task = ConcurrentTask {
      |        val steps = 20
      |        val interval = $spinupDurationMillis / 1
      |        Thread.sleep(interval)
      |        print($welcomeBannerIdentifier + "\\n\\nscala> ")
      |      }
    """.stripMargin.stripLineEnd

  def valDefs(vals: Seq[(String, String)]) = {
    vals.map{ case (valId, assignedId) =>
      s"val $valId = $assignedId"
    }.mkString("", "\n", "\n")
  }

  def cslInit_externalHelpImport = "import org.jcryptool.consolehelp.ExternalHelp._"
  def cslInit_banners(initBannerIdentifier: String,
                      welcomeBannerIdentifier: String,
                      spinupMillis: Int) =
    s"""
      |println($initBannerIdentifier)
      |${spinupMessageCmd(welcomeBannerIdentifier, spinupMillis)}
    """.stripMargin.stripLineEnd
  def cslInit_jlineUtilCommands =
    """
      |jline.TerminalFactory.get.init
      |def exit = {
      |  jline.TerminalFactory.get.restore
      |  sys.exit()
      |}
    """.stripMargin.stripLineEnd

  def cslInit_globalBindings(bindings: Seq[(String, String)]) = valDefs(bindings).stripLineEnd

  def defaultInteractiveCmds(initBannerIdentifier: String,
                             welcomeBannerIdentifier: String,
                             bindings: Seq[(String, String)],
                             spinupMillis: Int) = Seq(
    cslInit_globalBindings(bindings),
    cslInit_banners(initBannerIdentifier, welcomeBannerIdentifier, spinupMillis),
    cslInit_jlineUtilCommands,
    cslInit_externalHelpImport
  )

//  def interactiveInitialCommands(initBannerIdentifier: String,
//                                 welcomeBannerIdentifier: String,
//                                 valsForConsole: Seq[(String, String)],
//                                 spinupMillis: Int): String = {
//    val init = valDefs(valsForConsole) + s"""
//       |println($initBannerIdentifier)
//       |${spinupMessageCmd(welcomeBannerIdentifier, spinupMillis)}
//       |jline.TerminalFactory.get.init
//       |def exit = {
//       |  jline.TerminalFactory.get.restore
//       |  sys.exit()
//       |}
//       |import org.jcryptool.consolehelp.ExternalHelp._
//       |""".stripMargin
//    init
//  }

//  def jlineWorkarounds = Def.settings(
//    Seq(
//      console        in Compile,
//      console        in Test,
//      consoleProject in Compile,
//      consoleProject in Test,
//    ) map { key =>
//      key := key
//        .dependsOn(Def.task {jline.TerminalFactory.get.init })
//        .andFinally({
//          jline.TerminalFactory.get.restore
//        })
//        .value
//    }: _*)

}
