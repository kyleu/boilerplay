package services.database

import com.github.mauricio.async.db.{Configuration, Connection, QueryResult}
import com.github.mauricio.async.db.pool.{ConnectionPool, PoolConfiguration}
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.postgresql.pool.PostgreSQLConnectionFactory
import models.database.{DatabaseConfig, RawQuery, Statement}
import util.FutureUtils.databaseContext
import util.tracing.{TraceData, TracingService}
import zipkin.TraceKeys

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._

abstract class AsyncDatabase(override val key: String, configPrefix: String) extends Database[Connection] {
  private[this] val poolConfig = new PoolConfiguration(maxObjects = 100, maxIdle = 10, maxQueueSize = 1000)

  private[this] var poolOption: Option[ConnectionPool[PostgreSQLConnection]] = None
  protected[this] def pool: ConnectionPool[PostgreSQLConnection] = poolOption.getOrElse {
    throw new IllegalStateException("Pool not configured. Did you forget to call \"open\"?")
  }

  def open(cfg: play.api.Configuration, svc: TracingService) = {
    start(DatabaseConfig.fromConfig(cfg, configPrefix), svc)
    val configuration = Configuration(getConfig.username, getConfig.host, getConfig.port, getConfig.password, getConfig.database)
    val factory = new PostgreSQLConnectionFactory(configuration)
    poolOption = Some(new ConnectionPool(factory, poolConfig))
    val healthCheck = pool.sendQuery("select now()")
    healthCheck.failed.foreach(x => throw new IllegalStateException("Cannot connect to database.", x))
    Await.result(healthCheck.map(r => r.rowsAffected == 1.toLong), 5.seconds)
  }

  override def transaction[A](f: (TraceData, Connection) => Future[A], conn: Option[Connection] = None)(implicit traceData: TraceData): Future[A] = {
    trace("tx.open")(td => conn.getOrElse(pool).inTransaction(c => f(td, c)))
  }

  override def execute(statement: Statement, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[Int] = {
    val n = statement.name
    trace("execute." + n) { tn =>
      log.debug(s"Executing statement [$n] with SQL [${statement.sql}] with values [${statement.values.mkString(", ")}].")
      tn.span.tag(TraceKeys.SQL_QUERY, statement.sql)
      val ret = metrics.timer(s"execute.$n").timeFuture {
        conn.getOrElse(pool).sendPreparedStatement(prependComment(statement, statement.sql), statement.values).map(_.rowsAffected.toInt)
      }
      ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing statement [$n] with SQL [${statement.sql}].", x))
      ret
    }
  }

  override def query[A](query: RawQuery[A], conn: Option[Connection] = None)(implicit traceData: TraceData): Future[A] = {
    val n = query.name
    trace("query." + n) { tn =>
      log.debug(s"Executing query [$n] with SQL [${query.sql}] with values [${query.values.mkString(", ")}].")
      tn.span.tag(TraceKeys.SQL_QUERY, query.sql)
      val ret = metrics.timer(s"query.$n").timeFuture {
        conn.getOrElse(pool).sendPreparedStatement(prependComment(query, query.sql), query.values).map { r =>
          query.handle(r.rows.getOrElse(throw new IllegalStateException()))
        }
      }
      ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing query [$n] with SQL [${query.sql}].", x))
      ret
    }
  }

  def raw(name: String, sql: String, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[QueryResult] = {
    trace("raw." + name) { tn =>
      tn.span.tag(TraceKeys.SQL_QUERY, sql)
      log.debug(s"Executing raw query [$name] with SQL [$sql].")
      val ret = metrics.timer(s"raw.$name").timeFuture(conn.getOrElse(pool).sendQuery(prependComment(name, sql)))
      ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing raw query [$name] with SQL [$sql].", x))
      ret
    }
  }

  override def close() = {
    Await.result(pool.close, 5.seconds)
    true
  }
}
