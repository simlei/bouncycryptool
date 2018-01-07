package org.jcryptool.structure

import sbt._
import de.simlei.multimanager.misc.fs._
import de.simlei.multimanager.projects._
import de.simlei.multimanager.git._
import de.simlei.multimanager.projects._
import flavor._
import org.jcryptool.structure.p2._
import de.simlei.multimanager.projects.flavor.SBTProject
import org.jcryptool.projectapi.{ConnectorJCTPluginAPIImpl, JCTCoreTychoBuildAPIImpl, TargetplatformToMavenAPI, _}
import de.simlei.multimanager.misc.fs._


/*
Models the JCrypTool core repository
 */
case class JCTCoreRepo(dir: File) extends JCTMainProj with JCTCoreTychoBuildAPIImpl {

  def api: JCTCoreTychoBuildAPI = this

  override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(relengProject, productProject) //TODO: include, bouncycryptoolPlugin)

  object relengProject extends JCTCoreOrdinarySubProject with MavenProject {
    override def subfolderName: String = "org.jcryptool.releng"
  }
  object productProject extends JCTCoreOrdinarySubProject with MavenProject {
    override def subfolderName: String = "org.jcryptool.product"
  }
  object bouncycryptoolPlugin extends ScalaToJCTConnectorPlugin with JCTCoreOrdinarySubProject with ConnectorJCTPluginAPIImpl {
    def api: ConnectorJCTPluginAPI = this
    override def subfolderName: String = "org.jcryptool.bouncycryptool.plugin"
    override def projectName: String = "bouncycryptool-plugin"
  }

  trait JCTCoreOrdinarySubProject extends JCTOrdinarySubProject {
    override def jctMetaProj: JCTCoreRepo = JCTCoreRepo.this
  }
}

/*
Models the JCrypTool crypto repository
 */
case class JCTCryptoRepo(dir: File) extends JCTMainProj {
  // TODO: implement something snappy here? orient on JCTCoreRepo API structure
  // def classicCryptoFeature = detectSubproject("org.jcryptool.crypto.classic.feature").get
}

/*
Models the JCrypTool BouncyCryptool repository
 */
case class BouncyCryptoolRepo(dir: File, jctLayout: JCTLayout) extends JCTMainProj {
  import BouncyCryptoolRepo._

  override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(
    managerPlugin, targetManagerPlugin, ivyPlatformProject
  )

//  override def fsEntries: Seq[FSPresence] = Seq()

  object managerPlugin extends BCTSubProject with SBTProject {
    override def subPath: String = "internal/sbt-jcryptool-manager"
    override def projectName: String = "sbt-jcryptool-manager"
  }
  object targetManagerPlugin extends BCTSubProject with SBTProject {
    override def subPath: String = "internal/sbt-jcryptool-target-manager"
    override def projectName: String = "sbt-jcryptool-target-manager"
  }
  object ivyPlatformProject extends TargetplatformProject with BCTSubProject {
    override def subPath: String = "internal/org.jcryptool.bouncycryptool.platform"
    override def projectName: String = "bouncycryptool-platform"
  }

  trait BCTSubProject extends JCTSubProject with SBTProject {
    override def jctMetaProj: BouncyCryptoolRepo = BouncyCryptoolRepo.this
  }
}
object BouncyCryptoolRepo {
  trait TargetplatformProject extends JCTSubProject with SBTProject with TargetplatformToMavenAPIImpl {
    def api: TargetplatformToMavenAPI = this
  }
}


case class JCTLayout(bouncyCrypToolHome: File) extends FSEnsemble with JCT_API_Implementation {

  override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(
    bouncycryptool,
    core,
    crypto,
    internal.bannerMessageFile: FilePresence,
    internal.welcomeMessageFile: FilePresence
  )

  def bouncycryptool: BouncyCryptoolRepo = new BouncyCryptoolRepo(bouncyCrypToolHome, this)
  def core: JCTCoreRepo = specs.core.findPresenceOrBail()
  def crypto: JCTCryptoRepo = specs.crypto.findPresenceOrBail()

  def gitClonedUpdatesite: Option[GithubPagesUpdateSiteClone] = specs.githubPagesUpdatesiteClone.optionalVerifiedPresence()

  object snapshotUpdateSite extends GithubPagesUpdateSite {
    val githubOwner = "simlei" // TODO: will be moved to the JCrypTool organization in time
    val repoName = "jcryptool-p2"
  }

  object specs {
    val core: SubpathSpec[JCTCoreRepo] = SubpathSpec(
      searchRoot = bouncyCrypToolHome.getParentFile,
      subpath = "core",
      presenceConstructor = JCTCoreRepo(_)
    )
    val crypto: SubpathSpec[JCTCryptoRepo] = SubpathSpec(
      searchRoot = bouncyCrypToolHome.getParentFile,
      subpath = "crypto",
      presenceConstructor = JCTCryptoRepo(_)
    )
    val githubPagesUpdatesiteClone: ChildPatternSpec[GithubPagesUpdateSiteClone] = ChildPatternSpec(
      searchRoot = bouncyCrypToolHome.getParentFile,
      childPattern = "*jcryptool-p2*",
      presenceConstructor = { cloneDir =>
        GithubPagesUpdateSiteClone( GitRepo(cloneDir), snapshotUpdateSite)
      }
    )

  }

  object internal {
    val welcomeMessageFile = bouncycryptool.dir / "internal" / "welcomeToREPL.txt"
    val welcomeContent = IO.read(welcomeMessageFile)

    val bannerMessageFile = bouncycryptool.dir / "internal" / "initializationBanner.txt"
    val initializationBanner = IO.read(bannerMessageFile)
  }

}


