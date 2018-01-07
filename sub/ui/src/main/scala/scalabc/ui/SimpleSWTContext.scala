package scalabc.ui

import ammonite.runtime
import io.reactivex._

import collection.JavaConverters._

//TODO: test one last time, then retire

object rxapp extends App {
  val exitor = Completable.fromAction(() => println("Helloworld!"))
  private val data: Observable[Int] = Observable.just(2, 3, 4, 5)
  data.subscribe(println(_))
}

object ammInMain extends App {
  //  val storage = new runtime.Storage.Folder(ammonite.ops.Path("/home/simon/gitShared/scalabc/ammDebugPredef"))
  val storage = new runtime.Storage.InMemory()
  ammonite.Main(storageBackend = storage).run()
}