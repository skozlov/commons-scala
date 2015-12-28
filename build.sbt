name := "commons-scala"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

libraryDependencies ++= List(
	"org.scalatest" %% "scalatest" % "2.2.4" % "test",
	"io.reactivex" % "rxscala_2.11" % "0.25.0"
)