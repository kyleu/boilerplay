package models

import java.util.TimeZone

import akka.actor.{ActorSystem, Props}
import com.mohiva.play.silhouette.api.Silhouette
import models.auth.AuthEnv
import play.api.Environment
import play.api.inject.ApplicationLifecycle
import services.database._
import services.file.FileService
import services.supervisor.ActorSupervisor
import services.user.UserService
import services.cache.CacheService
import services.audit.AuditService
import services.note.ModelNoteService
import services.settings.SettingsService
import util.{Config, FutureUtils, Logging}
import util.metrics.Instrumented
import util.tracing.TracingService
import util.web.TracingWSClient

import scala.concurrent.Future

object Application {
  var initialized = false

  @javax.inject.Singleton
  class CoreServices @javax.inject.Inject() (
      val users: UserService,
      val settings: SettingsService,
      val audits: AuditService,
      val notes: ModelNoteService
  )
}

@javax.inject.Singleton
class Application @javax.inject.Inject() (
    val contexts: FutureUtils,
    val config: Configuration,
    val lifecycle: ApplicationLifecycle,
    val playEnv: Environment,
    val actorSystem: ActorSystem,
    val coreServices: Application.CoreServices,
    val silhouette: Silhouette[AuthEnv],
    val ws: TracingWSClient,
    val tracing: TracingService
) extends Logging {
  if (Application.initialized) {
    log.info("Skipping initialization after failure.")
  } else {
    start()
  }

  val supervisor = actorSystem.actorOf(Props(classOf[ActorSupervisor], this), "supervisor")

  private[this] def start() = tracing.topLevelTraceBlocking("application.start") { implicit tn =>
    log.info(s"${Config.projectName} is starting.")
    Application.initialized = true

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    System.setProperty("user.timezone", "UTC")

    if (config.metrics.prometheusEnabled) { Instrumented.start(config.metrics.prometheusPort) }

    lifecycle.addStopHook(() => Future.successful(stop()))

    FileService.setRootDir(config.dataDir)

    SystemDatabase.open(config.cnf, tracing)
    ApplicationDatabase.open(config.cnf, tracing)
    MasterDdl.init()
    coreServices.settings.load()

    true
  }

  private[this] def stop() = {
    ApplicationDatabase.close()
    SystemDatabase.close()
    CacheService.close()
    if (config.metrics.tracingEnabled) { tracing.close() }
    if (config.metrics.prometheusEnabled) { Instrumented.stop() }
  }
}
