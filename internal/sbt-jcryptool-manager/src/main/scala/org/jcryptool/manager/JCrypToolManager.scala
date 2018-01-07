package org.jcryptool

import de.simlei.multimanager.ConsoleProjectUtils
import de.simlei.multimanager.ConsoleProjectUtils.jlineWorkarounds
import org.jcryptool.structure.JCTLayout
import sbt._
import sbt.Keys._

object JCrypToolManager extends AutoPlugin {

  override def trigger = allRequirements // auto-activation

  object deps {

  }

  object autoImport {
    val bctMainDirectory = settingKey[File]("Main directory of the bouncyCrypTool project")
    val bctLayout = settingKey[JCTLayout]("layout descriptor of the BouncyCrypTool and JCrypTool projects")
  }

  import autoImport._



  lazy val defaultSettings: Seq[Def.Setting[_]] = Seq(
  )

  val valsForConsole: Map[String, String] = Map(
    "jct" -> "bctLayout.eval",
    "core" -> "jct.core"
  )

  def bctSettings: Seq[Def.Setting[_]] = Seq(
    bctMainDirectory := (baseDirectory in ThisBuild).value,
    bctLayout := JCTLayout(bctMainDirectory.value).validatedOrBail,
    initialCommands in consoleProject +=
      ConsoleProjectUtils.initialCommands("jct.internal.initializationBanner", "jct.internal.welcomeContent", valsForConsole, 3000)

  )

  override def projectSettings: Seq[Def.Setting[_]] = bctSettings ++ jlineWorkarounds

}