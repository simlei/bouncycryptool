package scalabc.ui.test

import ammonite.runtime

import org.scalatest.Tag._
import org.scalatest.FlatSpec
import scalabc.tools.Tags

class AmmoniteUISpec extends FlatSpec {

  "The Ammonite REPL" should "run in-memory with stdIO" taggedAs (Tags.AmmInteractive) in {
    val storage = new runtime.Storage.InMemory()
    ammonite.Main(storageBackend = storage).run()
  }

}