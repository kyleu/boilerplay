import Dependencies._
import sbt.Keys._
import sbt._
import io.gatling.sbt.GatlingPlugin
import pl.project13.scala.sbt.JmhPlugin

object Utilities {
  private[this] val metricsLibs = Seq(
    Play.lib, Akka.actor, Tracing.brave, Tracing.http, Tracing.logging,
    Metrics.metrics, Metrics.healthChecks, Metrics.json, Metrics.jvm, Metrics.ehcache, Metrics.jettyServlet, Metrics.servlets, Metrics.graphite
  )

  lazy val metrics = (project in file("util/metrics"))
    .settings(libraryDependencies ++= metricsLibs)
    .settings(Shared.commonSettings: _*)

  lazy val benchmarking = (project in file("util/benchmarking"))
    .settings(libraryDependencies ++= Seq(Testing.gatlingCore, Testing.gatlingCharts))
    .settings(Shared.commonSettings: _*)
    .enablePlugins(GatlingPlugin, JmhPlugin)
    .dependsOn(Shared.sharedJvm)
}
