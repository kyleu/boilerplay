package models.database.queries.report

import java.util.UUID

import models.database.queries.BaseQueries
import models.database.{ Query, Row, SingleRowQuery }

object ReportQueries {
  private[this] def playerClause(name: String, userIds: Seq[UUID]) = if (userIds.isEmpty) {
    ""
  } else {
    s" where $name in (${userIds.map(id => "?").mkString(", ")})"
  }

  case object ListTables extends Query[List[String]] {
    override def sql = BaseQueries.trim("""
      select t.table_name as tn
      from information_schema.tables as t
      where table_catalog = 'boilerplay' and table_schema = 'public' and table_type = 'BASE TABLE'
      order by table_name
    """)
    override def reduce(rows: Iterator[Row]) = rows.map(row => row.as[String]("tn")).toList
  }

  case class CountTable(t: String) extends SingleRowQuery[(String, Long)] {
    override def sql = s"select count(*) as c from $t"
    override def map(row: Row) = t -> row.as[Long]("c")
  }

  case class GameCountForUsers(userIds: Seq[UUID]) extends Query[Map[UUID, Int]] {
    override def sql = s"select player, count(*) as c from games${playerClause("player", userIds)} group by player"
    override def values = userIds
    override def reduce(rows: Iterator[Row]) = rows.map { row =>
      UUID.fromString(row.as[String]("player")) -> row.as[Long]("c").toInt
    }.toMap
  }

  case class WinCountForUsers(userIds: Seq[UUID]) extends Query[Map[UUID, Int]] {
    override def sql = s"select player, count(*) as c from games${playerClause("player", userIds)} and status = 'win' group by player"
    override def values = userIds
    override def reduce(rows: Iterator[Row]) = rows.map { row =>
      UUID.fromString(row.as[String]("player")) -> row.as[Long]("c").toInt
    }.toMap
  }

  case class RequestCountForUsers(userIds: Seq[UUID]) extends Query[Map[UUID, Int]] {
    override def sql = s"select user_id, count(*) as c from requests${playerClause("user_id", userIds)} group by user_id"
    override def values = userIds
    override def reduce(rows: Iterator[Row]) = rows.map { row =>
      UUID.fromString(row.as[String]("user_id")) -> row.as[Long]("c").toInt
    }.toMap
  }
}
