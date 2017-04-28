package utils

import com.mohiva.play.silhouette.impl.authenticators.CookieAuthenticatorSettings
import play.api.{Environment, Mode}
import utils.metrics.MetricsConfig

@javax.inject.Singleton
class Configuration @javax.inject.Inject() (val cnf: play.api.Configuration, env: Environment) {
  val debug = env.mode == Mode.Dev
  val dataDir = new java.io.File(cnf.getString("data.directory").getOrElse("./data"))

  val metrics: MetricsConfig = MetricsConfig(
    jmxEnabled = cnf.getBoolean("metrics.jmx.enabled").getOrElse(true),
    graphiteEnabled = cnf.getBoolean("metrics.graphite.enabled").getOrElse(false),
    graphiteServer = cnf.getString("metrics.graphite.server").getOrElse("127.0.0.1"),
    graphitePort = cnf.getInt("metrics.graphite.port").getOrElse(2003),
    servletEnabled = cnf.getBoolean("metrics.servlet.enabled").getOrElse(true),
    servletPort = cnf.getInt("metrics.servlet.port").getOrElse(9001)
  )

  val cookieAuthSettings = {
    import scala.concurrent.duration._
    val cfg = cnf.getConfig("silhouette.authenticator.cookie").getOrElse {
      throw new IllegalArgumentException("Missing cookie configuration.")
    }

    CookieAuthenticatorSettings(
      cookieName = cfg.getString("name").getOrElse(throw new IllegalArgumentException()),
      cookiePath = cfg.getString("path").getOrElse(throw new IllegalArgumentException()),
      cookieDomain = cfg.getString("domain"),
      secureCookie = cfg.getBoolean("secure").getOrElse(throw new IllegalArgumentException()),
      httpOnlyCookie = true,
      useFingerprinting = cfg.getBoolean("useFingerprinting").getOrElse(throw new IllegalArgumentException()),
      cookieMaxAge = cfg.getInt("maxAge").map(_.seconds),
      authenticatorIdleTimeout = cfg.getInt("idleTimeout").map(_.seconds),
      authenticatorExpiry = cfg.getInt("expiry").map(_.seconds).getOrElse(throw new IllegalArgumentException())
    )
  }
}
