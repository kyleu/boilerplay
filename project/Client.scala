import org.scalajs.sbtplugin.ScalaJSPlugin
import webscalajs.ScalaJSWeb
import sbt.Keys._
import sbt._

import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Client {
  private[this] val clientSettings = Shared.commonSettings ++ Seq(
    libraryDependencies ++= Seq("com.kyleu" %%% "projectile-lib-scalajs" % Dependencies.Projectile.version)
  )

  lazy val client = (project in file("client")).settings(clientSettings: _*).enablePlugins(ScalaJSPlugin, ScalaJSWeb)
}
