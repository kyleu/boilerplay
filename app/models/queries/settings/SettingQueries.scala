/* Generated File */
package models.queries.settings

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import models.settings.{Setting, SettingKeyType}

object SettingQueries extends BaseQueries[Setting]("setting", "setting_values") {
  override val fields = Seq(
    DatabaseField(title = "K", prop = "k", col = "k", typ = EnumType(SettingKeyType)),
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

  def getByPrimaryKey(k: SettingKeyType) = new GetByPrimaryKey(Seq(k))
  def getByPrimaryKeySeq(kSeq: Seq[SettingKeyType]) = new ColSeqQuery(column = "k", values = kSeq)

  final case class CountByK(k: SettingKeyType) extends ColCount(column = "k", values = Seq(k))
  final case class GetByK(k: SettingKeyType, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("k") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(k)
  )
  final case class GetByKSeq(kSeq: Seq[SettingKeyType]) extends ColSeqQuery(column = "k", values = kSeq)

  def insert(model: Setting) = new Insert(model)
  def insertBatch(models: Seq[Setting]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(k: SettingKeyType) = new RemoveByPrimaryKey(Seq[Any](k))

  def update(k: SettingKeyType, fields: Seq[DataField]) = new UpdateFields(Seq[Any](k), fields)

  override def fromRow(row: Row) = Setting(
    k = EnumType(SettingKeyType)(row, "k"),
    v = StringType(row, "v")
  )
}
