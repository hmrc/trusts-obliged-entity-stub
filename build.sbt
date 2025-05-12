ThisBuild / scalaVersion := "2.13.16"
ThisBuild / majorVersion := 0

lazy val microservice = Project("trusts-obliged-entity-stub", file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(CodeCoverageSettings.settings)
  .settings(
    PlayKeys.playDefaultPort := 9851,
    libraryDependencies ++= AppDependencies(),
    scalacOptions ++= Seq(
      "-feature",
      "-Wconf:src=routes/.*:s"
    )
  )

addCommandAlias("scalastyleAll", "all scalastyle Test/scalastyle")
