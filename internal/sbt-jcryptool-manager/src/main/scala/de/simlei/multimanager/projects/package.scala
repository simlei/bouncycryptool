package de.simlei.multimanager

import de.simlei.multimanager.misc.fs.FSEnsembleDir
import sbt._
import de.simlei.multimanager.misc.fs.DirPresence

package object projects {


  trait Proj extends FSEnsembleDir {
    override def dir: File
  }

  trait RemoteProj {
//    def name: String //TODO: give a name to this trait
    def webUrl: String
    def gitUrl: String
  }

}
