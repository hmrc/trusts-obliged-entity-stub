import scoverage.ScoverageKeys

val appName = "trusts-obliged-entity-stub"

lazy val microservice = Project(appName, file("."))
  .enablePlugins(play.sbt.PlayScala, SbtDistributablesPlugin)
  .disablePlugins(JUnitXmlReportPlugin) //Required to prevent https://github.com/scalatest/scalatest/issues/1427
  .settings(
    PlayKeys.playDefaultPort := 9851,
    ScoverageKeys.coverageExcludedFiles := "<empty>;.*config.*;Reverse.*;.*BuildInfo.*;.*Routes.*;.*GuiceInjector;" +
      ".*LanguageSwitchController",
    ScoverageKeys.coverageMinimumStmtTotal := 99,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true
  )
  .settings(
    scalaVersion := "2.13.11",
    majorVersion                     := 0,
    libraryDependencies              ++= AppDependencies.compile ++ AppDependencies.test,
    scalacOptions += "-Wconf:src=routes/.*:s"
  )
  // To resolve a bug with version 2.x.x of the scoverage plugin - https://github.com/sbt/sbt/issues/6997
  // Try to remove when sbt[ 1.8.0+ and scoverage is 2.0.7+
  .settings(libraryDependencySchemes ++= Seq("org.scala-lang.modules" %% "scala-xml" % VersionScheme.Always))
