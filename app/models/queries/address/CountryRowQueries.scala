/* Generated File */
package models.queries.address

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.address.CountryRow

object CountryRowQueries extends BaseQueries[CountryRow]("countryRow", "country") {
  override val fields = Seq(
    DatabaseField(title = "Country Id", prop = "countryId", col = "country_id", typ = IntegerType),
    DatabaseField(title = "Country", prop = "country", col = "country", typ = StringType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("country_id")
  override protected val searchColumns = Seq("country_id", "country", "last_update")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(countryId: Int) = new GetByPrimaryKey(Seq(countryId))
  def getByPrimaryKeySeq(countryIdSeq: Seq[Int]) = new ColSeqQuery(column = "country_id", values = countryIdSeq)

  final case class CountByCountry(country: String) extends ColCount(column = "country", values = Seq(country))
  final case class GetByCountry(country: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("country") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(country)
  )
  final case class GetByCountrySeq(countrySeq: Seq[String]) extends ColSeqQuery(column = "country", values = countrySeq)

  final case class CountByCountryId(countryId: Int) extends ColCount(column = "country_id", values = Seq(countryId))
  final case class GetByCountryId(countryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("country_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(countryId)
  )
  final case class GetByCountryIdSeq(countryIdSeq: Seq[Int]) extends ColSeqQuery(column = "country_id", values = countryIdSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  def insert(model: CountryRow) = new Insert(model)
  def insertBatch(models: Seq[CountryRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(countryId: Int) = new RemoveByPrimaryKey(Seq[Any](countryId))

  def update(countryId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](countryId), fields)

  override def fromRow(row: Row) = CountryRow(
    countryId = IntegerType(row, "country_id"),
    country = StringType(row, "country"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
