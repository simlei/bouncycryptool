package scalabc.ui.test

import ammonite.runtime

import org.scalatest.Tag._
import scalabc.tools.Tags
import org.scalatest.FlatSpec

class AmmoniteUISpec extends FlatSpec {

  "The Ammonite REPL" should "run in-memory with stdIO" taggedAs (Tags.AmmInteractive) in {
//    val storage = new runtime.Storage.InMemory() // TODO: reenable test when fixed on Windows machines
//    ammonite.Main(storageBackend = storage).run()
    println("This would instantiate an Ammonite REPL in the testbed and loop until the tester exits it manually.")
    println("It works on Unix systems, but not on windows, so we disable it for now.")
  }

}