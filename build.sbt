import sbtcrossproject.CrossPlugin.autoImport.crossProject
import scalajsbundler.util.JSON._
import scala.sys.process._

lazy val commonSettings =
  Seq(organization := "io.scalac", scalaVersion := "2.12.11", version := "0.1")

lazy val circeVersion = "0.13.0"

lazy val circe = Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-parser",
  "io.circe" %% "circe-generic"
).map(_ % circeVersion)

lazy val catsVersion = "2.2.0-M1"

lazy val cats = Seq("org.typelevel" %% "cats-core" % catsVersion)

lazy val scalaTestVersion = "3.1.1"

lazy val scalaTest = Seq(
  "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
)
lazy val commonsText = Seq("org.apache.commons" % "commons-text" % "1.7")

lazy val core = crossProject(JSPlatform, JVMPlatform)
  .in(file("core"))
  .settings(commonSettings: _*)
  .settings(
    name := "core",
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % circeVersion,
      "io.circe" %%% "circe-parser" % circeVersion,
      "io.circe" %%% "circe-generic" % circeVersion,
      "org.typelevel" %%% "cats-core" % catsVersion,
      "org.scalatest" %%% "scalatest" % scalaTestVersion % "test"
    )
  )
  .jvmSettings(libraryDependencies ++= commonsText)
  .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
  .jsSettings(
    webpackBundlingMode := BundlingMode.LibraryAndApplication(),
    npmExtraArgs in Compile := Seq("-silent"),
    additionalNpmConfig in Compile := Map(
      "name" -> str("scalac-tezos-contract-translator"),
      "version" -> str("0.1.0")
    ),
    libraryDependencies ++= Seq(
      "io.github.cquiroz" %%% "scala-java-time" % "2.0.0-RC5" % Test
    ),
    buildJS.in(Compile) := {
      (webpack in (Compile, fullOptJS)).value
      val buildDir = target.value / "build"
      s"rm -rf $buildDir" !;
      s"mkdir $buildDir -p" !;
      s"cp ${crossTarget.value}/scalajs-bundler/main/package.json             $buildDir/package.json" !;
      s"cp ${crossTarget.value}/scalajs-bundler/main/${name.value}-opt.js     $buildDir/index.js" !;
      s"cp ${crossTarget.value}/scalajs-bundler/main/${name.value}-opt.js.map $buildDir/index.js.map" !
    }
  )

lazy val coreJVM = core.jvm
lazy val coreJS = core.js

lazy val root = project
  .in(file("."))
  .aggregate(coreJS, coreJVM)
  .dependsOn(coreJVM % "compile->compile;test->test")
  .settings(commonSettings: _*)
  .settings(
    name := "tezos-fullstack-console-translation-module",
    libraryDependencies ++= circe ++ cats ++ scalaTest ++ commonsText,
    publishMavenStyle := true,
    publishTo := Some(Resolver.file("file", new File("../mvn-repo"))),
    publishArtifact in Test := false,
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
  )

lazy val buildJS = taskKey[Unit]("Prepare a production js build")
