package org.jcryptool.structure

import de.simlei.multimanager.misc.fs.{FSEnsemble, FSPresence, FilePresence}
import de.simlei.multimanager.git._
import de.simlei.multimanager.misc.fs._
import org.jcryptool.structure.p2.LocalUpdateSite
import sbt._

package object p2 {
  trait UpdateSite {
    def url: String // may point to a web or local location
  }
  trait GithubPagesUpdateSite extends UpdateSite { // assumes the p2 repository being put into the root directory of the repository
    def githubOwner: String
    val repoName: String
    def url: String = s"http://${githubOwner}.github.io/${repoName}/"
  }

  class LocalUpdateSite(val dir: File) extends UpdateSite with FSEnsemble {
    def indexfile: FilePresence = dir / "p2.index"
    def artifactsjar: FilePresence = dir / "artifacts.jar"

    override def fsEntries: Seq[FSPresence] = Seq(indexfile, artifactsjar)
    override def url: String = s"file://${dir.getAbsoluteFile.getPath}"
  }

  case class GithubPagesUpdateSiteClone(git: GitRepo, page: GithubPagesUpdateSite) extends LocalUpdateSite(git.repoDir) with GithubPagesUpdateSite {

    override def url: String = super[LocalUpdateSite].url
    override def fsEntries: Seq[FSPresence] = super.fsEntries :+ git.presence
    override def githubOwner: String = page.githubOwner
    override val repoName: String = page.repoName
  }
}