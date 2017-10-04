package models.database.jdbc

import java.sql.{Connection, PreparedStatement, Types}
import java.util.UUID

import models.database.{Query, RawQuery, Statement}
import util.{Logging, NullUtils}

import scala.annotation.tailrec
import scala.concurrent.Future

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

  def apply[A](connection: Connection, query: RawQuery[A]): Future[A] = {
    log.debug(s"${query.sql} with ${query.values.mkString("(", ", ", ")")}")
    val stmt = connection.prepareStatement(query.sql)
    try {
      prepare(stmt, query.values)
      val results = stmt.executeQuery()
      try {
        Future.successful(query.handle(results))
      } finally {
        results.close()
      }
    } finally {
      stmt.close()
    }
  }

  def executeUpdate(connection: Connection, statement: Statement): Future[Int] = {
    log.debug(s"${statement.sql} with ${statement.values.mkString("(", ", ", ")")}")
    val stmt = connection.prepareStatement(statement.sql)
    try {
      prepare(stmt, statement.values)
      Future.successful(stmt.executeUpdate())
    } finally {
      stmt.close()
    }
  }

  def executeUnknown[A](connection: Connection, query: Query[A], resultId: Option[UUID]): Future[Either[A, Int]] = {
    log.debug(s"${query.sql} with ${query.values.mkString("(", ", ", ")")}")
    val stmt = connection.prepareStatement(query.sql)
    try {
      prepare(stmt, query.values)
      val isResultset = stmt.execute()
      if (isResultset) {
        val res = stmt.getResultSet
        Future.successful(Left(query.handle(res)))
      } else {
        Future.successful(Right(stmt.getUpdateCount))
      }
    } finally {
      stmt.close()
    }
  }
}
