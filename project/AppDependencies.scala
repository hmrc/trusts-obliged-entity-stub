import sbt._

object AppDependencies {

  val bootstrapVersion = "7.19.0"

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"    %% "bootstrap-backend-play-28" % bootstrapVersion,
    "com.github.fge"  % "json-schema-validator"     % "2.2.14"
  )

  val test = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-28"   % bootstrapVersion,
    "org.mockito"             % "mockito-core"              % "5.4.0",
    "org.scalatest"           %% "scalatest"                % "3.2.16",
    "com.vladsch.flexmark"    %  "flexmark-all"             % "0.64.8"
  ).map(_ % Test)

}
