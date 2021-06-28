name := "dependency-manager"

version := "1.0.0"

lazy val root = (project in file(".")).settings(PlayKeys.playDefaultPort := 8080).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(guice,
  "org.jgrapht" % "jgrapht-io" % "1.4.0"
)