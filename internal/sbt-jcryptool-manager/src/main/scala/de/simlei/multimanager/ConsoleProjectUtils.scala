package de.simlei.multimanager

import org.jcryptool.structure.JCTLayout
import sbt._
import sbt.Keys._

import scala.collection.GenTraversableOnce

object ConsoleProjectUtils {
  val addSettings: Seq[Def.Setting[_]] = addCommandAlias("startJCTConsole", "consoleProject") ++ Seq(

  )


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
    """.stripMargin

  def valDefs(vals: Map[String, String]) = {
    vals.map{ case (valId, assignedId) =>
      s"val $valId = $assignedId;"
    }.mkString("", ";\n", "\n")
  }

  def initialCommands(initBannerIdentifier: String,
                      welcomeBannerIdentifier: String,
                      valsForConsole: Map[String, String],
                      spinupMillis: Int): String = {
    valDefs(valsForConsole) + s"""
       |println($initBannerIdentifier)
       |${spinupMessageCmd(welcomeBannerIdentifier, spinupMillis)}
       |jline.TerminalFactory.get.init
       |def exit = {
       |  jline.TerminalFactory.get.restore
       |  sys.exit()
       |}
       |import org.jcryptool.consolehelp.ExternalHelp._
       |""".stripMargin
  }

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
