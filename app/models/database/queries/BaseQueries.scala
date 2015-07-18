package models.database.queries

import models.database.{ Row, FlatSingleRowQuery, Statement, Query }
import utils.Config

object BaseQueries {
  def trim(s: String) = s.replaceAll("""[\s]+""", " ").trim
}

trait BaseQueries[T] {
  protected def tableName: String = "_invalid_"
  protected def idColumns = Seq("id")
  protected def columns: Seq[String]
  protected def searchColumns: Seq[String]

  protected def fromRow(row: Row): T
  protected def toDataSeq(t: T): Seq[Any]

  protected lazy val insertSql = s"insert into $tableName (${columns.mkString(", ")}) values (${columns.map(x => "?").mkString(", ")})"

  protected def updateSql(updateColumns: Seq[String], additionalUpdates: Option[String] = None) = BaseQueries.trim(s"""
    update $tableName set ${updateColumns.map(x => s"$x = ?").mkString(", ")}${additionalUpdates.map(x => s", $x").getOrElse("")} where $idWhereClause
  """)

  protected def getSql(
    whereClause: Option[String] = None, groupBy: Option[String] = None, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None
  ) = BaseQueries.trim(s"""
    select ${columns.mkString(", ")} from $tableName
    ${whereClause.map(x => s" where $x").getOrElse("")}
    ${groupBy.map(x => s" group by $x").getOrElse("")}
    ${orderBy.map(x => s" order by $x").getOrElse("")}
    ${limit.map(x => s" limit $x").getOrElse("")}
    ${offset.map(x => s" offset $x").getOrElse("")}
  """)

  protected case class GetById(override val values: Seq[Any]) extends FlatSingleRowQuery[T] {
    override val sql = s"select ${columns.mkString(", ")} from $tableName where $idWhereClause"
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  protected case class Insert(model: T) extends Statement {
    override val sql = insertSql
    override val values = toDataSeq(model)
  }

  protected case class InsertBatch(models: Seq[T]) extends Statement {
    private[this] val valuesClause = models.map(m => s"(${columns.map(x => "?").mkString(", ")})").mkString(", ")
    override val sql = s"insert into $tableName (${columns.mkString(", ")}) values $valuesClause"
    override val values = models.flatMap(toDataSeq)
  }

  protected case class RemoveById(override val values: Seq[Any]) extends Statement {
    override val sql = s"delete from $tableName where $idWhereClause"
  }

  protected case class Count(override val sql: String, override val values: Seq[Any]) extends Query[Int] {
    override def reduce(rows: Iterator[Row]) = rows.next().as[Long]("c").toInt
  }

  protected class SearchCount(q: String, groupBy: Option[String] = None) extends Count(sql = {
    val searchWhere = if (q.isEmpty) { "" } else { "where " + searchColumns.map(c => s"lower($c) like lower(?)").mkString(" or ") }
    s"select count(*) as c from $tableName $searchWhere ${groupBy.map(x => s" group by $x").getOrElse("")}"
  }, values = if (q.isEmpty) { Seq.empty } else { searchColumns.map(c => s"%$q%") })

  protected case class Search(q: String, orderBy: String, page: Option[Int], /* TODO use */ groupBy: Option[String] = None) extends Query[List[T]] {
    private[this] val whereClause = if (q.isEmpty) { None } else { Some(searchColumns.map(c => s"lower($c) like lower(?)").mkString(" or ")) }
    private[this] val limit = page.map(x => Config.pageSize)
    private[this] val offset = page.map(x => x * Config.pageSize)
    override val sql = getSql(whereClause, groupBy, Some(orderBy), limit, offset)
    override val values = if (q.isEmpty) { Seq.empty } else { searchColumns.map(c => s"%$q%") }
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  private def idWhereClause = idColumns.map(c => s"$c = ?").mkString(" and ")
}
