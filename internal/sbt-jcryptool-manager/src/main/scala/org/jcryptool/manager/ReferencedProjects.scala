package org.jcryptool.manager

import de.simlei.multimanager.projects.flavor.SBTProject
import org.jcryptool.structure.JCTLayout
import sbt._
import scala.sys.process.Process

case class ReferencedProject(proj: SBTProject) {

  def quoteIfNecessary(s: String) = if(s.contains(" ")) "\""+s+"\"" else s
  def cmdSeqToString(cmd: String, more: Seq[String]) = if(more.size == 0) {
    quoteIfNecessary(cmd)
  } else {
    "\"" + (cmd +: more).mkString("; ", "; ", "") + "\""
  }

  def remoteCallCommand(cmdId: String, remoteCmd: String, logLevel: Level.Value = Level.Warn): Command = {
    Command.command(cmdId) { state =>
      //  IO.delete(proj2File / "project" / "target" :: proj2File / "project" / "project" / "target" :: Nil)
      println(s"running command '$remoteCmd' on project $proj")
      val exitcode = Process("sbt" :: s"--${logLevel.toString}" :: quoteIfNecessary(remoteCmd) :: Nil, proj.dir).!
      if(exitcode != 0) {
        println(s"command on project $proj: $remoteCmd did not succeed")
        state.fail
      } else {
        state
      }
    }
  }
}
object ReferencedProject {
  implicit def toRef(sbtProject: SBTProject): ReferencedProject = ReferencedProject(sbtProject)
}

object layout {
  val jct = new JCTLayout(file("./..").getAbsoluteFile)
}

class BuildRealm(bctBase: File) {

  val jctLayout = new JCTLayout(bctBase)

  object fs {
    val platform = file("./../internal/org.jcryptool.bouncycryptool.platform")
    val managerPlugin = file("./../internal/sbt-jcryptool-manager")
  }

  object ref {
    val managerPlugin = ProjectRef(fs.managerPlugin, "sbt-jcryptool-manager")
    val platform = ProjectRef(fs.platform, "sbt-jcryptool-manager")
  }

  object dep {
    val managerPlugin = addSbtPlugin("org.jcryptool" % "sbt-jcryptool-manager" % "0.1-SNAPSHOT")
    val platform = "org.jcryptool" %% "bouncycryptool-platform" % "1.0.0-SNAPSHOT" classifier("assembly")
  }

  object platformInterface {
    val testCmd = "test-platform"
    val localPublishCmd = "publish-jct-local-maven"
    val globalPublishCmd = "publish-jct-global-maven"
  }


}