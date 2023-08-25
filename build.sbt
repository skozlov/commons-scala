ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val core = (project in file("core"))
  .settings(
    name := "commons-scala-core",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.2.16" % Test
    )
  )

lazy val root = (project in file("."))
  .settings(name := "commons-scala-root")
  .aggregate(
    core,
  )
