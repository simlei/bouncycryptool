package scalabc.ui.tests

import org.scalatest._
import org.scalatest.Tag._
import scalabc.tools.Tags

class LegacyTests extends FlatSpec {
  "A simple Durian-powered shell" should "open and play nice on closing" taggedAs (Tags.SWTInteractive) in {
    scalabc.ui.examples.TestApp.main(Array())
  }
}

