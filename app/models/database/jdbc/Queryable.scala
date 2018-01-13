package models.database.jdbc

import java.sql.{Connection, PreparedStatement, Types}
import java.util.UUID

import enumeratum.values.StringEnumEntry
import io.circe.Json
import models.database.{Query, RawQuery, Statement}
import util.{Logging, NullUtils}

import scala.annotation.tailrec
import scala.util.control.NonFatal

trait Queryable extends Logging {
  @tailrec
  @SuppressWarnings(Array("AsInstanceOf"))
  private[this] def prepare(stmt: PreparedStatement, values: Seq[Any], index: Int = 1): Unit = {
    if (values.nonEmpty) {
      values.headOption.getOrElse(throw new IllegalStateException()) match {
        case v if NullUtils.isNull(v) => stmt.setNull(index, Types.NULL)

        case Some(x) => stmt.setObject(index, Conversions.convert(x.asInstanceOf[AnyRef]))
        case None => stmt.setNull(index, Types.NULL)

        case v => stmt.setObject(index, Conversions.convert(v.asInstanceOf[AnyRef]))
      }
      prepare(stmt, values.tail, index + 1)
    }
  }

  def valForJdbc(conn: Connection, v: Any): Any = v match {
    case Some(x) => Some(valForJdbc(conn, x))
    case seq: Seq[_] => conn.createArrayOf("text", seq.map(_.toString).toArray)
    case enum: StringEnumEntry => enum.value
    case json: Json => json.spaces2
    case ldt: java.time.LocalDateTime => java.sql.Timestamp.valueOf(ldt)
    case ld: java.time.LocalDate => java.sql.Date.valueOf(ld)
    case lt: java.time.LocalTime => java.sql.Time.valueOf(lt)
    case x => x
  }

  def valsForJdbc(conn: Connection, vals: Seq[Any]) = vals.map(v => valForJdbc(conn, v)).toIndexedSeq

  def apply[A](connection: Connection, query: RawQuery[A]): A = {
    val actualValues = valsForJdbc(connection, query.values)
    log.debug(s"${query.sql} with ${actualValues.mkString("(", ", ", ")")}")
    val stmt = connection.prepareStatement(query.sql)
    try {
      try {
        prepare(stmt, actualValues)
      } catch {
        case NonFatal(x) => log.errorThenThrow(s"Unable to prepare query for [${query.sql}].", x)
      }
      val results = stmt.executeQuery()
      try {
        query.handle(results)
      } catch {
        case NonFatal(x) => log.errorThenThrow(s"Unable to handle query results for [${query.sql}].", x)
      } finally {
        results.close()
      }
    } finally {
      stmt.close()
    }
  }

  def executeUpdate(connection: Connection, statement: Statement): Int = {
    val actualValues = valsForJdbc(connection, statement.values)
    log.debug(s"${statement.sql} with ${actualValues.mkString("(", ", ", ")")}")
    val stmt = connection.prepareStatement(statement.sql)
    try {
      prepare(stmt, actualValues)
    } catch {
      case NonFatal(x) =>
        stmt.close()
        log.errorThenThrow(s"Unable to prepare statement [${statement.sql}].", x)
    }
    try {
      stmt.executeUpdate()
    } catch {
      case NonFatal(x) => log.errorThenThrow(s"Unable to execute statement [${statement.sql}].", x)
    } finally {
      stmt.close()
    }
  }

  def executeUnknown[A](connection: Connection, query: Query[A], resultId: Option[UUID]): Either[A, Int] = {
    val actualValues = valsForJdbc(connection, query.values)
    log.debug(s"${query.sql} with ${actualValues.mkString("(", ", ", ")")}")
    val stmt = connection.prepareStatement(query.sql)
    try {
      try {
        prepare(stmt, actualValues)
      } catch {
        case NonFatal(x) => log.errorThenThrow(s"Unable to prepare raw query [${query.sql}].", x)
      }
      val isResultset = stmt.execute()
      if (isResultset) {
        val res = stmt.getResultSet
        try {
          Left(query.handle(res))
        } catch {
          case NonFatal(x) => log.errorThenThrow(s"Unable to handle query results for [${query.sql}].", x)
        } finally {
          res.close()
        }
      } else {
        Right(stmt.getUpdateCount)
      }
    } finally {
      stmt.close()
    }
  }
}
