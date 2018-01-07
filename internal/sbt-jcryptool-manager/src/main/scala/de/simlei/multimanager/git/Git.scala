package de.simlei.multimanager.git

import sbt._
import de.simlei.multimanager.misc.fs.{DirPresence, FSEnsemble, FSPresence}

import scala.collection.GenTraversableOnce

  sealed trait GitEnvironment {
    def repoDir: File
    def gitSystemDir: File = repoDir / ".git"
    def gitPathOf(file: File): Option[String] = Path.relativeTo(repoDir).apply(file)
  }
  object GitEnvironment {
    def isGitDir(dir: File): Boolean = dir.exists() && dir.isDirectory && dir.listFiles().filter(_.isDirectory).filter(_.name == ".git").size > 0
    def noGitDir(dir: File): Boolean = ! isGitDir(dir)
    def gitDir(dir: File): Option[GitEnvironment] = if(isGitDir(dir)) Some(GitRepo(dir)) else None

    def detect(file: File): Option[GitEnvironment] = {
      GitRelation.detect(file).map(_.environment).orElse(gitDir(file))
    }
  }

  sealed trait GitRelation {
    val subject: File
    val environment: GitEnvironment

    def gitManagedPath = environment.gitPathOf(subject)
    def gitRootPath = environment.gitPathOf(subject)
  }
  object GitRelation {
    def rootSearch(currentDir: File): Seq[File] = Option(currentDir.getParentFile).map(p => rootSearch(p)).getOrElse(Seq()) :+ currentDir
    def detect(file: File): Option[GitRelation] = {
      val fsRootPath = rootSearch(file)
      val topmostGit = fsRootPath.reverse.dropWhile(GitEnvironment.noGitDir).headOption
      val bottommostGit = fsRootPath.dropWhile(GitEnvironment.noGitDir).headOption

      if(topmostGit.isEmpty || bottommostGit.isEmpty) {
        return None
      }
      val top = GitRepo(topmostGit.get)
      val bottom = GitRepo(bottommostGit.get)

      if(bottom.repoDir != file) {
        Some(GitManagedFile(file, bottom))
      } else None // repo has no relation to itself

      // submodule case, not considered right now, see commented-out code
//      if(top == bottom) {
//        //see obove
//      } else {
//        if(bottom.repoDir == file) {
//          Some(GitSubmoduleRepo(top, bottom))
//        } else {
//          Some(GitManagedFile(file, GitSubmoduleRepo(top, bottom)))
//        }
//      }
    }
  }

  case class GitRepo(repoDir: File) extends GitEnvironment {
    def presence = new FSEnsemble {
      override def fsEntries: Seq[FSPresence] = Seq(repoDir: DirPresence, gitSystemDir: DirPresence)
    }

  }

  case class GitManagedFile(subject: File, environment: GitEnvironment) extends GitRelation
//  case class GitSubmoduleRepo(repo: GitRepo, rootRepo: GitRepo) extends GitEnvironment with GitRelation {
//    override def repoDir = repo.repoDir
//    override val environment: GitRepo = rootRepo
//    override val subject: File = repo.repoDir
//
//    def pathFromRootOf(file: File): Option[String] = Path.relativeTo(rootRepo.repoDir).apply(file)
//  }
