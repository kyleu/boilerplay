import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport.{scapegoatIgnoredFiles, scapegoatDisabledInspections}
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
      GraphQL.sangria, GraphQL.playJson, GraphQL.circe,
      WebJars.jquery, WebJars.fontAwesome, WebJars.materialize,
      Utils.csv, Utils.scalaGuice, Utils.commonsIo, Utils.betterFiles, Utils.enumeratumCirce,
      Akka.testkit, Play.test, Testing.scalaTest
    ) ++ Dependencies.Serialization.circeProjects.map(c => "io.circe" %% c % Dependencies.Serialization.circeVersion)
  }

  private[this] lazy val serverSettings = Shared.commonSettings ++ Seq(
    name := Shared.projectId,
    maintainer := "Boilerplay User <admin@boilerplay.com>",
    description := "Boilerplay",

    resolvers += Resolver.jcenterRepo,
    resolvers += Resolver.bintrayRepo("stanch", "maven"),
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    libraryDependencies ++= dependencies,

    // Play
    RoutesKeys.routesGenerator := InjectedRoutesGenerator,
    RoutesKeys.routesImport ++= Seq("util.web.QueryStringUtils._"),
    PlayKeys.externalizeResources := false,
    PlayKeys.devSettings := Seq("play.server.akka.requestTimeout" -> "infinite"),
    PlayKeys.playDefaultPort := 9000,

    // Sbt-Web
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node,
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
    mainClass in assembly := Some("Entrypoint"),

    scapegoatIgnoredFiles := Seq(".*/Routes.scala", ".*/RoutesPrefix.scala", ".*/*ReverseRoutes.scala", ".*/*.template.scala"),
    scapegoatDisabledInspections := Seq("UnusedMethodParameter")
  )

  lazy val server = Project(id = Shared.projectId, base = file(".")).enablePlugins(
    SbtWeb, play.sbt.PlayScala, JavaAppPackaging, diagram.ClassDiagramPlugin,
    UniversalPlugin, LinuxPlugin, DebianPlugin, RpmPlugin, DockerPlugin, WindowsPlugin, JDKPackagerPlugin
  ).settings(serverSettings: _*).settings(Packaging.settings: _*)
}
