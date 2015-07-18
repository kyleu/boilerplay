import com.typesafe.sbt.{ GitBranchPrompt, GitVersioning }
import sbt._
import sbt.Keys._

import net.virtualvoid.sbt.graph.Plugin.graphSettings
import com.typesafe.sbt.SbtScalariform.{ScalariformKeys, defaultScalariformSettings}

import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._

import playscalajs.ScalaJSPlay
import playscalajs.ScalaJSPlay.autoImport._

object Shared {
  val projectId = "boilerplay"

  val compileOptions = Seq(
    "-encoding", "UTF-8", "-feature", "-deprecation", "-unchecked", "–Xcheck-null", "-Xfatal-warnings", "-Xlint",
    "-Ywarn-adapted-args", "-Ywarn-dead-code", "-Ywarn-inaccessible", "-Ywarn-nullary-override", "-Ywarn-numeric-widen"
  )

  object Versions {
    val app = "0.1-SNAPSHOT"
    val scala = "2.11.7"
  }

  lazy val sharedJs = (crossProject.crossType(CrossType.Pure) in file("shared")).settings(
    scalaVersion := Versions.scala
  )
    .enablePlugins(ScalaJSPlay)
    .settings(
      sourceMapsBase := baseDirectory.value / "..",
      scalaJSStage in Global := FastOptStage
    ).js

  lazy val sharedJvm = (project in file("shared")).settings(
    scalaVersion := Versions.scala,
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
  )
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)
    .settings(graphSettings: _*)
    .settings(defaultScalariformSettings: _*)
}
