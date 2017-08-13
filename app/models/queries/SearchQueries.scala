package models.queries

import models.database._
import models.result.ResultFieldHelper._
import models.result.filter.Filter
import models.result.orderBy.OrderBy

trait SearchQueries[T] { this: BaseQueries[T] =>
  protected def onCountAll(filters: Seq[Filter] = Nil) = {
    new Count(sql = "select count(*) as c from \"" + tableName + "\"" + filterClause(filters, fields).getOrElse(""), values = filters.flatMap(_.v))
  }

  private[this] def searchClause(q: String) = searchColumns.map(c => s"""lower("$c"::text) like ?""").mkString(" or ")

  protected case class GetAll(
      filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  ) extends Query[Seq[T]] {
    private[this] val (whereClause, orderBy) = {
      getResultSql(filters = filters, orderBys = orderBys, limit = limit, offset = offset, fields = fields, add = None)
    }
    override val sql = getSql(whereClause = whereClause, orderBy = orderBy, limit = limit, offset = offset)
    override val values = filters.flatMap(_.v)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected case class SearchCount(q: String, filter: Seq[Filter] = Nil) extends Count(
    sql = {
    val f = filterClause(filters = filter, fields = fields, add = Some(searchClause(q))).map(" where " + _).getOrElse("")
    s"""select count(*) as c from "$tableName"$f"""
  },
    values = if (q.isEmpty) { filter.flatMap(_.v) } else { filter.flatMap(_.v) ++ searchColumns.map(_ => "%" + q + "%") }
  )

  protected case class Search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends Query[List[T]] {
    private[this] val (whereClause, orderBy) = {
      getResultSql(filters = filters, orderBys = orderBys, limit = limit, offset = offset, fields = fields, add = Some(searchClause(q)))
    }
    override val sql = getSql(whereClause = whereClause, orderBy = orderBy, limit = limit, offset = offset)
    override val values = filters.flatMap(_.v) ++ searchColumns.map(_ => "%" + q.toLowerCase + "%")
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected case class SearchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends Query[List[T]] {
    private[this] val whereClause = searchColumns.map(c => s"""lower("$c"::text) = ?""").mkString(" or ")
    override val sql = getSql(whereClause = Some(whereClause), orderBy = orderClause(orderBys, fields), limit = limit, offset = offset)
    override val values = searchColumns.map(_ => q.toLowerCase)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }
}
