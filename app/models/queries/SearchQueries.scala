package models.queries

import models.database._
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.result.ResultFieldHelper._

trait SearchQueries[T <: Product] { this: BaseQueries[T] =>
  private[this] def searchCol(c: String) = s"${quote(c)}::text like ?"

  private[this] def searchClause(q: Option[String]) = q.map(_ => searchColumns.map(searchCol).mkString(" or "))
  private[this] def whereClause(filters: Seq[Filter], add: Option[String] = None) = (filterClause(filters, fields), add) match {
    case (Some(fc), Some(a)) => " where (" + fc + ") and (" + a + ")"
    case (Some(fc), None) => " where " + fc
    case (None, Some(a)) => " where " + a
    case (None, None) => ""
  }

  protected def onCountAll(filters: Seq[Filter] = Nil) = new Count("all", s"${whereClause(filters)}", filters.flatMap(_.v))

  protected class GetAll(
      filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  ) extends Query[Seq[T]] {
    override val name = s"$key.get.all"
    private[this] val (whereClause, orderBy) = {
      getResultSql(filters = filters, orderBys = orderBys, fields = fields, add = None)
    }
    override val sql = getSql(whereClause = whereClause, orderBy = orderBy, limit = limit, offset = offset)
    override val values = filters.flatMap(_.v)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected class SearchCount(q: Option[String], filters: Seq[Filter] = Nil) extends Count(
    key = "search",
    add = whereClause(filters, add = searchClause(q)),
    values = filters.flatMap(_.v) ++ q.map(query => searchColumns.map(_ => "%" + query.toLowerCase + "%")).getOrElse(Seq.empty)
  )

  protected class Search(
      q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  ) extends Query[List[T]] {
    override val name = s"$key.search"
    private[this] val (whereClause, orderBy) = {
      getResultSql(filters = filters, orderBys = orderBys, fields = fields, add = searchClause(q))
    }
    override val sql = getSql(whereClause = whereClause, orderBy = orderBy, limit = limit, offset = offset)
    override val values = filters.flatMap(_.v) ++ q.map(query => searchColumns.map(_ => "%" + query.toLowerCase + "%")).getOrElse(Seq.empty)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  protected class SearchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends Query[List[T]] {
    override val name = s"$key.search.exact"
    private[this] val whereClause = searchColumns.map(searchCol).mkString(" or ")
    override val sql = getSql(whereClause = Some(whereClause), orderBy = orderClause(fields, orderBys: _*), limit = limit, offset = offset)
    override val values = searchColumns.map(_ => q.toLowerCase)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }
}
