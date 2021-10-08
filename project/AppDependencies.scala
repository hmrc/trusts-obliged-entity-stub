import play.core.PlayVersion.current
import sbt._

object AppDependencies {

  val compile: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"    %% "bootstrap-backend-play-28" % "5.9.0",
    "com.github.fge"  % "json-schema-validator"     % "2.2.6"
  )

  val test = Seq(
    "org.scalatest"           %% "scalatest"          % "3.2.8" % "test",
    "com.typesafe.play"       %% "play-test"          % current % "test",
    "org.pegdown"             %  "pegdown"            % "1.6.0" % "test, it",
    "org.scalatestplus.play"  %% "scalatestplus-play" % "5.0.0" % "test, it",
    "com.vladsch.flexmark" %  "flexmark-all"          % "0.35.10" % "test, it"
  )

  val akkaVersion = "2.6.14"
  val akkaHttpVersion = "10.2.4"

  val overrides = Seq(
    "com.typesafe.akka" %% "akka-stream_2.12"     % akkaVersion,
    "com.typesafe.akka" %% "akka-protobuf_2.12"   % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j_2.12"      % akkaVersion,
    "com.typesafe.akka" %% "akka-actor_2.12"      % akkaVersion,
    "com.typesafe.akka" %% "akka-http-core_2.12"  % akkaHttpVersion
  )
}
