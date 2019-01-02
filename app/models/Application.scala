package models

import java.util.TimeZone

import akka.actor.ActorSystem
import com.kyleu.projectile.services.database._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import com.mohiva.play.silhouette.api.Silhouette

import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.models.auth.AuthEnv
import play.api.Environment
import play.api.inject.ApplicationLifecycle
import services.audit.AuditService
import services.cache.CacheService
import services.file.FileService
import services.note.ModelNoteService
import services.settings.SettingsService
import util.Config
import com.kyleu.projectile.util.metrics.Instrumented
import com.kyleu.projectile.util.web.TracingWSClient

import scala.concurrent.{Await, Future}

object Application {
  var initialized = false

  @javax.inject.Singleton
  class CoreServices @javax.inject.Inject() (
      val settings: SettingsService,
      val notes: ModelNoteService
  )
}

@javax.inject.Singleton
class Application @javax.inject.Inject() (
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
    log.info("Skipping initialization after failure.")(TraceData.noop)
  } else {
    import scala.concurrent.duration._
    Await.result(start(), 20.seconds)
  }

  private[this] def start() = tracing.topLevelTrace("application.start") { implicit tn =>
    log.info(s"${Config.projectName} is starting.")
    Application.initialized = true

    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    System.setProperty("user.timezone", "UTC")
    util.EncryptionUtils.setKey(config.secretKey)

    if (config.metrics.micrometerEnabled) { Instrumented.start(config.metrics.micrometerEngine, Config.projectName, config.metrics.micrometerHost) }

    lifecycle.addStopHook(() => Future.successful(stop()))

    FileService.setRootDir(config.dataDir)

    ApplicationDatabase.open(config.cnf.underlying, tracing)

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
