package services.database

import com.github.mauricio.async.db.pool.{ConnectionPool, PoolConfiguration}
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import com.github.mauricio.async.db.{Configuration, Connection, QueryResult}
import models.database.{RawQuery, Statement}
import org.slf4j.LoggerFactory
import util.FutureUtils.databaseContext
import util.metrics.Instrumented
import util.tracing.{TraceData, TracingService}
import zipkin.TraceKeys

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Database extends Instrumented {
  private[this] val log = LoggerFactory.getLogger(Database.getClass)
  private[this] val poolConfig = new PoolConfiguration(maxObjects = 100, maxIdle = 10, maxQueueSize = 1000)
  private[this] var factory: PostgreSQLConnectionFactory = _
  private[this] var pool: ConnectionPool[PostgreSQLConnection] = _
  private[this] var config: Option[Configuration] = None
  private[this] var tracingService: Option[TracingService] = None
  private[this] def tracing = tracingService.getOrElse(throw new IllegalStateException())
  def getConfig = config.getOrElse(throw new IllegalStateException("Database not open."))
  private[this] lazy val endpoint = tracing.endpointFor("database.master")

  private[this] def prependComment(obj: Object, sql: String) = s"/* ${obj.getClass.getSimpleName.replace("$", "")} */ $sql"

  def open(cfg: play.api.Configuration, svc: TracingService): Unit = {
    def get(k: String) = cfg.get[String]("database." + k)
    tracingService = Some(svc)
    open(get("host"), get("port").toInt, get("username"), Some(get("password")), Some(get("database")))
  }

  private[this] def open(host: String, port: Int = 5432, username: String, password: Option[String] = None, database: Option[String] = None): Unit = {
    open(Configuration(username, host, port, password, database))
  }

  private[this] def open(configuration: Configuration): Unit = {
    factory = new PostgreSQLConnectionFactory(configuration)
    pool = new ConnectionPool(factory, poolConfig)
    config = Some(configuration)

    val healthCheck = pool.sendQuery("select now()")
    healthCheck.failed.foreach(x => throw new IllegalStateException("Cannot connect to database.", x))
    Await.result(healthCheck.map(r => r.rowsAffected == 1.toLong), 5.seconds)
  }

  def transaction[A](f: (Connection) => Future[A], conn: Connection = pool)(implicit traceData: TraceData): Future[A] = tracing.trace("tx.open") { tn =>
    tn.span.remoteEndpoint(endpoint)
    conn.inTransaction(c => f(c))
  }

  def execute(statement: Statement, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[Int] = {
    val name = statement.getClass.getSimpleName.replaceAllLiterally("$", "")
    tracing.trace("execute." + name) { tn =>
      log.debug(s"Executing statement [$name] with SQL [${statement.sql}] with values [${statement.values.mkString(", ")}].")
      tn.span.remoteEndpoint(endpoint)
      tn.span.tag(TraceKeys.SQL_QUERY, statement.sql)
      val ret = metrics.timer(s"execute.$name").timeFuture {
        conn.getOrElse(pool).sendPreparedStatement(prependComment(statement, statement.sql), statement.values).map(_.rowsAffected.toInt)
      }
      ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing statement [$name] with SQL [${statement.sql}].", x))
      ret
    }
  }

  def query[A](query: RawQuery[A], conn: Option[Connection] = None)(implicit traceData: TraceData): Future[A] = {
    val name = query.getClass.getSimpleName.replaceAllLiterally("$", "")
    tracing.trace("query." + name) { tn =>
      log.debug(s"Executing query [$name] with SQL [${query.sql}] with values [${query.values.mkString(", ")}].")
      tn.span.remoteEndpoint(endpoint)
      tn.span.tag(TraceKeys.SQL_QUERY, query.sql)
      val ret = metrics.timer(s"query.$name").timeFuture {
        conn.getOrElse(pool).sendPreparedStatement(prependComment(query, query.sql), query.values).map { r =>
          query.handle(r.rows.getOrElse(throw new IllegalStateException()))
        }
      }
      ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing query [$name] with SQL [${query.sql}].", x))
      ret
    }
  }

  def raw(name: String, sql: String, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[QueryResult] = tracing.trace("raw." + name) { _ =>
    log.debug(s"Executing raw query [$name] with SQL [$sql].")
    val ret = metrics.timer(s"raw.$name").timeFuture {
      conn.getOrElse(pool).sendQuery(prependComment(name, sql))
    }
    ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing raw query [$name] with SQL [$sql].", x))
    ret
  }

  def close() = {
    Await.result(pool.close, 5.seconds)
    true
  }
}
