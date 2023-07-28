ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.0"

lazy val root = (project in file("."))
  .settings(
    name := "space-traders",
    libraryDependencies += "org.http4s" %% "http4s-blaze-client" % "1.0.0-M38",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.6-0142603",
    libraryDependencies += "org.http4s" %% "http4s-circe" % "1.0.0-M38",
    libraryDependencies += "io.circe" %% "circe-generic" % "0.14.3",
    libraryDependencies += "org.slf4j" % "slf4j-api" % "2.0.7",
    libraryDependencies += "org.slf4j" % "slf4j-simple" % "2.0.7",
    libraryDependencies += "dev.zio" %% "zio-http" % "3.0.0-RC2",
  )
