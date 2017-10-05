package models

import java.util.TimeZone

import akka.actor.{ActorSystem, Props}
import com.codahale.metrics.SharedMetricRegistries
import com.mohiva.play.silhouette.api.Silhouette
import models.auth.AuthEnv
import play.api.Environment
import play.api.inject.ApplicationLifecycle
import services.database.{ApplicationDatabase, MasterDdl, SystemDatabase}
import services.file.FileService
import services.supervisor.ActorSupervisor
import services.user.UserService
import util.FutureUtils.defaultContext
import services.cache.CacheService
import services.audit.AuditService
import services.settings.SettingsService
import util.{Config, FutureUtils, Logging}
import util.metrics.Instrumented
import util.tracing.TracingService
import util.web.TracingWSClient

import scala.concurrent.Future

object Application {
  var initialized = false
}

@javax.inject.Singleton
class Application @javax.inject.Inject() (
    val contexts: FutureUtils,
    val config: Configuration,
    val lifecycle: ApplicationLifecycle,
    val playEnv: Environment,
    val actorSystem: ActorSystem,
    val userService: UserService,
    val settingsService: SettingsService,
    val silhouette: Silhouette[AuthEnv],
    val ws: TracingWSClient,
    val notifications: AuditService,
    val tracing: TracingService
) extends Logging {
  if (Application.initialized) {
    log.info("Skipping initialization after failure.")
  } else {
    start()
  }

  val supervisor = actorSystem.actorOf(Props(classOf[ActorSupervisor], this), "supervisor")

  private[this] def start() = tracing.topLevelTrace("application.start") { implicit tn =>
    log.info(s"${Config.projectName} is starting.")
    Application.initialized = true

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    System.setProperty("user.timezone", "UTC")

    SharedMetricRegistries.remove("default")
    SharedMetricRegistries.add("default", Instrumented.metricRegistry)

    lifecycle.addStopHook(() => Future.successful(stop()))

    FileService.setRootDir(config.dataDir)

    SystemDatabase.open(config.cnf, tracing)
    ApplicationDatabase.open(config.cnf, tracing)
    MasterDdl.init().map { _ =>
      settingsService.load()
    }
  }

  private[this] def stop() = {
    ApplicationDatabase.close()
    SystemDatabase.close()
    CacheService.close()
    tracing.close()
    SharedMetricRegistries.remove("default")
  }
}
