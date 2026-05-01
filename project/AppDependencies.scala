import sbt.*

object AppDependencies {

  private val bootstrapVersion = "10.7.0"

  private val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"  %% "bootstrap-backend-play-30" % bootstrapVersion,
    "com.networknt" % "json-schema-validator"     % "2.0.1" exclude ("com.fasterxml.jackson.core", "jackson-databind")
  )

  private val test: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapVersion
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ test

}
