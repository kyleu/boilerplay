import org.scalajs.sbtplugin.ScalaJSPlugin
import webscalajs.ScalaJSWeb
import sbt.Keys._
import sbt._

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Client {
  val circeVersion = "0.11.0"
  val circeProjects = Seq("circe-core", "circe-generic", "circe-generic-extras", "circe-parser", "circe-java8")

  private[this] val clientSettings = Shared.commonSettings ++ Seq(
    libraryDependencies ++= circeProjects.map(c => "io.circe" %%% c % circeVersion) ++ Seq(
      "com.kyleu" %%% "projectile-lib-scalajs" % Dependencies.Projectile.version
    )
  )

  lazy val client = (project in file("client")).settings(clientSettings: _*).enablePlugins(ScalaJSPlugin, ScalaJSWeb)
}
