name := "Tezos-FullStack-Console-Translation-Module"

version := "0.1"

scalaVersion := "2.12.8"

lazy val circeVersion = "0.12.0-M4"

lazy val circe = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic"
).map(_ % circeVersion)

lazy val catsVersion = "2.0.0-M4"

lazy val cats = Seq(
  "org.typelevel" %% "cats-core" % catsVersion
)

lazy val jacksonVersion = "2.9.9"

lazy val jackson = Seq(
  "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion exclude ("com.fasterxml.jackson.core", "jackson-annotations"),
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
)

libraryDependencies ++= circe ++ cats ++ jackson