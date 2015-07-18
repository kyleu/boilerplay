package utils

import com.github.mauricio.async.db.Configuration
import com.typesafe.config.ConfigFactory
import _root_.play.api.Play

object Config {
  private[this] val cnf = ConfigFactory.load()

  val projectId = utils.BuildInfo.name
  val projectName = "Boilerplay"
  val version = "0.1"
  val hostname = cnf.getString("host")

  val debug = !Play.isProd(Play.current)

  val fileCacheDir = cnf.getString("cache.dir")

  // Database
  val databaseConfiguration = new Configuration(
    host = Option(cnf.getString("db.host")).getOrElse("localhost"),
    port = 5432,
    database = Option(cnf.getString("db.database")),
    username = Option(cnf.getString("db.username")).getOrElse(projectId),
    password = Option(cnf.getString("db.password"))
  )

  // Admin
  val adminEmail = cnf.getString("admin.email")
  val pageSize = 100

  // Metrics
  val jmxEnabled = cnf.getBoolean("metrics.jmx.enabled")
  val graphiteEnabled = cnf.getBoolean("metrics.graphite.enabled")
  val graphiteServer = cnf.getString("metrics.graphite.server")
  val graphitePort = cnf.getInt("metrics.graphite.port")
  val servletEnabled = cnf.getBoolean("metrics.servlet.enabled")
  val servletPort = cnf.getInt("metrics.servlet.port")
}
