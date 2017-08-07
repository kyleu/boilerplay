package models.queries

import models.SettingValues
import models.database.Row

object SettingValuesQueries extends BaseQueries[SettingValues] {
  override protected val tableName = "setting_values"
  override protected val columns = Seq("k", "v")
  override protected val idColumns = Seq("k")
  override protected val searchColumns = Seq("k")

  def getById(k: String) = GetById(Seq(k))
  def getByIdSeq(kSeq: Seq[String]) = new ColSeqQuery("k", kSeq)

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = GetAll(orderBy, limit, offset)

  def searchCount(q: String) = SearchCount(q)
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Search(q, orderBy, limit, offset)

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = SearchExact(q, orderBy, limit, offset)

  def insert(model: SettingValues) = Insert(model)
  def removeById(k: String) = RemoveById(Seq(k))

  override protected def fromRow(row: Row) = SettingValues(
    k = row.as[String]("k"),
    v = row.as[String]("v")
  )
  override protected def toDataSeq(o: SettingValues) = Seq[Any](
    o.k, o.v
  )
}
