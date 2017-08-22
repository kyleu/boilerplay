package services.database

import java.net.InetAddress

import com.github.mauricio.async.db.pool.ConnectionPool
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.{Configuration, Connection, QueryResult}
import com.google.common.net.InetAddresses
import models.database.{RawQuery, Statement}
import util.FutureUtils.databaseContext
import util.Logging
import util.metrics.Instrumented
import util.tracing.{TraceData, TracingService}
import zipkin.{Endpoint, TraceKeys}

import scala.concurrent.Future

trait DatabaseHelper extends Instrumented with Logging {
  protected[this] def tracing: TracingService
  protected[this] def pool: ConnectionPool[PostgreSQLConnection]
  protected[this] def getConfig: Configuration
  protected[this] def name: String

  private[this] lazy val endpoint = {
    val builder = Endpoint.builder().port(getConfig.port).serviceName("database." + name)
    InetAddress.getByName(getConfig.host) match {
      case x if x.getAddress.length == 4 => builder.ipv4(InetAddresses.coerceToInteger(x))
      case x => builder.ipv6(x.getAddress)
    }
    builder.build()
  }

  private[this] def prependComment(obj: Object, sql: String) = s"/* ${obj.getClass.getSimpleName.replace("$", "")} */ $sql"

  private[this] def trace[A](traceName: String)(f: TraceData => Future[A])(implicit traceData: TraceData) = tracing.trace(name + "." + traceName) { td =>
    td.span.kind(brave.Span.Kind.CLIENT).remoteEndpoint(endpoint)
    f(td)
  }

  def transaction[A](f: (TraceData, Connection) => Future[A], conn: Connection = pool)(implicit traceData: TraceData): Future[A] = {
    trace("tx.open")(td => conn.inTransaction(c => f(td, c)))
  }

  def execute(statement: Statement, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[Int] = {
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

  def query[A](query: RawQuery[A], conn: Option[Connection] = None)(implicit traceData: TraceData): Future[A] = {
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

  def raw(namw: String, sql: String, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[QueryResult] = {
    trace("raw." + name) { tn =>
      tn.span.tag(TraceKeys.SQL_QUERY, sql)
      log.debug(s"Executing raw query [$name] with SQL [$sql].")
      val ret = metrics.timer(s"raw.$name").timeFuture(conn.getOrElse(pool).sendQuery(prependComment(name, sql)))
      ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing raw query [$name] with SQL [$sql].", x))
      ret
    }
  }
}
