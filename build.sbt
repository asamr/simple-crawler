import sbt.internal.bsp.BuildTargetTag

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.8"

val Http4sVersion = "0.23.11"
val CirceVersion = "0.14.1"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.10"
val MunitCatsEffectVersion = "1.0.7"
val CatsEffectVersion = "3.3.9"
val scalaScraperVersion = "2.2.1"

lazy val root = (project in file("."))
  .settings(
    name := "crawler",
    libraryDependencies ++= Seq(
      "org.http4s"       %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"       %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"       %% "http4s-circe"        % Http4sVersion,
      "org.http4s"       %% "http4s-dsl"          % Http4sVersion,
      "org.typelevel"    %% "cats-effect"         % CatsEffectVersion,
      "io.circe"         %% "circe-generic"       % CirceVersion,
      "net.ruippeixotog" %% "scala-scraper"       % scalaScraperVersion,
      "org.scalameta"    %% "munit"               % MunitVersion           % Test,
      "org.typelevel"    %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"   %  "logback-classic"     % LogbackVersion         % Runtime,
      "org.scalameta"    %% "svm-subs"            % "20.2.0"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.2" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )
