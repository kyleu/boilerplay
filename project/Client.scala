import org.scalajs.sbtplugin.ScalaJSPlugin
import webscalajs.ScalaJSWeb
import sbt.Keys._
import sbt._
import Dependencies._

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Client {
  private[this] val clientSettings = Shared.commonSettings ++ Seq(
    libraryDependencies ++= Serialization.circeProjects.map(c => "io.circe" %%% c % Serialization.circeVersion) ++ Seq(
      "be.doeraene" %%% "scalajs-jquery" % ScalaJS.jQueryVersion,
      "com.lihaoyi" %%% "scalatags" % Utils.scalatagsVersion,
      "com.beachape" %%% "enumeratum-circe" % Utils.enumeratumCirceVersion,
      "io.github.cquiroz" %%% "scala-java-time" % Utils.javaTimeVersion
    )
  )

  lazy val client = (project in file("client")).settings(clientSettings: _*).enablePlugins(ScalaJSPlugin, ScalaJSWeb)
}
