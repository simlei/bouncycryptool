package scalabc.tools

//TODO: get TrySeq in here!

//import scala.util.{Try, Success, Failure}
//
//case class trySeq[R](run : Try[Seq[R]]) {
//  def map[B](f : R => B): trySeq[B] = trySeq(run map { _ map f })
//  def flatMap[B](f : R => trySeq[B]): trySeq[B] = trySeq {
//    run match {
//      case Success(s) => trySeq.sequence(s map f map { _.run }).map { _.flatten }
//      case Failure(e) => Failure(e)
//    }
//  }
//
//}
//
//object trySeq {
//  def withSeq[R](run : Seq[R]): trySeq[R] = new trySeq(Try { run })
//  def withTry[R](run : Try[R]): trySeq[R] = new trySeq(run map (_ :: Nil))
//
//  def tryOp[R](f: R => Try[R]) : (R => trySeq[R]) = (r => trySeq.withTry(f(r)))
//  def seqOp[R](f: R => Seq[R]) : (R => trySeq[R]) = (r => trySeq.withSeq(f(r)))
//
//  def flatThen[R](f1: R => Try[R], f2: R=> Try[R]): R => Try[R] = f1.andThen(_.flatMap(f2))
//
//  def sequence[R](seq : Seq[Try[R]]): Try[Seq[R]] = {
//    seq match {
//      case Success(h) :: tail =>
//        tail.foldLeft(Try(h :: Nil)) {
//          case (Success(acc), Success(elem)) => Success(elem :: acc)
//          case (e : Failure[R], _) => e
//          case (_, Failure(e)) => Failure(e)
//        }
//      case Failure(e) :: _  => Failure(e)
//      case Nil => Try { Nil }
//    }
//  }
//
//  implicit def toTrySeqT[R](run : Try[Seq[R]]) = trySeq(run)
//  implicit def fromTrySeqT[R](trySeqT : trySeq[R]) = trySeqT.run
//}
//
//implicit class FctOps[R](f1: R => Try[R]) {
//  def flatThen(f2: R=> Try[R]): R => Try[R] = trySeq.flatThen(f1, f2)
//}
//
//
//implicit class LogOps(f1: ProjAction) {
//
//  def composeMsg(msgF: Proj=>String): Proj => Try[Proj] = {
//    val sideeffectStart: Proj => Proj = p => {
//      println(msgF(p))
//      p
//    }
//    f1.compose(sideeffectStart)
//  }
//  def announceMessage(actionName: String)(p: Proj) = s"\n!! performing ${actionName} on project ${p.loc.name} ...\n"
//  def withAnnounce(actionName: String = f1.name) = {
//    composeMsg(announceMessage(actionName))
//  }
//  def just() = f1.fct
//}