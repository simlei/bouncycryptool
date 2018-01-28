import org.scalatest.FlatSpec

class LogicTests extends FlatSpec {

  "A Scala Abstract Datatype structure " should "behave orderly" in {
    trait TA
    trait TAB extends TA
    trait TAC extends TA
    trait TB
    trait TBX extends TB
    trait TBY extends TB
    case class CAB(ta: TA, i: Int)
    case class CAC(tbx: TBX, i: Int)
    case class CBX(tab: TAB, s: String)
    case class CBY(s: String)
  }

  it should "produce NoSuchElementException when 'head' is invoked" in {
    assertThrows[NoSuchElementException] {
      Set.empty.head
    }
  }
}
