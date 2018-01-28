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
//    val bctValsInConsole = settingKey[Seq[(String, String)]]("imported values into the console")
    val bctMainDirectory = settingKey[File]("Main directory of the bouncyCrypTool project")
    val bctLayout = taskKey[JCTLayout]("layout descriptor of the BouncyCrypTool and JCrypTool projects")
//    val bctTargetPlatformDependency = settingKey[ModuleID]("JCT target platform dependency")
  }

  import autoImport._


  def bctSettings: Seq[Def.Setting[_]] = Seq(
    bctMainDirectory := (baseDirectory in ThisBuild).value,
    bctLayout := JCTLayout(bctMainDirectory.value, Project.extract(state.value)).validatedOrBail
  )

  override def projectSettings: Seq[Def.Setting[_]] = bctSettings ++ ConsoleProjectUtils.addSettings

}