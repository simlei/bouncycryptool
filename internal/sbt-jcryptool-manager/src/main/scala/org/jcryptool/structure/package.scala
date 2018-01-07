package org.jcryptool

import sbt._
import de.simlei.multimanager.git.{GitEnvironment, GitRepo}
import de.simlei.multimanager.projects.Proj
import de.simlei.multimanager.projects.flavor.{EclipsePluginProject, GitVCSProject, SBTProject}

package object structure {
  trait JCTProj extends Proj with GitVCSProject //TODO: differentiate between BCT and JCT

  trait JCTMainProj extends JCTProj {
    final def git: GitEnvironment = GitRepo(dir)
    def detectSubproject(id: String): Option[JCTSubProject] = None // TODO: later: each meta project may override this for project resolution by id
  }

  trait JCTSubProject extends JCTProj {
    def jctMetaProj: JCTMainProj
    def subPath: String

    override def git = jctMetaProj.git
    override final def dir = jctMetaProj.dir / subPath
  }

  trait JCTOrdinarySubProject extends JCTSubProject {
    def subfolderName: String
    override final def subPath = subfolderName
  }

  trait ScalaToJCTConnectorPlugin extends JCTProj with JCTOrdinarySubProject with EclipsePluginProject with SBTProject

}