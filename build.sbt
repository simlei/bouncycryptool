inThisBuild(Settings.settingsBuild)

inScope(ThisScope.copy(
  project = Zero,
  config = Zero,
  task = Select(run.key)))(Settings.interactiveExecutionSettings)

inScope(ThisScope.copy(
  project = Zero,
  config = Select(Test),
  task = Zero))(
  Settings.interactiveExecutionSettings ++ Seq(
    logBuffered := false
  ))

lazy val connector = (project in file("sub/connector"))
  .settings(Settings.connectorSettings: _*)
  .dependsOn(ui, crypto, logic, tools)

lazy val ui = (project in file("sub/ui"))
  .settings(Settings.uiSettings: _*)
  .dependsOn(crypto, logic, tools)

lazy val crypto = (project in file("sub/crypto"))
  .settings(Settings.cryptoSettings: _*)
  .dependsOn(logic, tools)

lazy val logic = (project in file("sub/logic"))
  .settings(Settings.logicSettings: _*)
  .dependsOn(tools)

lazy val tools = (project in file("sub/tools"))
  .settings(Settings.toolsSettings: _*)

lazy val `bouncycryptool` = (project in file("."))
  .settings(Settings.rootSettings: _*)
//  .settings(addArtifact(artifact in (Compile, assembly), assembly).settings: _*)
  .dependsOn(logic, crypto, ui, connector, tools)
  .aggregate(logic, crypto, ui, connector, tools)
  .enablePlugins(JCrypToolManager)