name := "Tezos-FullStack-Console-Translation-Module"

organization := "io.scalac"

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

lazy val scalaTestVersion = "3.0.8"

lazy val scalaTest = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)

lazy val jsonAssertVersion = "0.0.3"

lazy val jsonAssert = Seq(
  "com.stephenn" %% "scalatest-json-jsonassert" % jsonAssertVersion % Test,
)

libraryDependencies ++= circe ++ cats ++ jackson ++ scalaTest ++ jsonAssert

publishMavenStyle := true

publishTo := Some(Resolver.file("file",  new File("../mvn-repo")))

publishArtifact in Test := false

pomExtra := <url>http://www.scalac.io/</url>
  <licenses>
    <license>
      <name>MIT</name>
      <url>http://opensource.org/licenses/MIT</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:ScalaConsultants/Tezos-FullStack-Console-Translation-Module.git</url>
    <connection>scm:git:git@github.com:ScalaConsultants/Tezos-FullStack-Console-Translation-Module.git</connection>
  </scm>
  <developers>
    <developer>
      <id>scalac</id>
      <name>ScalaConsultants</name>
    </developer>
  </developers> 