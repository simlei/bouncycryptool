import scala.collection.mutable
//LATER: modularize, use actual tools platform

def quoted(s: String) = "\"" + s + "\""

def tagsStr(tt: String*): String = {
  assert(tt.size > 0)
  return quoted(tt.mkString(" "))
}

def withoutDecl(tt: String*) = s"-l ${tagsStr(tt: _*)}"
def withDecl(tt: String*) = s"-n ${tagsStr(tt: _*)}"

def testWithoutCmd(pattern: String, firstTag: String, addTags: String*) =
  s";testOnly $pattern -- ${withoutDecl((firstTag +: addTags): _*)} "

def testOnlyCmd(pattern: String, firstTag: String, addTags: String*) =
  s";testOnly $pattern -- ${withDecl((firstTag +: addTags): _*)} "

def testOnlyCmd(pattern: String): String = s";testOnly $pattern "

val tagUI = "SWTInteractive"
val tagAmmIO = "AmmInteractive"

// === TESTS

val cmdTestsPure = testWithoutCmd("*", tagUI, tagAmmIO)
val cmdTestsNoAmm = testWithoutCmd("*", tagUI, tagAmmIO)

// === OTHERS

val cmdCleanAll = "; clean ; test:clean"
val cmdPublishToJCT = "; compile; publishM2; inJCTConsole api_bctPlugin.updatePlugin()"
val cmdFullrun = cmdCleanAll + cmdPublishToJCT

val aliases =
  Map(
    "testAll" -> testOnlyCmd("*"),
    "testPure" -> testWithoutCmd("*", tagUI, tagAmmIO),
    "testNoAmm" -> testWithoutCmd("*", tagAmmIO),
    "testAmm" -> testOnlyCmd("*", tagAmmIO),
    "testUI" -> testOnlyCmd("*", tagUI)
  ) ++
    Map(
      "cleanAll" -> cmdCleanAll,
      "publishToJCT" -> ("; compile; " + cmdPublishToJCT),
      "fullrun" -> (cmdCleanAll + cmdTestsPure + cmdPublishToJCT),
      "fullrun_interactive" -> (cmdCleanAll + testOnlyCmd("*") + cmdPublishToJCT), // LATER: UI tests currently not possible, kills sbt...
      "fullrun_sbtinteractive" -> (cmdCleanAll + testWithoutCmd("*", tagAmmIO) + cmdPublishToJCT) // LATER: UI tests currently not possible, kills sbt...

    )

aliases.toSeq.flatMap { case (x, y) => addCommandAlias(x, y) }

