package org.jcryptool.projectapi

import de.simlei.multimanager.Utils
import de.simlei.multimanager.misc.fs.{DirPresence, FSEnsembleDir, FSPresence}
import de.simlei.multimanager.misc.fs._
import org.jcryptool.projectapi.APIModel.{API_Command, API_Spec, Arg, Signature}
import org.jcryptool.structure._
import org.jcryptool.structure.p2.LocalUpdateSite
import sbt._

import scala.sys.process._
import scala.sys.process.Process
import scala.util.Try

trait JCTCoreTychoBuildAPI extends API_Spec {
  import JCTCoreTychoBuildAPI._

  override def allCommands = Seq("buildJCT"->buildJCT)
  override def allSubAPIs = Seq()

  val outputs: TychoOutputs
  def buildJCT: LocalP2BuildCmd
}
object JCTCoreTychoBuildAPI {

  trait TychoOutputs {
    def default_build_dir: File
    def built_product: JCTBuiltProduct
//  def build_p2_repo: LocalUpdateSite = built_product.localUpdateSite
  }

  abstract class LocalP2BuildCmd extends API_Command {
    override def cmdName: String = "buildJCT"
    override def signatures: Map[APIModel.Signature, String] = Map(
      Signature(cmdName, Seq()) -> "builds the product to the default directory",
      Signature(cmdName, Seq(Arg("targetDir", "File"))) -> "builds the product to the specified directory"
    )
    def apply(): JCTBuiltProduct = apply(defaultDirectory)

    def defaultDirectory: File
    def apply(targetDir: File): JCTBuiltProduct
  }

  case class JCTBuiltProduct(outputDir: File) extends FSEnsembleDir {
    override def dir = outputDir
    def p2Dir: DirPresence = dir / "repository"
    def localUpdateSite = new LocalUpdateSite(p2Dir)

    override def fsEntries: Seq[FSPresence] = Seq(p2Dir, localUpdateSite)
  }
}


case class JCTCoreTychoBuildAPIImpl(coreRepoProject: JCTCoreRepo) extends JCTCoreTychoBuildAPI {

  val proj: JCTCoreRepo = coreRepoProject
  import JCTCoreTychoBuildAPI._

  override val outputs = new TychoOutputs {
    override def default_build_dir = proj.projects.product.dir / "target"
    override def built_product: JCTBuiltProduct = new JCTBuiltProduct(default_build_dir)
  }

  override def buildJCT: LocalP2BuildCmd = new LocalP2BuildCmd {
    override def apply(targetDir: sbt.File) = {
      if(targetDir != defaultDirectory && targetDir.exists()) {
        sys.error(Utils.makeExceptionMessage(s"target directory ($targetDir) may not yet exist"))
      }
//      val result = proj.projects.releng.api_mvn.call(Seq("clean", "package"))
//      if(result.isFailure) {
//        println("Maybe you have compilation errors in your JCT core/crypto code, or your RAM is not sufficient (4GB). Please mail me or open an issue!")
//      }
      val built = JCTBuiltProduct(outputs.default_build_dir)
      println("Successfully built the JCT product!")
      if(targetDir != defaultDirectory) {
        println(s"Transferring the JCT product to ${targetDir}")
        IO.createDirectory(targetDir)
        IO.copyDirectory(built.dir, targetDir)
        JCTBuiltProduct(targetDir)
      } else {
        built
      }
    }
    override def defaultDirectory = outputs.default_build_dir
  }

}
