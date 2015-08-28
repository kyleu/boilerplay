import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import com.typesafe.sbt.{ GitBranchPrompt, GitVersioning }
import com.typesafe.sbt.SbtScalariform.{ ScalariformKeys, defaultScalariformSettings }
import net.virtualvoid.sbt.graph.Plugin.graphSettings
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import playscalajs.ScalaJSPlay
import playscalajs.ScalaJSPlay.autoImport._
import sbt.Keys._
import sbt._

object Client {
  lazy val client = (project in file("client")).settings(
    scalaVersion := Shared.Versions.scala,
    persistLauncher := false,
    sourceMapsDirectories += Shared.sharedJs.base / "..",
    unmanagedSourceDirectories in Compile := Seq((scalaSource in Compile).value),
    libraryDependencies ++= Seq("com.lihaoyi" %%% "upickle" % "0.3.6"),
    scalaJSStage in Global := FastOptStage,
    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala"),
    ScalariformKeys.preferences := ScalariformKeys.preferences.value
  )
    .enablePlugins(GitVersioning)
    .enablePlugins(GitBranchPrompt)
    .settings(graphSettings: _*)
    .settings(defaultScalariformSettings: _*)
    .enablePlugins(ScalaJSPlugin, ScalaJSPlay)
    .dependsOn(Shared.sharedJs)
}
