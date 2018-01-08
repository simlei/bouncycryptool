package org.jcryptool.projectapi

import org.jcryptool.consolehelp.Help

object APIModel {

  trait SignatureSpec{def cmdName: String; def args: Seq[ArgSpec];
    override def toString: String = s"cmdName(${args.mkString(", ")})"
  }
  case class Signature(cmdName: String, args: Seq[ArgSpec]) extends SignatureSpec
  object Signature {
    def of(cmdName: String, args: ArgSpec*) = Signature(cmdName, args)
  }

  trait ArgSpec{def name: String; def tpe: String;
    def description: String = name
    final def nameTypeString: String = s"$name: $tpe"
  }
  case class Arg(name: String, tpe: String) extends ArgSpec

  trait API_Command {
    def cmdName: String
    def signatures: Map[Signature, String]
  }

  object helpAbstractions {

    // unifies the description of an API command which already provides the command signatures
    def consoleCmdHelp[CmdType <: API_Command](apicmd: CmdType, headerDescription: String, afterSignatures: String): Help[CmdType] = {
      val sigsWithNumbers = apicmd.signatures.toSeq.zip(1 to apicmd.signatures.size+1)
      val signatureParts = for{((signature, sigDescr), number) <- sigsWithNumbers}
        yield s"""$number) $signature:
                 |  -> $sigDescr
                 |""".stripMargin.stripLineEnd

      val signaturePart = signatureParts.mkString("\n")
      val fulltext = s"""
                        |$headerDescription
                        |
       |Signatures:
                        |$signaturePart
                        |
       |$afterSignatures
     """.stripMargin.stripLineEnd

      return Help(apicmd, fulltext, Some(signaturePart))
    }

  }
}
