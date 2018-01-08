package org.jcryptool.manager

import de.simlei.multimanager.ConsoleProjectUtils
import org.jcryptool.structure.JCTLayout
import sbt._
import sbt.Keys._

object JCrypToolManager extends AutoPlugin {

  override def trigger = allRequirements // auto-activation

  object deps {

  }

  object autoImport {
    val bctValsInConsole = settingKey[Seq[(String, String)]]("imported values into the console")
    val bctMainDirectory = settingKey[File]("Main directory of the bouncyCrypTool project")
    val bctLayout = settingKey[JCTLayout]("layout descriptor of the BouncyCrypTool and JCrypTool projects")

    val bctTargetPlatformDependency = settingKey[ModuleID]("JCT target platform dependency")
  }

  import autoImport._


  val valsForConsole: Seq[(String, String)] = Seq(
    "jct" -> "bctLayout.eval",
    "core" -> "jct.projects.core",
    "crypto" -> "jct.projects.crypto",
    "bouncycryptool" -> "jct.projects.bouncycryptool",
    "targetPlatformExtractor" -> "jct.projects.bouncycryptool.projects.jctPlatformExtractor",
    "api_build" -> "jct.projects.core.api_build",
    "api_p2Extractor" -> "jct.projects.bouncycryptool.projects.jctPlatformExtractor.api"
  )
//  val valsForConsole: Map[String, String] = Map(
//    "jct" -> "bctLayout.eval"
//  )


  val extraSettings:Seq[Def.Setting[_]] = Seq(
    bctTargetPlatformDependency := bctLayout.value.projects.bouncycryptool.projects.jctPlatformExtractor.bct_api_sbt_internal.dependency
  )

  def bctSettings: Seq[Def.Setting[_]] = Seq(
    bctValsInConsole := valsForConsole,
    bctMainDirectory := (baseDirectory in ThisBuild).value,
    bctLayout := JCTLayout(bctMainDirectory.value).validatedOrBail,
    initialCommands in consoleProject +=
      ConsoleProjectUtils.initialCommands("jct.projects.bouncycryptool.files.initializationBanner", "jct.projects.bouncycryptool.files.welcomeContent", valsForConsole, 500)

  ) ++ extraSettings

  override def projectSettings: Seq[Def.Setting[_]] = bctSettings ++ ConsoleProjectUtils.addSettings

}