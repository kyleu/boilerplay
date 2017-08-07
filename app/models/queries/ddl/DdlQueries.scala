package models.queries.ddl

import models.database.Row
import models.ddl.Ddl
import models.queries.BaseQueries

object DdlQueries extends BaseQueries[Ddl] {
  override protected val tableName = "ddl"
  override protected val columns = Seq("id", "name", "sql", "applied")
  override protected val idColumns = Seq("id")
  override protected val searchColumns = Seq("id")

  def getById(id: Int) = GetById(Seq(id))
  def getByIdSeq(idSeq: Seq[Int]) = new ColSeqQuery("id", idSeq)

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = GetAll(orderBy, limit, offset)

  def searchCount(q: String) = SearchCount(q)
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Search(q, orderBy, limit, offset)

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = SearchExact(q, orderBy, limit, offset)

  def insert(model: Ddl) = Insert(model)
  def removeById(id: Int) = RemoveById(Seq(id))

  override protected def fromRow(row: Row) = Ddl(
    id = row.as[Int]("id"),
    name = row.as[String]("name"),
    sql = row.as[String]("sql"),
    applied = fromJoda(row.as[org.joda.time.LocalDateTime]("applied"))
  )
  override protected def toDataSeq(o: Ddl) = Seq[Any](
    o.id, o.name, o.sql, toJoda(o.applied)
  )
}
