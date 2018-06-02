package models

import java.util.TimeZone

import akka.actor.ActorSystem
import play.api.Environment
import play.api.inject.ApplicationLifecycle
import util.metrics.Instrumented
import util.tracing.TracingService
import util.web.TracingWSClient
import util.{Config, FutureUtils, Logging}

import scala.concurrent.{Await, Future}

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
    val ws: TracingWSClient,
    val tracing: TracingService
) extends Logging {
  if (Application.initialized) {
    log.info("Skipping initialization after failure.")
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

    if (config.metrics.prometheusEnabled) { Instrumented.start(config.metrics.prometheusPort) }

    lifecycle.addStopHook(() => Future.successful(stop()))

    Future.successful(true)
  }

  private[this] def stop() = {
    if (config.metrics.tracingEnabled) { tracing.close() }
    if (config.metrics.prometheusEnabled) { Instrumented.stop() }
  }
}
