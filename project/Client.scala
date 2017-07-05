import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
import org.scalajs.sbtplugin.ScalaJSPlugin
import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import webscalajs.ScalaJSWeb
import sbt.Keys._
import sbt._

object Client {
  private[this] val clientSettings = Shared.commonSettings ++ Seq(
    libraryDependencies ++= Seq(
      "be.doeraene" %%% "scalajs-jquery" % Dependencies.ScalaJS.jQueryVersion,
      "com.lihaoyi" %%% "scalatags" % Dependencies.ScalaJS.scalaTagsVersion
    ),
    testFrameworks += new TestFramework("utest.runner.Framework"),
    scalaJSStage in Global := FastOptStage,
    scapegoatIgnoredFiles := Seq(".*/JsonUtils.scala", ".*/JsonSerializers.scala")
  )

  lazy val client = (project in file("client"))
    .settings(clientSettings: _*)
    .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
    .dependsOn(Shared.sharedJs)
}
