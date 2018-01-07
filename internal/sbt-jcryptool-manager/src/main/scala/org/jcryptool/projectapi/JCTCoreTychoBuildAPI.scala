package org.jcryptool.projectapi

import de.simlei.multimanager.misc.fs.{DirPresence, FSEnsembleDir, FSPresence}
import de.simlei.multimanager.misc.fs._
import org.jcryptool.structure._
import org.jcryptool.structure.p2.LocalUpdateSite
import sbt._
import scala.sys.process._
import scala.sys.process.Process

import scala.util.Try

trait JCTCoreTychoBuildAPI {
  import JCTCoreTychoBuildAPI._

  def performBuild: Try[JCTBuiltProduct]
  def performBuildOfUpdateSite: Try[LocalUpdateSite] = {
    //TODO: this just builds the whole product and relegates to the generated repository subfolder
    performBuild.map(_.localUpdateSite)
  }
}
object JCTCoreTychoBuildAPI {
  case class JCTBuiltProduct(outputDir: File) extends FSEnsembleDir {
    override def dir = outputDir
    def repoDir: DirPresence = dir / "repository"
    def localUpdateSite = new LocalUpdateSite(repoDir)

    override def fsEntries: Seq[FSPresence] = Seq(repoDir, localUpdateSite)
  }
}


trait JCTCoreTychoBuildAPIImpl extends JCTCoreTychoBuildAPI { self: JCTCoreRepo =>
  import JCTCoreTychoBuildAPI._

  def targetDir = productProject.dir / "target"
  def product: JCTBuiltProduct = new JCTBuiltProduct(targetDir)

  val lastBuildProduct: Option[JCTBuiltProduct] = product.asOption
  val lastUpdateSite: Option[LocalUpdateSite] = lastBuildProduct.map(_.localUpdateSite)

  override def performBuild: Try[JCTBuiltProduct] = Try {
    val output = Process("mvn package", relengProject.dir).!
    if(output == 0) {
      JCTBuiltProduct(targetDir)
    } else {
      sys.error("A problem occurred with the Maven build. See console output.")
    }
  }
}
