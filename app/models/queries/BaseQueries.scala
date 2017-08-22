package models.queries

import models.database._
import util.JodaDateUtils

object BaseQueries {
  val (leftQuote, rightQuote) = ("\"", "\"")
  def trim(s: String) = s.replaceAll("""[\s]+""", " ").trim
}

abstract class BaseQueries[T](val key: String, val tableName: String) extends SearchQueries[T] with MutationQueries[T] with JodaDateUtils {
  def fields: Seq[DatabaseField]

  protected def pkColumns = Seq("id")
  protected def searchColumns: Seq[String] = Nil
  protected def fromRow(row: Row): T
  protected def toDataSeq(t: T): Seq[Any]

  protected def quote(n: String) = BaseQueries.leftQuote + n + BaseQueries.rightQuote

  protected lazy val quotedColumns = fields.map(f => quote(f.col)).mkString(", ")
  protected def placeholdersFor(seq: Seq[_]) = seq.map(_ => "?").mkString(", ")
  protected lazy val columnPlaceholders = placeholdersFor(fields)
  protected lazy val insertSql = s"""insert into ${quote(tableName)} ($quotedColumns) values ($columnPlaceholders)"""

  protected def updateSql(updateColumns: Seq[String], additionalUpdates: Option[String] = None) = BaseQueries.trim(s"""
    update ${quote(tableName)} set ${updateColumns.map(x => s"${quote(x)} = ?").mkString(", ")}${additionalUpdates.map(x => s", $x").getOrElse("")} where $pkWhereClause
  """)

  protected def getSql(whereClause: Option[String] = None, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    BaseQueries.trim(s"""
      select $quotedColumns from ${quote(tableName)}
      ${whereClause.map(x => s" where $x").getOrElse("")}
      ${orderBy.map(x => s" order by $x").getOrElse("")}
      ${limit.map(x => s" limit $x").getOrElse("")}
      ${offset.map(x => s" offset $x").getOrElse("")}
    """)
  }

  protected case class GetByPrimaryKey(override val values: Seq[Any]) extends FlatSingleRowQuery[T] {
    override val name = s"$key.get.by.primary.key"
    override val sql = s"""select $quotedColumns from ${quote(tableName)} where $pkWhereClause"""
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  protected class SeqQuery(additionalSql: String, override val values: Seq[Any] = Nil) extends Query[Seq[T]] {
    override val name = s"$key.seq.query"
    override val sql = s"""select $quotedColumns from ${quote(tableName)} $additionalSql"""
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected class ColSeqQuery(column: String, vals: Seq[Any] = Nil) extends SeqQuery(s"where ${quote(column)} in (${placeholdersFor(vals)})", vals) {
    override val name = s"$key.col.seq.query.$column"
  }

  protected abstract class OptQuery(additionalSql: String, override val values: Seq[Any] = Nil) extends FlatSingleRowQuery[T] {
    override val name = s"$key.opt.query"
    override val sql = s"""select $quotedColumns from ${quote(tableName)} $additionalSql"""
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  protected class Count(override val sql: String, override val values: Seq[Any] = Nil) extends SingleRowQuery[Int] {
    override val name = s"$key.count"
    override def map(row: Row) = row.as[Long]("c").toInt
  }
}
