package scalabc.ui.test

import org.scalatest._
import org.scalatest.Tag._
import org.scalatest.FlatSpec
import scalabc.tools.Tags
import collection.mutable.ListBuffer

trait Builder extends TestSuiteMixin { this: TestSuite =>

  val builder = new StringBuilder

  abstract override def withFixture(test: NoArgTest) = {
    builder.append("ScalaTest is ")
    try super.withFixture(test) // To be stackable, must call super.withFixture
    finally builder.clear()
  }
}

trait Buffer extends TestSuiteMixin { this: TestSuite =>

  val buffer = new ListBuffer[String]

  abstract override def withFixture(test: NoArgTest) = {
    try super.withFixture(test) // To be stackable, must call super.withFixture
    finally buffer.clear()
  }
}

class MixinSpec extends FlatSpec with Builder with Buffer {

  "Testing" should "be easy" ignore {
    builder.append("easy!")
    assert(builder.toString === "ScalaTest is easy!")
    assert(buffer.isEmpty)
    buffer += "sweet"
  }

  it should "be fun" ignore {
    builder.append("fun!")
    assert(builder.toString === "ScalaTest is fun!")
    assert(buffer.isEmpty)
    buffer += "clear"
  }
}