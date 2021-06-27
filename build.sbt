lazy val elastic4sVersion = "7.12.3"
lazy val catsVersion = "2.3.1"
lazy val scalaTestVersion = "3.2.2"
lazy val scalaCheckVersion = "1.14.1"
lazy val pureConfigVersion = "0.15.0"
lazy val logbackVersion = "1.2.3"
lazy val scalaLoggingVersion = "3.9.2"
lazy val sttpVersion = "3.3.7"
lazy val http4sVersion = "0.21.23"
lazy val circeVersion = "0.14.1"

lazy val catsDependencies = Seq(
  "org.typelevel" %% "cats-core" % catsVersion,
  "org.typelevel" %% "cats-effect" % catsVersion,
  "org.typelevel" %% "cats-effect-laws" % catsVersion % "test"
)

lazy val elastic4sDependencies = Seq(
  "com.sksamuel.elastic4s" %% "elastic4s-core" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-client-esjava" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-effect-cats" % elastic4sVersion,
  "com.sksamuel.elastic4s" %% "elastic4s-json-circe" % elastic4sVersion
)

lazy val scalaTestDependencies = Seq(
  "org.scalactic" %% "scalactic" % scalaTestVersion,
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)

lazy val scalaCheckDependencies = Seq(
  "org.scalacheck" %% "scalacheck" % scalaCheckVersion % "test"
)

lazy val pureConfigDependencies = Seq(
  "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
)

lazy val loggerDependencies = Seq(
  "ch.qos.logback" % "logback-classic" % logbackVersion,
  "ch.qos.logback" % "logback-core" % logbackVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
)

lazy val circeDependencies = Seq(
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion
)

lazy val root = (project in file("."))
  .settings(
    name := "elastic4s-skeleton",
    organization := "com.dkarwacki",
    version := "0.1",
    scalaVersion := "2.13.6",
    scalacOptions += "-Ymacro-annotations",
    libraryDependencies ++=
      catsDependencies
        ++ elastic4sDependencies
        ++ scalaCheckDependencies
        ++ scalaTestDependencies
        ++ pureConfigDependencies
        ++ loggerDependencies
        ++ circeDependencies
  )
