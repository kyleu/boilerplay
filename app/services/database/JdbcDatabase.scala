package services.database

import java.sql.Connection
import java.util.Properties

import com.codahale.metrics.MetricRegistry
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import models.database.{DatabaseConfig, RawQuery, Statement}
import util.metrics.{Checked, Instrumented}
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

abstract class JdbcDatabase(override val key: String, configPrefix: String) extends Database[Connection] {
  private[this] def time[A](klass: java.lang.Class[_])(f: => A) = {
    val ctx = Instrumented.metricRegistry.timer(MetricRegistry.name(klass)).time()
    try { f } finally { ctx.stop }
  }

  private[this] var ds: Option[HikariDataSource] = None

  def open(cfg: play.api.Configuration, svc: TracingService) = {
    ds.foreach(_ => throw new IllegalStateException("Database already initialized."))

    val config = DatabaseConfig.fromConfig(cfg, configPrefix)

    val properties = new Properties

    val url = s"jdbc:postgresql://${config.host}:${config.port}/${config.database.getOrElse(util.Config.projectId)}"

    val poolConfig = new HikariConfig(properties) {
      setPoolName(util.Config.projectId)
      setJdbcUrl(url)
      setUsername(config.username)
      setPassword(config.password.getOrElse(""))
      setConnectionTimeout(10000)
      setMinimumIdle(1)
      setMaximumPoolSize(32)

      setHealthCheckRegistry(Checked.healthCheckRegistry)
      setMetricRegistry(Instrumented.metricRegistry)
    }

    val poolDataSource = new HikariDataSource(poolConfig)

    ds = Some(poolDataSource)

    start(config, svc)
  }

  override def transaction[A](f: (TraceData, Connection) => Future[A], conn: Option[Connection])(implicit traceData: TraceData) = {
    ???
  }

  override def execute(statement: Statement, conn: Option[Connection])(implicit traceData: TraceData) = {
    val connection = conn.getOrElse(ds.get.getConnection)
    log.debug(s"${statement.sql} with ${statement.values.mkString("(", ", ", ")")}")
    val stmt = connection.prepareStatement(statement.sql)
    try {
      ???
      //prepare(stmt, statement.values)
      //stmt.executeUpdate()
    } finally {
      stmt.close()
    }
  }

  override def query[A](query: RawQuery[A], conn: Option[Connection])(implicit traceData: TraceData) = {
    val connection = ds.get.getConnection
    //try { time(query.getClass) { apply(connection, query) } } finally { connection.close() }
    ???
  }

  override def close() = {
    ds.foreach(_.close())
    true
  }
}
