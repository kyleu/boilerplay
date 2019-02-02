import com.kyleu.projectile.sbt.SbtProjectile
import com.kyleu.projectile.sbt.ProjectVersion
import com.typesafe.sbt.GitPlugin.autoImport.git
import com.typesafe.sbt.gzip.Import._
import com.typesafe.sbt.jse.JsEngineImport.JsEngineKeys
import com.typesafe.sbt.less.Import._
import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging
import com.typesafe.sbt.packager.docker.DockerPlugin
import com.typesafe.sbt.packager.universal.UniversalPlugin
import com.typesafe.sbt.web.Import._
import com.typesafe.sbt.web.SbtWeb
import Dependencies._
import play.routes.compiler.InjectedRoutesGenerator
import play.sbt.PlayImport.PlayKeys
import play.sbt.routes.RoutesKeys
import webscalajs.WebScalaJS.autoImport._
import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._

object Server {
  private[this] val dependencies = Projectile.all ++ PlayFramework.all ++ Utils.all ++ Testing.all

  private[this] lazy val serverSettings = Shared.commonSettings ++ Seq(
    name := Shared.projectId,
    maintainer := "Boilerplay User <admin@boilerplay.com>",
    description := Shared.projectName,

    libraryDependencies ++= dependencies,

    // Play
    RoutesKeys.routesGenerator := InjectedRoutesGenerator,
    RoutesKeys.routesImport ++= Seq("com.kyleu.projectile.web.util.QueryStringUtils._", "util.web.ModelBindables._"),
    PlayKeys.externalizeResources := false,
    PlayKeys.devSettings := Seq("play.server.akka.requestTimeout" -> "infinite"),
    PlayKeys.playDefaultPort := Shared.projectPort,
    PlayKeys.playInteractionMode := PlayUtils.NonBlockingInteractionMode,

    // Assets
    packagedArtifacts in publishLocal := {
      val artifacts: Map[sbt.Artifact, java.io.File] = (packagedArtifacts in publishLocal).value
      val assets: java.io.File = (PlayKeys.playPackageAssets in Compile).value
      artifacts + (Artifact(moduleName.value, "jar", "jar", "assets") -> assets)
    },

    // Scala.js
    scalaJSProjects := Seq(Client.client),

    // Sbt-Web
    JsEngineKeys.engineType := JsEngineKeys.EngineType.Node,
    pipelineStages in Assets := Seq(scalaJSPipeline),
    pipelineStages ++= Seq(gzip),
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

    (sourceGenerators in Compile) += ProjectVersion.writeConfig(Shared.projectId, Shared.projectName, Shared.projectPort).taskValue
  )

  lazy val server = Project(id = Shared.projectId, base = file(".")).enablePlugins(
    SbtProjectile, SbtWeb, play.sbt.PlayScala, JavaAppPackaging, UniversalPlugin, DockerPlugin
  ).settings(serverSettings: _*).settings(Packaging.settings: _*)
}
