package de.simlei.multimanager.projects

import de.simlei.multimanager.Utils
import de.simlei.multimanager.misc.fs.{DirPresence, FSPresence, FilePresence}
import de.simlei.multimanager.git.{GitEnvironment, GitRepo}
import de.simlei.multimanager.misc.fs.{DirPresence, FilePresence}
import de.simlei.multimanager.projects.flavor.SBTProject.API
import org.jcryptool.structure._
import sbt._

import scala.sys.process.Process
import scala.util.Try

package object flavor {

  trait MavenProject extends Proj {
    def pomFile: File = dir / "pom.xml"
    def api_mvn = MavenProject.API(this)
  }
  object MavenProject {
    case class API(proj: MavenProject) {

      def call(cmds: Seq[String]): Try[Unit] = Try {
        //  IO.delete(proj2File / "project" / "target" :: proj2File / "project" / "project" / "target" :: Nil)
        println(s"running command 'mvn ${cmds.mkString(" ")}' on project $proj")
        val myProcess = Process(Utils.winPanderProcess("mvn" +: cmds), proj.dir)
        if(myProcess.! != 0) {
          sys.error(s"command on project $proj> 'mvn ${cmds.mkString(" ")}' did not succeed")
        } else {
          println(s"Successfully ran command 'mvn ${cmds.mkString(" ")}' on project $proj")
        }
      }
    }
  }

  trait GitVCSProject extends Proj {
    override def fsEntries: Seq[FSPresence] = super.fsEntries//TODO: reenable git dir checking| :+ (git.gitSystemDir: DirPresence)

    def git: GitEnvironment
  }

  // this trait just means that there is some sort of sbt working behind the curtains, could be anything, not just Scala (e.g. script project)
  trait SBTProject extends Proj {
    override def fsEntries: Seq[FSPresence] = Seq(buildSbt, buildProjectFolder)

    def projectName: String

    def buildSbt: FilePresence = dir / "build.sbt"
    def buildProjectFolder: DirPresence = dir / "project"
    def jarDropinFolder = dir / "lib"

    val bct_api_sbt: API = SBTProject.API(this)

  }
  object SBTProject {
    case class API(proj: SBTProject) {
      private[this] def quoteIfNecessary(s: String) = s //if(s.contains(" ")) "\""+s+"\"" else s
      private[this] def cmdSeqToString(cmd: String, more: Seq[String]) = if(more.size == 0) {
        quoteIfNecessary(cmd)
      } else {
        "\"" + (cmd +: more).mkString("; ", "; ", "") + "\""
      }

      def remoteCall(remoteCmd: String, logLevel: Level.Value = Level.Error): Try[Unit] = Try {
        //  IO.delete(proj2File / "project" / "target" :: proj2File / "project" / "project" / "target" :: Nil)
        val cmdToBeRun = quoteIfNecessary(remoteCmd)
        println(s"running command '$cmdToBeRun' on project $proj")
        val myProcess = Process(Utils.winPanderProcess("sbt" :: s"--${logLevel.toString}" :: cmdToBeRun :: Nil), proj.dir)
//        val process = myProcess.run(true)
//        if(process.exitValue() != 0) {
        if(myProcess.! != 0) {
          sys.error(s"command on project $proj> $remoteCmd did not succeed")
        } else {
          println(println(s"Successfully ran command '$remoteCmd' on project $proj"))
        }
      }
    }
  }

  trait EclipseJavaProject extends Proj {
    def sourceFolder: File = dir / "src"
    def classpathFile: File = dir/ ".classpath"
    def projectFile: File = dir / ".project"
  }

  trait EclipsePluginProject extends EclipseJavaProject {
    def manifestFile: File = dir / "META-INF" / "MANIFEST.MF"
    def pluginXml: File = dir / "plugin.xml"
    def buildproperties: File = dir / "build.properties"
  }

}