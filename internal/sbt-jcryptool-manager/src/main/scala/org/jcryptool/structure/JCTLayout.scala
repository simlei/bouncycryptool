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
import org.jcryptool.structure.JCTCoreRepo.SubProject


/*
Models the JCrypTool core repository
 */
case class JCTCoreRepo(dir: File, jctLayout: JCTLayout) extends JCTMainProj {

  val api_build: JCTCoreTychoBuildAPI = JCTCoreTychoBuildAPIImpl(this)

  override def fsEntries: Seq[FSPresence] = super.fsEntries :+ projects

  object projects extends FSEnsemble {
    override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(releng, product) //TODO: weekend: include, bctConnector)

    object releng extends CoreSubProject with MavenProject {
      override def subfolderName: String = "org.jcryptool.releng"
    }
    object product extends CoreSubProject with MavenProject {
      override def subfolderName: String = "org.jcryptool.product"
    }
    object bctConnector extends ScalaToJCTConnectorPlugin with CoreSubProject {
      def api: ConnectorJCTPluginAPI = ConnectorJCTPluginAPIImpl(this)
      override def subfolderName: String = "org.jcryptool.bouncycryptool.plugin"
      override def projectName: String = "bouncycryptool-plugin"
    }
  }

  trait CoreSubProject extends SubProject{
    override def jctMetaProj = JCTCoreRepo.this
  }
}
object JCTCoreRepo {
  trait SubProject extends JCTOrdinarySubProject {
    override def jctMetaProj: JCTCoreRepo
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
case class BouncyCryptoolRepo(dir: File, jctLayout: JCTLayout, extractedState: Extracted) extends JCTMainProj {
  import BouncyCryptoolRepo._

  override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(projects, files)

  object projects extends FSEnsemble {
    override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(jctManager, targetManager, jctPlatformExtractor)

    object jctManager extends BCTSubProject with SBTProject {
      override def subPath: String = "internal/sbt-jcryptool-manager"
      override def projectName: String = "sbt-jcryptool-manager"
    }
    object targetManager extends BCTSubProject with SBTProject {
      override def subPath: String = "internal/sbt-jcryptool-target-manager"
      override def projectName: String = "sbt-jcryptool-target-manager"
    }
    object jctPlatformExtractor extends BCTSubProject {
      val api: TargetplatformToMavenAPI = TargetplatformToMavenAPIImpl(this)
      override def subPath: String = "internal/org.jcryptool.bouncycryptool.platform"
      override def projectName: String = "bouncycryptool-platform"
    }
  }

  object files extends FSEnsemble {
    override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(welcomeMessageFile, bannerMessageFile)

    val welcomeMessageFile: FilePresence = dir / "internal" / "welcomeToREPL.txt"
    val bannerMessageFile: FilePresence = dir / "internal" / "initializationBanner.txt"
    def welcomeContent = IO.read(welcomeMessageFile)
    def initializationBanner = IO.read(bannerMessageFile)
  }
//  override def fsEntries: Seq[FSPresence] = Seq()

  trait BCTSubProject extends JCTSubProject with SBTProject {
    override def jctMetaProj: BouncyCryptoolRepo = BouncyCryptoolRepo.this
  }
}


case class JCTLayout(bouncyCrypToolHome: File, bctExtracted: Extracted) extends FSEnsemble {

  override def fsEntries: Seq[FSPresence] = super.fsEntries :+ projects

  object projects extends FSEnsemble  {
    override def fsEntries: Seq[FSPresence] = super.fsEntries ++ Seq(bouncycryptool,core,crypto)
    val bouncycryptool: BouncyCryptoolRepo = new BouncyCryptoolRepo(bouncyCrypToolHome, JCTLayout.this, bctExtracted)
    val core: JCTCoreRepo = specs.core.findPresenceOrBail()
    val crypto: JCTCryptoRepo = specs.crypto.findPresenceOrBail()
  }

  object snapshotUpdateSite extends GithubPagesUpdateSite {
    val githubOwner = "simlei" // TODO: will be moved to the JCrypTool organization in time
    val repoName = "jcryptool-p2"
  }
  val gitClonedUpdatesite: Option[GithubPagesUpdateSiteClone] = specs.githubPagesUpdatesiteClone.optionalVerifiedPresence()

  object specs {
    val core: ChildPatternSpec[JCTCoreRepo] = ChildPatternSpec(
      searchRoot = bouncyCrypToolHome.getParentFile,
      childPattern = "*core",
      presenceConstructor = JCTCoreRepo(_, JCTLayout.this)
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

  val api: JCT_API = JCT_API_Implementation(this)
}


