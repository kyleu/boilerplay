/* Generated File */
package models.queries.address

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.address.CityRow

object CityRowQueries extends BaseQueries[CityRow]("cityRow", "city") {
  override val fields = Seq(
    DatabaseField(title = "City Id", prop = "cityId", col = "city_id", typ = IntegerType),
    DatabaseField(title = "City", prop = "city", col = "city", typ = StringType),
    DatabaseField(title = "Country Id", prop = "countryId", col = "country_id", typ = IntegerType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("city_id")
  override protected val searchColumns = Seq("city_id", "city", "country_id")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(cityId: Int) = new GetByPrimaryKey(Seq(cityId))
  def getByPrimaryKeySeq(cityIdSeq: Seq[Int]) = new ColSeqQuery(column = "city_id", values = cityIdSeq)

  final case class CountByCity(city: String) extends ColCount(column = "city", values = Seq(city))
  final case class GetByCity(city: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("city") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(city)
  )
  final case class GetByCitySeq(citySeq: Seq[String]) extends ColSeqQuery(column = "city", values = citySeq)

  final case class CountByCityId(cityId: Int) extends ColCount(column = "city_id", values = Seq(cityId))
  final case class GetByCityId(cityId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("city_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(cityId)
  )
  final case class GetByCityIdSeq(cityIdSeq: Seq[Int]) extends ColSeqQuery(column = "city_id", values = cityIdSeq)

  final case class CountByCountryId(countryId: Int) extends ColCount(column = "country_id", values = Seq(countryId))
  final case class GetByCountryId(countryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("country_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(countryId)
  )
  final case class GetByCountryIdSeq(countryIdSeq: Seq[Int]) extends ColSeqQuery(column = "country_id", values = countryIdSeq)

  def insert(model: CityRow) = new Insert(model)
  def insertBatch(models: Seq[CityRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(cityId: Int) = new RemoveByPrimaryKey(Seq[Any](cityId))

  def update(cityId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](cityId), fields)

  override def fromRow(row: Row) = CityRow(
    cityId = IntegerType(row, "city_id"),
    city = StringType(row, "city"),
    countryId = IntegerType(row, "country_id"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
