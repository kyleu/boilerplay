package utils

import java.util.TimeZone

import akka.actor.{ActorSystem, Props}
import com.codahale.metrics.SharedMetricRegistries
import com.mohiva.play.silhouette.api.Silhouette
import models.auth.AuthEnv
import org.joda.time.DateTimeZone
import play.api.Environment
import play.api.inject.ApplicationLifecycle
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.ws.WSClient
import services.database.{Database, MasterDdl}
import services.settings.SettingsService
import services.supervisor.ActorSupervisor
import utils.metrics.Instrumented

import scala.concurrent.Future

object Application {
  var initialized = false
}

@javax.inject.Singleton
class Application @javax.inject.Inject() (
    val config: Configuration,
    val lifecycle: ApplicationLifecycle,
    val playEnv: Environment,
    val actorSystem: ActorSystem,
    val silhouette: Silhouette[AuthEnv],
    val ws: WSClient
) extends Logging {
  if (Application.initialized) {
    log.info("Skipping initialization after failure.")
  } else {
    start()
  }

  val supervisor = actorSystem.actorOf(Props(classOf[ActorSupervisor], this), "supervisor")
  log.debug(s"Actor Supervisor [${supervisor.path}] started for [${utils.Config.projectId}].")

  private[this] def start() = {
    log.info(s"${Config.projectName} is starting.")
    Application.initialized = true

    DateTimeZone.setDefault(DateTimeZone.UTC)
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

    SharedMetricRegistries.remove("default")
    SharedMetricRegistries.add("default", Instrumented.metricRegistry)

    lifecycle.addStopHook(() => Future.successful(stop()))

    Database.open(config.cnf)
    MasterDdl.init().map { ok =>
      SettingsService.load()
    }
  }

  private[this] def stop() = {
    Database.close()
    SharedMetricRegistries.remove("default")
  }
}
