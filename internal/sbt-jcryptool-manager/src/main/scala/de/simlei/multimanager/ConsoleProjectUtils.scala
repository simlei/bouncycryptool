package de.simlei.multimanager

import sbt._
import sbt.Keys._

object ConsoleProjectUtils {

  def spinupMessageCmd(welcomeBannerIdentifier: String, spinupDurationMillis: Int) =
    s"""
      |import scala.concurrent.{Future => ConcurrentTask}           // rename
      |      import scala.concurrent.ExecutionContext.Implicits.global
      |      val task = ConcurrentTask {
      |        Thread.sleep($spinupDurationMillis)
      |        print($welcomeBannerIdentifier)
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
       |""".stripMargin
  }

  def jlineWorkarounds = Def.settings(
    Seq(
      console        in Compile,
      console        in Test,
      consoleProject in Compile,
      consoleProject in Test,
    ) map { key =>
      key := key
        .dependsOn(Def.task { jline.TerminalFactory.get.init })
        .andFinally(jline.TerminalFactory.get.restore)
        .value
    }: _*)

}
