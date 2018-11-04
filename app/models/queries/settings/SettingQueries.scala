/* Generated File */
package models.queries.settings

import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.queries.{BaseQueries, ResultFieldHelper}
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.settings.{Setting, SettingKey}

object SettingQueries extends BaseQueries[Setting]("setting", "setting_values") {
  override val fields = Seq(
    DatabaseField(title = "K", prop = "k", col = "k", typ = EnumType(SettingKey)),
    DatabaseField(title = "V", prop = "v", col = "v", typ = StringType)
  )
  override protected val pkColumns = Seq("k")
  override protected val searchColumns = Seq("k")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(k: SettingKey) = new GetByPrimaryKey(Seq(k))
  def getByPrimaryKeySeq(kSeq: Seq[SettingKey]) = new ColSeqQuery(column = "k", values = kSeq)

  final case class CountByK(k: SettingKey) extends ColCount(column = "k", values = Seq(k))
  final case class GetByK(k: SettingKey, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("k") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(k)
  )
  final case class GetByKSeq(kSeq: Seq[SettingKey]) extends ColSeqQuery(column = "k", values = kSeq)

  def insert(model: Setting) = new Insert(model)
  def insertBatch(models: Seq[Setting]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def removeByPrimaryKey(k: SettingKey) = new RemoveByPrimaryKey(Seq[Any](k))

  def update(k: SettingKey, fields: Seq[DataField]) = new UpdateFields(Seq[Any](k), fields)

  override def fromRow(row: Row) = Setting(
    k = EnumType(SettingKey)(row, "k"),
    v = StringType(row, "v")
  )
}
