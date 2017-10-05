package models.queries

import models.database._

abstract class BaseQueries[T <: Product](val key: String, val tableName: String) extends SearchQueries[T] with MutationQueries[T] {
  def fields: Seq[DatabaseField]

  protected def pkColumns = Seq.empty[String]
  protected def searchColumns: Seq[String] = Nil
  protected def fromRow(row: Row): T

  protected def toDataSeq(t: T): Seq[Any] = t.productIterator.toSeq

  protected lazy val quotedColumns = fields.map(f => quote(f.col)).mkString(", ")
  protected def placeholdersFor(seq: Seq[_]) = seq.map(_ => "?").mkString(", ")
  protected lazy val columnPlaceholders = placeholdersFor(fields)
  protected lazy val insertSql = s"""insert into ${quote(tableName)} ($quotedColumns) values ($columnPlaceholders)"""
  protected def quote(n: String) = EngineHelper.quote(n)

  protected def updateSql(updateColumns: Seq[String], additionalUpdates: Option[String] = None) = s"""update ${quote(tableName)}
    |set ${updateColumns.map(x => s"${quote(x)} = ?").mkString(", ")}${additionalUpdates.map(x => s", $x").getOrElse("")}
    |where $pkWhereClause""".stripMargin.trim

  protected def getSql(whereClause: Option[String] = None, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Seq(
    Some(s"select $quotedColumns from ${quote(tableName)}"),
    whereClause.map(x => s" where $x"),
    orderBy.map(x => s" order by $x"),
    limit.map(x => s" limit $x"),
    offset.map(x => s" offset $x")
  ).flatten.mkString(" ")

  protected case class GetByPrimaryKey(override val values: Seq[Any]) extends FlatSingleRowQuery[T] {
    override val name = s"$key.get.by.primary.key"
    override val sql = s"""select $quotedColumns from ${quote(tableName)} where $pkWhereClause"""
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  protected class SeqQuery(
      whereClause: Option[String], orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None, override val values: Seq[Any] = Nil
  ) extends Query[Seq[T]] {
    override val name = s"$key.seq.query"
    override val sql = getSql(whereClause = whereClause, orderBy = orderBy, limit = limit, offset = offset)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected abstract class OptQuery(additionalSql: String, override val values: Seq[Any] = Nil) extends FlatSingleRowQuery[T] {
    override val name = s"$key.opt.query"
    override val sql = s"""select $quotedColumns from ${quote(tableName)} $additionalSql"""
    override def flatMap(row: Row) = Some(fromRow(row))
  }

  protected class Count(key: String, add: String, override val values: Seq[Any] = Nil) extends SingleRowQuery[Int] {
    override val name = s"$key.count." + key
    override def sql = s"select count(*) as c from ${quote(tableName)} $add".trim
    override def map(row: Row) = row.as[Long]("c").toInt
  }

  protected class ColSeqQuery(
      column: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None, override val values: Seq[Any] = Nil
  ) extends Query[Seq[T]] {
    override val name = s"$key.by.$column.seq.query"
    override val sql = getSql(whereClause = Some(quote(column) + " in (" + placeholdersFor(values) + ")"), orderBy = orderBy, limit = limit, offset = offset)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected class ColCount(column: String, values: Seq[Any] = Nil) extends Count(column, s"where ${quote(column)} in (${placeholdersFor(values)})", values) {
    override val name = s"$key.col.$column.count"
  }
}
