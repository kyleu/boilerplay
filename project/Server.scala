import com.sksamuel.scapegoat.sbt.ScapegoatSbtPlugin.autoImport._
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
import play.sbt.routes.RoutesKeys.routesGenerator
import play.sbt.PlayImport.PlayKeys._
import play.sbt.routes.RoutesKeys
import webscalajs.WebScalaJS.autoImport._
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._

object Server {
  private[this] val dependencies = {
    import Dependencies._
    Seq(
      Akka.actor, Akka.logging, Play.filters, Play.guice, Play.ws, Play.json, Play.cache,
      Database.mysql, Database.postgres, Database.hikariCp, GraphQL.sangria, GraphQL.playJson, GraphQL.circe,
      Authentication.silhouette, Authentication.hasher, Authentication.persistence, Authentication.crypto,
      WebJars.jquery, WebJars.fontAwesome, WebJars.materialize, WebJars.moment, WebJars.mousetrap,
      Utils.csv, Utils.scalaGuice, Utils.commonsIo, Utils.betterFiles, Akka.testkit, Play.test, Testing.scalaTest
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
    routesGenerator := InjectedRoutesGenerator,
    RoutesKeys.routesImport += "util.web.QueryStringUtils._",
    externalizeResources := false,

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
    fullClasspath in assembly += Attributed.blank(PlayKeys.playPackageAssets.value),
    mainClass in assembly := Some(Shared.projectName),

    // Code Quality
    scapegoatIgnoredFiles := Seq(".*/Row.scala", ".*/Routes.scala", ".*/ReverseRoutes.scala", ".*/JavaScriptReverseRoutes.scala", ".*/*.template.scala")
  )

  lazy val server = {
    val ret = Project(id = Shared.projectId, base = file(".")).enablePlugins(
      SbtWeb, play.sbt.PlayScala, JavaAppPackaging, diagram.ClassDiagramPlugin,
      UniversalPlugin, LinuxPlugin, DebianPlugin, RpmPlugin, DockerPlugin, WindowsPlugin, JDKPackagerPlugin
    ).settings(serverSettings: _*).settings(Packaging.settings: _*)

    Shared.withProjects(ret, Seq(Shared.sharedJvm, Utilities.metrics))
  }
}
