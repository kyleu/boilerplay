package models.queries

import models.database._

object BaseQueries {
  def trim(s: String) = s.replaceAll("""[\s]+""", " ").trim
}

trait BaseQueries[T] extends JodaDateUtils {
  protected def tableName: String = "_invalid_"
  protected def idColumns = Seq("id")

  protected def columns: Seq[String]
  protected def searchColumns: Seq[String] = Nil

  protected def fromRow(row: Row): T
  protected def toDataSeq(t: T): Seq[Any]

  protected lazy val quotedColumns = columns.map("\"" + _ + "\"").mkString(", ")
  protected def placeholdersFor(seq: Seq[_]) = seq.map(_ => "?").mkString(", ")
  protected lazy val columnPlaceholders = placeholdersFor(columns)
  protected lazy val insertSql = s"""insert into "$tableName" ($quotedColumns) values ($columnPlaceholders)"""

  protected def updateSql(updateColumns: Seq[String], additionalUpdates: Option[String] = None) = BaseQueries.trim(s"""
    update "$tableName" set ${updateColumns.map(x => s"$x = ?").mkString(", ")}${additionalUpdates.map(x => s", $x").getOrElse("")} where $idWhereClause
  """)

  protected def getSql(
    whereClause: Option[String] = None,
    groupBy: Option[String] = None,
    orderBy: Option[String] = None,
    limit: Option[Int] = None,
    offset: Option[Int] = None
  ) = BaseQueries.trim(s"""
    select $quotedColumns from "$tableName"
    ${whereClause.map(x => s" where $x").getOrElse("")}
    ${groupBy.map(x => s" group by $x").getOrElse("")}
    ${orderBy.map(x => s" order by $x").getOrElse("")}
    ${limit.map(x => s" limit $x").getOrElse("")}
    ${offset.map(x => s" offset $x").getOrElse("")}
  """)

  protected case class GetById(override val values: Seq[Any]) extends FlatSingleRowQuery[T] {
    override val sql = s"""select $quotedColumns from "$tableName" where $idWhereClause"""
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  protected case class GetAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) extends Query[Seq[T]] {
    override val sql = getSql(orderBy = orderBy, limit = limit, offset = offset)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected case class Insert(model: T) extends Statement {
    override val sql = insertSql
    override val values: Seq[Any] = toDataSeq(model)
  }

  protected case class InsertBatch(models: Seq[T]) extends Statement {
    private[this] val valuesClause = models.map(_ => s"($columnPlaceholders)").mkString(", ")
    override val sql = s"""insert into "$tableName" ($quotedColumns) values $valuesClause"""
    override val values: Seq[Any] = models.flatMap(toDataSeq)
  }

  protected case class RemoveById(override val values: Seq[Any]) extends Statement {
    override val sql = s"""delete from "$tableName" where $idWhereClause"""
  }

  protected class SeqQuery(additionalSql: String, override val values: Seq[Any] = Nil) extends Query[Seq[T]] {
    override val sql = s"""select $quotedColumns from "$tableName" $additionalSql"""
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected class ColSeqQuery(column: String, vals: Seq[Any] = Nil) extends SeqQuery("where \"" + column + "\" in (" + placeholdersFor(vals) + ")", vals)

  protected abstract class OptQuery(additionalSql: String, override val values: Seq[Any] = Nil) extends FlatSingleRowQuery[T] {
    override val sql = s"""select $quotedColumns from "$tableName" $additionalSql"""
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  protected class Count(override val sql: String, override val values: Seq[Any] = Nil) extends SingleRowQuery[Int] {
    override def map(row: Row) = row.as[Long]("c").toInt
  }

  protected case class SearchCount(q: String, groupBy: Option[String] = None) extends Count(sql = {
    val searchWhere = if (q.isEmpty) { "" } else { "where " + searchColumns.map(c => s"""lower("$c") like lower(?)""").mkString(" or ") }
    s"""select count(*) as c from "$tableName" $searchWhere ${groupBy.map(x => s" group by $x").getOrElse("")}"""
  }, values = if (q.isEmpty) { Seq.empty[Any] } else { searchColumns.map(_ => "%" + q + "%") })

  protected case class Search(q: String, orderBy: Option[String], limit: Option[Int], offset: Option[Int]) extends Query[List[T]] {
    private[this] val whereClause = if (q.isEmpty) { None } else { Some(searchColumns.map(c => s"""lower($c) like ?""").mkString(" or ")) }
    override val sql = getSql(whereClause, None, orderBy, limit, offset)
    override val values = if (q.isEmpty) { Seq.empty } else { searchColumns.map(_ => "%" + q.toLowerCase + "%") }
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  private def idWhereClause = idColumns.map(c => s""""$c" = ?""").mkString(" and ")
}
