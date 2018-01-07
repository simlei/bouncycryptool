package de.simlei.multimanager.projects

import de.simlei.multimanager.misc.fs.{DirPresence, FSPresence, FilePresence}
import de.simlei.multimanager.git.{GitEnvironment, GitRepo}
import de.simlei.multimanager.misc.fs.{DirPresence, FilePresence}
import org.jcryptool.structure._
import sbt._

package object flavor {

  trait MavenProject extends Proj {
    def pomFile: File = dir / "pom.xml"
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