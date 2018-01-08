package org.jcryptool.projectapi

import org.jcryptool.consolehelp.Help

object APIModel {

  trait API_Spec {
    def allCommands: Seq[(String, API_Command)]
    def allSubAPIs: Seq[(String, API_Spec)]
  }

  trait SignatureSpec{def cmdName: String; def args: Seq[ArgSpec];
    override def toString: String = s"$cmdName(${args.mkString(", ")})"
  }
  case class Signature(cmdName: String, args: Seq[ArgSpec]) extends SignatureSpec
  object Signature {
    def of(cmdName: String, args: ArgSpec*) = Signature(cmdName, args)
  }

  trait ArgSpec{def name: String; def tpe: String;
    def description: String = name
    override def toString: String = s"$name: $tpe"
  }
  case class Arg(name: String, tpe: String) extends ArgSpec

  trait API_Command {
    def cmdName: String
    def signatures: Map[Signature, String]
  }

  object helpAbstractions {
    def apiHelp[API_T <: API_Spec](api: API_T, short: String, long: String): Help[API_T] = {
      def apiCommandsList = api.allCommands.map { case (id: String, command: API_Command) => id}
      def subApiList = api.allSubAPIs.map { case (id: String, sub: API_Spec) => id}

      Help(api,
        s"""$short
           |
           |List of API commands: ${apiCommandsList.mkString(", ")}
           |List of sub-APIs: ${subApiList.mkString(", ")}
           |
           |$long
         """.stripMargin.stripLineEnd
      )

    }


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
