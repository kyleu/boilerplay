package models.queries

import models.database._
import models.result.ResultFieldHelper._
import models.result.filter.Filter
import models.result.orderBy.OrderBy

trait SearchQueries[T <: Product] { this: BaseQueries[T] =>
  private[this] def searchClause(q: String) = searchColumns.map(c => s"lower(${quote(c)}::text) like ?").mkString(" or ")
  private[this] def whereClause(filters: Seq[Filter], add: Option[String] = None) = (filterClause(filters, fields), add) match {
    case (Some(fc), Some(a)) => " where (" + fc + ") and (" + a + ")"
    case (Some(fc), None) => " where " + fc
    case (None, Some(a)) => " where " + a
    case (None, None) => ""
  }

  protected def onCountAll(filters: Seq[Filter] = Nil) = new Count("all", s"${whereClause(filters)}", filters.flatMap(_.v))

  protected case class GetAll(
      filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  ) extends Query[Seq[T]] {
    override val name = s"$key.get.all"
    private[this] val (whereClause, orderBy) = {
      getResultSql(filters = filters, orderBys = orderBys, limit = limit, offset = offset, fields = fields, add = None)
    }
    override val sql = getSql(whereClause = whereClause, orderBy = orderBy, limit = limit, offset = offset)
    override val values = filters.flatMap(_.v)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected case class SearchCount(q: String, filters: Seq[Filter] = Nil) extends Count(
    key = "search",
    add = whereClause(filters, add = Some(searchClause(q))),
    values = if (q.isEmpty) { filters.flatMap(_.v) } else { filters.flatMap(_.v) ++ searchColumns.map(_ => "%" + q + "%") }
  )

  protected case class Search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends Query[List[T]] {
    override val name = s"$key.search"
    private[this] val (whereClause, orderBy) = {
      getResultSql(filters = filters, orderBys = orderBys, limit = limit, offset = offset, fields = fields, add = Some(searchClause(q)))
    }
    override val sql = getSql(whereClause = whereClause, orderBy = orderBy, limit = limit, offset = offset)
    override val values = filters.flatMap(_.v) ++ searchColumns.map(_ => "%" + q.toLowerCase + "%")
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected case class SearchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends Query[List[T]] {
    override val name = s"$key.search.exact"
    private[this] val whereClause = searchColumns.map(c => s"lower(${quote(c)}::text) = ?").mkString(" or ")
    override val sql = getSql(whereClause = Some(whereClause), orderBy = orderClause(fields, orderBys: _*), limit = limit, offset = offset)
    override val values = searchColumns.map(_ => q.toLowerCase)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }
}
