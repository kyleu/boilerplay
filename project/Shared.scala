import Dependencies._
import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.SbtScalariform.{ ScalariformKeys, scalariformSettings }
import net.virtualvoid.sbt.graph.DependencyGraphSettings.graphSettings
import webscalajs.ScalaJSWeb
import sbt.Keys._
import sbt._

import sbtcrossproject.CrossPlugin.autoImport._
import scalajscrossproject.ScalaJSCrossPlugin.autoImport.{ JSCrossProjectOps, JSPlatform}
import sbtcrossproject.{crossProject, CrossType}

object Shared {
  val projectId = "boilerplay"
  val projectName = "Boilerplay"

  object Versions {
    val app = "1.0.0"
    val scala = "2.12.3"
  }

  val compileOptions = Seq(
    "-target:jvm-1.8", "-encoding", "UTF-8", "-feature", "-deprecation", "-explaintypes", "-feature", "-unchecked",
    "–Xcheck-null", "-Xfatal-warnings", /* "-Xlint", */ "-Xcheckinit", "-Xfuture",
    "-Yno-adapted-args", "-Ywarn-dead-code", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-numeric-widen", "-Ywarn-infer-any"
  )

  lazy val commonSettings = Seq(
    version := Shared.Versions.app,
    scalaVersion := Shared.Versions.scala,

    scalacOptions ++= compileOptions,
    scalacOptions in (Compile, console) ~= (_.filterNot(Set(
      "-Ywarn-unused:imports",
      "-Xfatal-warnings"
    ))),
    scalacOptions in (Compile, doc) := Seq("-encoding", "UTF-8"),
    scalacOptions in Test ++= Seq("-Yrangepos"),

    publishMavenStyle := false,

    // Code Quality
    scapegoatVersion := Utils.scapegoatVersion,
    scapegoatDisabledInspections := Seq("MethodNames", "MethodReturningAny", "DuplicateImport"),
    scapegoatIgnoredFiles := Seq(".*/JsonSerializers.scala"),
    ScalariformKeys.preferences := ScalariformKeys.preferences.value,
    scapegoatDisabledInspections := Seq("FinalModifierOnCaseClass")
  ) ++ graphSettings ++ scalariformSettings

  def withProjects(p: Project, includes: Seq[Project]) = includes.foldLeft(p)((proj, inc) => proj.dependsOn(inc))

  lazy val shared = (crossProject(JSPlatform, JVMPlatform).crossType(CrossType.Pure) in file("shared")).settings(commonSettings: _*).settings(
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % Dependencies.Serialization.circeVersion,
      "io.circe" %%% "circe-generic" % Dependencies.Serialization.circeVersion,
      "io.circe" %%% "circe-generic-extras" % Dependencies.Serialization.circeVersion,
      "io.circe" %%% "circe-parser" % Dependencies.Serialization.circeVersion,
      "io.circe" %%% "circe-java8" % Dependencies.Serialization.circeVersion,
      "com.beachape" %%% "enumeratum-circe" % Dependencies.Utils.enumeratumVersion
    )
  ).jsSettings(
    libraryDependencies += "org.scala-js" %%% "scalajs-java-time" % "0.2.2"
  )

  lazy val sharedJs = shared.js.enablePlugins(ScalaJSWeb)

  lazy val sharedJvm = shared.jvm
}
