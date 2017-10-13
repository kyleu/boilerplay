package models.queries.auth

import java.util.UUID

import models.database._
import models.queries.EngineHelper.quote

object UserSearchQueries {
  private[this] val tableName = UserQueries.tableName

  case class IsUsernameInUse(name: String) extends SingleRowQuery[Boolean] {
    override val sql = s"""select count(*) as c from ${quote(tableName, "mysql")} where ${quote("username", "mysql")} = ?"""
    override val values = Seq(name)
    override def map(row: Row) = row.as[Long]("c") != 0L
  }

  case class GetUsername(id: UUID) extends Query[Option[String]] {
    override val name = s"user.search.get.username"
    override val sql = s"""select ${quote("username", "mysql")} from ${quote(tableName, "mysql")} where ${quote("id", "mysql")} = ?"""
    override val values = Seq(id)
    override def reduce(rows: Iterator[Row]) = rows.toSeq.headOption.map(_.as[String]("username"))
  }

  case class GetUsernameSeq(ids: Set[UUID]) extends Query[Map[UUID, String]] {
    override val name = s"user.search.get.username.seq"
    private[this] val idClause = ids.map("'" + _ + "'").mkString(", ")
    override val sql = s"""select ${quote("id", "mysql")}, ${quote("username", "mysql")}
      |from ${quote(tableName, "mysql")}
      |where ${quote("id", "mysql")} in ($idClause)
      |""".stripMargin.trim
    override def reduce(rows: Iterator[Row]) = rows.map(r => r.as[UUID]("id") -> r.as[String]("username")).toMap
  }

  case object CountAdmins extends SingleRowQuery[Int]() {
    override val name = s"user.search.count.admins"
    override val sql = s"select count(*) as c from ${quote(tableName, "mysql")} where ${quote("role", "mysql")} = 'admin'"
    override def map(row: Row) = row.as[Long]("c").toInt
  }
}
