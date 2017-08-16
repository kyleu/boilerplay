package models.queries.auth

import java.util.UUID

import models.database._

object UserSearchQueries {
  case class IsUsernameInUse(name: String) extends SingleRowQuery[Boolean] {
    override val sql = s"""select count(*) as c from "${UserQueries.tableName}" where "username" = ?"""
    override val values = Seq(name)
    override def map(row: Row) = row.as[Long]("c") != 0L
  }

  case class GetUsername(id: UUID) extends Query[Option[String]] {
    override val sql = s"""select "username" from "${UserQueries.tableName}" where "id" = ?"""
    override val values = Seq(id)
    override def reduce(rows: Iterator[Row]) = rows.toSeq.headOption.map(_.as[String]("username"))
  }

  case class GetUsernames(ids: Set[UUID]) extends Query[Map[UUID, String]] {
    private[this] val idClause = ids.map(id => s"'$id'").mkString(", ")
    override val sql = s"""select "id", "username" from "${UserQueries.tableName}" where "id" in ($idClause)"""
    override def reduce(rows: Iterator[Row]) = rows.map(r => r.as[UUID]("id") -> r.as[String]("username")).toMap
  }

  case object CountAdmins extends SingleRowQuery[Int]() {
    override val sql = "select count(*) as c from \"users\" where \"role\" = 'admin'"
    override def map(row: Row) = row.as[Long]("c").toInt
  }
}
