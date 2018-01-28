import org.jcryptool.bouncycastle.core.algorithms.AlgorithmReg
import org.jcryptool.bouncycastle.core.operation.digest.{DigestOperation, DigestSpec}
import org.scalatest.FlatSpec

import scala.collection.JavaConverters._

class MD5DigestTest extends FlatSpec {

  "The MD5 implementation of the Bouncy Castle Library" should "work to prove we define the specs the right way" in {
    val inputString = "Helloworld"

    val md5Spec: DigestSpec = AlgorithmReg.getInstanceDefault.getSpecs_digest.asScala.filter(_.id_name.toLowerCase.contains("md5")).head
    val pretty: String = runDigestOn(md5Spec, inputString)

    println(s"'$inputString' --md5--> '$pretty'")

  }

  "All digests known to this project" should "be able to digest 'Helloworld' as toy aggregation test" in {
    val inputString = "Helloworld"

    for(spec <- AlgorithmReg.getInstanceDefault.getSpecs_digest.asScala) {
      val digestId = spec.id_name
      val pretty: String = runDigestOn(spec, inputString)
      println(s"'$inputString' --$digestId--> '$pretty'")
    }

  }

  private def runDigestOn(spec: DigestSpec, inputString: String): String = {
    val md5Operation: DigestOperation = new DigestOperation(spec)
    val digested: Array[Byte] = md5Operation.executeOperation(inputString.getBytes)
    val pretty = javax.xml.bind.DatatypeConverter.printHexBinary(digested)
    pretty
  }
}
