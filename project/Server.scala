import com.typesafe.sbt.GitPlugin.autoImport.git
import com.typesafe.sbt.digest.Import._
import com.typesafe.sbt.gzip.Import._
import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import com.typesafe.sbt.less.Import._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.debian.DebianPlugin
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.jdkpackager.JDKPackagerPlugin
import com.typesafe.sbt.packager.linux.LinuxPlugin
import com.typesafe.sbt.packager.rpm.RpmPlugin
import com.typesafe.sbt.packager.universal.UniversalPlugin
import com.typesafe.sbt.packager.windows.WindowsPlugin
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.SbtWeb
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayImport.PlayKeys
import play.sbt.routes.RoutesKeys
import webscalajs.WebScalaJS.autoImport._
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._

object Server {
  private[this] val dependencies = {
    import Dependencies._
    Seq(
      Tracing.brave, Tracing.http, Tracing.logging,
      Metrics.metrics, Metrics.metricsJvm, Metrics.metricsHttp, Metrics.metricsPush,
      Akka.actor, Akka.logging, Akka.visualMailbox,
      Play.filters, Play.guice, Play.ws, Play.json, Play.cache,
      Database.postgres, Database.hikariCp,
      Database.slickCore, Database.slickHikariCp, Database.slickPg, Database.slickPgCirce, Database.slickless,
      GraphQL.sangria, GraphQL.playJson, GraphQL.circe,
      Authentication.silhouette, Authentication.hasher, Authentication.persistence, Authentication.crypto,
      WebJars.jquery, WebJars.fontAwesome, WebJars.materialize, WebJars.moment, WebJars.mousetrap,
      Utils.csv, Utils.scalaGuice, Utils.commonsIo, Utils.betterFiles,
      Akka.testkit, Play.test, Testing.scalaTest
    )
  }

  private[this] lazy val serverSettings = Shared.commonSettings ++ Seq(
    name := Shared.projectId,
    maintainer := "Boilerplay User <admin@boilerplay.com>",
    description := "Boilerplay",

    resolvers += Resolver.jcenterRepo,
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies ++= dependencies,

    // Play
    RoutesKeys.routesGenerator := InjectedRoutesGenerator,
    RoutesKeys.routesImport ++= Seq("util.web.QueryStringUtils._", "util.web.ModelBindables._"),
    PlayKeys.externalizeResources := false,
    PlayKeys.devSettings := Seq("play.server.akka.requestTimeout" -> "infinite"),

    // Scala.js
    scalaJSProjects := Seq(Client.client),

    // Sbt-Web
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    includeFilter in (Assets, LessKeys.less) := "*.less",
    excludeFilter in (Assets, LessKeys.less) := "_*.less",
    LessKeys.compress in Assets := true,

    // Source Control
    scmInfo := Some(ScmInfo(url("https://github.com/KyleU/boilerplay"), "git@github.com:KyleU/boilerplay.git")),
    git.remoteRepo := scmInfo.value.get.connection,

    // Fat-Jar Assembly
    assemblyJarName in assembly := Shared.projectId + ".jar",
    assemblyMergeStrategy in assembly := {
      case "play/reference-overrides.conf" => MergeStrategy.concat
      case x => (assemblyMergeStrategy in assembly).value(x)
    },
    fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value),
    mainClass in assembly := Some(Shared.projectName)
  )

  lazy val server = Project(id = Shared.projectId, base = file(".")).enablePlugins(
    SbtWeb, play.sbt.PlayScala, JavaAppPackaging, diagram.ClassDiagramPlugin,
    UniversalPlugin, LinuxPlugin, DebianPlugin, RpmPlugin, DockerPlugin, WindowsPlugin, JDKPackagerPlugin
  ).settings(serverSettings: _*).settings(Packaging.settings: _*).dependsOn(Shared.sharedJvm)
}
