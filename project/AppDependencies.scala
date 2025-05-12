import sbt.*

object AppDependencies {

  private val bootstrapVersion = "9.11.0"

  private val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-backend-play-30"  % bootstrapVersion,
    "com.github.fge"          %  "json-schema-validator"      % "2.2.14"
  )

  private val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30"   % bootstrapVersion,
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test

}
