package util

import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticatorSettings
import play.api.{Environment, Mode}
import util.metrics.MetricsConfig

@javax.inject.Singleton
class Configuration @javax.inject.Inject() (val cnf: play.api.Configuration, env: Environment) {
  val debug = env.mode == Mode.Dev
  val dataDir = new java.io.File(cnf.get[Option[String]]("data.directory").getOrElse("./data"))

  val metrics: MetricsConfig = MetricsConfig(
    jmxEnabled = cnf.get[Option[Boolean]]("metrics.jmx.enabled").getOrElse(true),
    graphiteEnabled = cnf.get[Option[Boolean]]("metrics.graphite.enabled").getOrElse(false),
    graphiteServer = cnf.get[Option[String]]("metrics.graphite.server").getOrElse("127.0.0.1"),
    graphitePort = cnf.get[Option[Int]]("metrics.graphite.port").getOrElse(2003),
    servletEnabled = cnf.get[Option[Boolean]]("metrics.servlet.enabled").getOrElse(true),
    servletPort = cnf.get[Option[Int]]("metrics.servlet.port").getOrElse(9001)
  )

  val cookieAuthSettings = {
    import scala.concurrent.duration._
    val cfg = cnf.get[Option[play.api.Configuration]]("silhouette.authenticator.cookie").getOrElse {
      throw new IllegalArgumentException("Missing cookie configuration.")
    }

    CookieAuthenticatorSettings(
      cookieName = cfg.get[Option[String]]("name").getOrElse(throw new IllegalArgumentException()),
      cookiePath = cfg.get[Option[String]]("path").getOrElse(throw new IllegalArgumentException()),
      cookieDomain = cfg.get[Option[String]]("domain"),
      secureCookie = cfg.get[Option[Boolean]]("secure").getOrElse(throw new IllegalArgumentException()),
      httpOnlyCookie = true,
      useFingerprinting = cfg.get[Option[Boolean]]("useFingerprinting").getOrElse(throw new IllegalArgumentException()),
      cookieMaxAge = cfg.get[Option[Int]]("maxAge").map(_.seconds),
      authenticatorIdleTimeout = cfg.get[Option[Int]]("idleTimeout").map(_.seconds),
      authenticatorExpiry = cfg.get[Option[Int]]("expiry").map(_.seconds).getOrElse(throw new IllegalArgumentException())
    )
  }
}
