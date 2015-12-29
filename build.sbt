organization := "com.github.skozlov"

name := "commons-scala"

version := "0.1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= List(
	"org.scalatest" %% "scalatest" % "2.2.4" % "test",
	"io.reactivex" % "rxscala_2.11" % "0.25.0"
)

publishMavenStyle := true

publishTo := {
	val nexus = "https://oss.sonatype.org/"
	if (isSnapshot.value)
		Some("snapshots" at nexus + "content/repositories/snapshots")
	else
		Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomExtra := <url>https://github.com/skozlov/commons-scala</url>
	<licenses>
		<license>
			<name>Apache License, Version 2.0</name>
			<url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
			<distribution>repo</distribution>
		</license>
	</licenses>
	<scm>
		<url>git@github.com:skozlov/commons-scala.git</url>
		<connection>scm:git:git@github.com:skozlov/commons-scala.git</connection>
	</scm>
	<developers>
		<developer>
			<id>skozlov</id>
			<name>Sergey Kozlov</name>
			<url>https://github.com/skozlov</url>
		</developer>
	</developers>