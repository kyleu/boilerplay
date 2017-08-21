package services.database

import com.github.mauricio.async.db.pool.ConnectionPool
import com.github.mauricio.async.db.postgresql.PostgreSQLConnection
import com.github.mauricio.async.db.{Configuration, Connection, QueryResult}
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

  private[this] def prependComment(obj: Object, sql: String) = s"/* ${obj.getClass.getSimpleName.replace("$", "")} */ $sql"

  private[this] def trace[A](name: String)(f: TraceData => Future[A])(implicit traceData: TraceData) = tracing.trace(name) { td =>
    td.span.kind(brave.Span.Kind.CLIENT).remoteEndpoint(Endpoint.builder().port(getConfig.port).serviceName("database.master").build())
    f(td)
  }

  def transaction[A](f: (TraceData, Connection) => Future[A], conn: Connection = pool)(implicit traceData: TraceData): Future[A] = {
    trace("tx.open")(td => conn.inTransaction(c => f(td, c)))
  }

  def execute(statement: Statement, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[Int] = {
    val name = statement.getClass.getSimpleName.replaceAllLiterally("$", "")
    trace("execute." + name) { tn =>
      log.debug(s"Executing statement [$name] with SQL [${statement.sql}] with values [${statement.values.mkString(", ")}].")
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
    trace("query." + name) { tn =>
      log.debug(s"Executing query [$name] with SQL [${query.sql}] with values [${query.values.mkString(", ")}].")
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

  def raw(name: String, sql: String, conn: Option[Connection] = None)(implicit traceData: TraceData): Future[QueryResult] = {
    trace("raw." + name) { tn =>
      tn.span.tag(TraceKeys.SQL_QUERY, sql)
      log.debug(s"Executing raw query [$name] with SQL [$sql].")
      val ret = metrics.timer(s"raw.$name").timeFuture(conn.getOrElse(pool).sendQuery(prependComment(name, sql)))
      ret.failed.foreach(x => log.error(s"Error [${x.getClass.getSimpleName}] encountered while executing raw query [$name] with SQL [$sql].", x))
      ret
    }
  }
}
