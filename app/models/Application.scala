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
import services.user.SystemUserService
import services.cache.CacheService
import services.audit.AuditService
import services.note.ModelNoteService
import services.settings.SettingsService
import util.{Config, FutureUtils, Logging}
import util.FutureUtils.defaultContext
import util.metrics.Instrumented
import util.tracing.TracingService
import util.web.TracingWSClient

import scala.concurrent.{Await, Future}

object Application {
  var initialized = false

  @javax.inject.Singleton
  class CoreServices @javax.inject.Inject() (
      val users: SystemUserService,
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
    import scala.concurrent.duration._
    Await.result(start(), 20.seconds)
  }

  val supervisor = actorSystem.actorOf(Props(classOf[ActorSupervisor], this), "supervisor")

  private[this] def start() = tracing.topLevelTrace("application.start") { implicit tn =>
    log.info(s"${Config.projectName} is starting.")
    Application.initialized = true

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    System.setProperty("user.timezone", "UTC")
    util.EncryptionUtils.setKey(config.secretKey)

    if (config.metrics.micrometerEnabled) { Instrumented.start() }

    lifecycle.addStopHook(() => Future.successful(stop()))

    FileService.setRootDir(config.dataDir)

    ApplicationDatabase.open(config.cnf, tracing)

    val flyway = new org.flywaydb.core.Flyway()
    flyway.setDataSource(ApplicationDatabase.source)
    flyway.migrate()

    coreServices.settings.load().map { _ =>
      true
    }
  }

  private[this] def stop() = {
    ApplicationDatabase.close()

    CacheService.close()
    if (config.metrics.tracingEnabled) { tracing.close() }
    if (config.metrics.micrometerEnabled) { Instrumented.stop() }
  }
}
