import Dependencies._
import com.typesafe.sbt.SbtScalariform.{ ScalariformKeys, scalariformSettings }
import com.typesafe.sbt.web.SbtWeb
import net.virtualvoid.sbt.graph.DependencyGraphSettings.graphSettings
import sbt.Keys._
import sbt._
import io.gatling.sbt.GatlingPlugin
import pl.project13.scala.sbt.JmhPlugin

object Utilities {
  private[this] val metricsLibs = Seq(
    Play.playLib, Akka.actor,
    Metrics.metrics, Metrics.healthChecks, Metrics.json, Metrics.jvm, Metrics.ehcache, Metrics.jettyServlet, Metrics.servlets, Metrics.graphite
  )

  lazy val metrics = (project in file("util/metrics"))
    .settings(ScalariformKeys.preferences := ScalariformKeys.preferences.value)
    .settings(libraryDependencies ++= metricsLibs)
    .settings(Shared.commonSettings: _*)
    .settings(graphSettings: _*)
    .settings(scalariformSettings: _*)

  lazy val translation = (project in file("util/translation"))
    .settings(ScalariformKeys.preferences := ScalariformKeys.preferences.value)
    .enablePlugins(SbtWeb, play.sbt.PlayScala)
    .settings(libraryDependencies ++= Seq(Utils.enumeratum, Play.playWs))
    .settings(Shared.commonSettings: _*)
    .settings(graphSettings: _*)
    .settings(scalariformSettings: _*)

  lazy val benchmarking = (project in file("util/benchmarking"))
    .settings(ScalariformKeys.preferences := ScalariformKeys.preferences.value)
    .settings(libraryDependencies ++= Seq(Testing.gatlingCore, Testing.gatlingCharts))
    .settings(Shared.commonSettings: _*)
    .enablePlugins(GatlingPlugin)
    .enablePlugins(JmhPlugin)
    .dependsOn(Shared.sharedJvm)
}
