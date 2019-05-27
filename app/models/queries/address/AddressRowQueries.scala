/* Generated File */
package models.queries.address

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.address.AddressRow

object AddressRowQueries extends BaseQueries[AddressRow]("addressRow", "address") {
  override val fields = Seq(
    DatabaseField(title = "Address Id", prop = "addressId", col = "address_id", typ = IntegerType),
    DatabaseField(title = "Address", prop = "address", col = "address", typ = StringType),
    DatabaseField(title = "Address2", prop = "address2", col = "address2", typ = StringType),
    DatabaseField(title = "District", prop = "district", col = "district", typ = StringType),
    DatabaseField(title = "City Id", prop = "cityId", col = "city_id", typ = IntegerType),
    DatabaseField(title = "Postal Code", prop = "postalCode", col = "postal_code", typ = StringType),
    DatabaseField(title = "Phone", prop = "phone", col = "phone", typ = StringType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("address_id")
  override protected val searchColumns = Seq("address_id", "address", "address2", "district", "city_id", "postal_code", "phone", "last_update")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(addressId: Int) = new GetByPrimaryKey(Seq(addressId))
  def getByPrimaryKeySeq(addressIdSeq: Seq[Int]) = new ColSeqQuery(column = "address_id", values = addressIdSeq)

  final case class CountByAddress(address: String) extends ColCount(column = "address", values = Seq(address))
  final case class GetByAddress(address: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("address") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(address)
  )
  final case class GetByAddressSeq(addressSeq: Seq[String]) extends ColSeqQuery(column = "address", values = addressSeq)

  final case class CountByAddress2(address2: String) extends ColCount(column = "address2", values = Seq(address2))
  final case class GetByAddress2(address2: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("address2") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(address2)
  )
  final case class GetByAddress2Seq(address2Seq: Seq[String]) extends ColSeqQuery(column = "address2", values = address2Seq)

  final case class CountByAddressId(addressId: Int) extends ColCount(column = "address_id", values = Seq(addressId))
  final case class GetByAddressId(addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("address_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(addressId)
  )
  final case class GetByAddressIdSeq(addressIdSeq: Seq[Int]) extends ColSeqQuery(column = "address_id", values = addressIdSeq)

  final case class CountByCityId(cityId: Int) extends ColCount(column = "city_id", values = Seq(cityId))
  final case class GetByCityId(cityId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("city_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(cityId)
  )
  final case class GetByCityIdSeq(cityIdSeq: Seq[Int]) extends ColSeqQuery(column = "city_id", values = cityIdSeq)

  final case class CountByDistrict(district: String) extends ColCount(column = "district", values = Seq(district))
  final case class GetByDistrict(district: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("district") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(district)
  )
  final case class GetByDistrictSeq(districtSeq: Seq[String]) extends ColSeqQuery(column = "district", values = districtSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  final case class CountByPhone(phone: String) extends ColCount(column = "phone", values = Seq(phone))
  final case class GetByPhone(phone: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("phone") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(phone)
  )
  final case class GetByPhoneSeq(phoneSeq: Seq[String]) extends ColSeqQuery(column = "phone", values = phoneSeq)

  final case class CountByPostalCode(postalCode: String) extends ColCount(column = "postal_code", values = Seq(postalCode))
  final case class GetByPostalCode(postalCode: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("postal_code") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(postalCode)
  )
  final case class GetByPostalCodeSeq(postalCodeSeq: Seq[String]) extends ColSeqQuery(column = "postal_code", values = postalCodeSeq)

  def insert(model: AddressRow) = new Insert(model)
  def insertBatch(models: Seq[AddressRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(addressId: Int) = new RemoveByPrimaryKey(Seq[Any](addressId))

  def update(addressId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](addressId), fields)

  override def fromRow(row: Row) = AddressRow(
    addressId = IntegerType(row, "address_id"),
    address = StringType(row, "address"),
    address2 = StringType.opt(row, "address2"),
    district = StringType(row, "district"),
    cityId = IntegerType(row, "city_id"),
    postalCode = StringType.opt(row, "postal_code"),
    phone = StringType(row, "phone"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
