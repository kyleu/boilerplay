package services.database

import java.sql.Connection
import java.util.Properties
import java.util.concurrent.TimeUnit

import cats.effect.IO
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.Transactor
import doobie.util.transactor
import models.database.jdbc.Queryable
import models.database.{DatabaseConfig, RawQuery, Statement}
import util.metrics.Instrumented
import util.tracing.{TraceData, TracingService}

import scala.util.control.NonFatal

abstract class JdbcDatabase(override val key: String, configPrefix: String) extends Database[Connection] with Queryable {
  protected val metricsId = s"${util.Config.projectId}_${key}_database"

  private[this] def time[A](method: String, name: String)(f: => A) = {
    val startNanos = System.nanoTime
    try { f } finally {
      Instrumented.regOpt.foreach(_.timer(metricsId, "method", method, "name", name).record(System.nanoTime - startNanos, TimeUnit.NANOSECONDS))
    }
  }

  private[this] var ds: Option[HikariDataSource] = None
  def source = ds.getOrElse(throw new IllegalStateException("Database not initialized."))

  private[this] var slickOpt: Option[SlickQueryService] = None
  def slick = slickOpt.getOrElse(throw new IllegalStateException("Slick not initialized."))

  private[this] var doobieOpt: Option[DoobieQueryService] = None
  def doobie = doobieOpt.getOrElse(throw new IllegalStateException("Doobie not initialized."))

  def open(cfg: play.api.Configuration, svc: TracingService) = {
    ds.foreach(_ => throw new IllegalStateException("Database already initialized."))

    Class.forName("org.postgresql.Driver")
    val config = DatabaseConfig.fromConfig(cfg, configPrefix)
    val properties = new Properties
    val maxPoolSize = 32

    val poolConfig = new HikariConfig(properties) {
      setPoolName(util.Config.projectId + "." + key)
      setJdbcUrl(config.url)
      setUsername(config.username)
      setPassword(config.password.getOrElse(""))
      setConnectionTimeout(10000)
      setMinimumIdle(1)
      setMaximumPoolSize(maxPoolSize)
    }

    val poolDataSource = new HikariDataSource(poolConfig)

    ds = Some(poolDataSource)

    start(config, svc)

    if (config.enableSlick) { slickOpt = Some(new SlickQueryService(key, source, maxPoolSize, svc)) }
    if (config.enableDoobie) { doobieOpt = Some(new DoobieQueryService(key, source, svc)) }
  }

  override def transaction[A](f: (TraceData, Connection) => A)(implicit traceData: TraceData) = trace("transaction") { td =>
    val connection = source.getConnection
    connection.setAutoCommit(false)
    try {
      val result = f(td, connection)
      connection.commit()
      result
    } catch {
      case NonFatal(x) =>
        connection.rollback()
        throw x
    } finally {
      connection.close()
    }
  }

  override def execute(statement: Statement, conn: Option[Connection])(implicit traceData: TraceData) = trace("execute." + statement.name) { td =>
    td.tag("SQL", statement.sql)
    val connection = conn.getOrElse(source.getConnection)
    try {
      time("execute", statement.getClass.getName) { executeUpdate(connection, statement) }
    } catch {
      case NonFatal(x) => log.errorThenThrow(s"Error executing [${statement.name}] with [${statement.values.size}] values and sql [${statement.sql}].", x)
    } finally {
      if (conn.isEmpty) { connection.close() }
    }
  }

  override def query[A](query: RawQuery[A], conn: Option[Connection])(implicit traceData: TraceData) = trace("query." + query.name) { td =>
    td.tag("SQL", query.sql)
    val connection = conn.getOrElse(source.getConnection)
    try {
      time[A]("query", query.getClass.getName)(apply(connection, query))
    } catch {
      case NonFatal(x) => log.errorThenThrow(s"Error running query [${query.name}] with [${query.values.size}] values and sql [${query.sql}].", x)
    } finally {
      if (conn.isEmpty) { connection.close() }
    }
  }

  def withConnection[T](f: Connection => T) = {
    val conn = source.getConnection()
    try { f(conn) } finally { conn.close() }
  }

  override def close() = {
    slickOpt.foreach(_.close())
    ds.foreach(_.close())
    true
  }
}
