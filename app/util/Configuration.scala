package util

import better.files._
import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticatorSettings
import play.api.{Environment, Mode}
import util.metrics.MetricsConfig

@javax.inject.Singleton
class Configuration @javax.inject.Inject() (val cnf: play.api.Configuration, env: Environment) {
  val debug = env.mode == Mode.Dev
  val dataDir = cnf.get[Option[String]]("data.directory").getOrElse("./data").toFile

  val tracing = ""

  val metrics = MetricsConfig(
    jmxEnabled = cnf.get[Boolean]("metrics.jmx.enabled"),

    tracingEnabled = cnf.get[Boolean]("metrics.tracing.enabled"),
    tracingServer = cnf.get[String]("metrics.tracing.server"),
    tracingPort = cnf.get[Int]("metrics.tracing.port"),

    graphiteEnabled = cnf.get[Boolean]("metrics.graphite.enabled"),
    graphiteServer = cnf.get[String]("metrics.graphite.server"),
    graphitePort = cnf.get[Int]("metrics.graphite.port"),

    servletEnabled = cnf.get[Boolean]("metrics.servlet.enabled"),
    servletPort = cnf.get[Int]("metrics.servlet.port")
  )

  val cookieAuthSettings = {
    import scala.concurrent.duration._
    val cfg = cnf.get[Option[play.api.Configuration]]("silhouette.authenticator.cookie").getOrElse {
      throw new IllegalArgumentException("Missing cookie configuration.")
    }

    CookieAuthenticatorSettings(
      cookieName = cfg.get[String]("name"),
      cookiePath = cfg.get[String]("path"),
      cookieDomain = Some(cfg.get[String]("domain")),
      secureCookie = cfg.get[Boolean]("secure"),
      httpOnlyCookie = true,
      useFingerprinting = cfg.get[Boolean]("useFingerprinting"),
      cookieMaxAge = Some(cfg.get[Int]("maxAge").seconds),
      authenticatorIdleTimeout = Some(cfg.get[Int]("idleTimeout").seconds),
      authenticatorExpiry = cfg.get[Int]("expiry").seconds
    )
  }
}
