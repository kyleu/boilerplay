/* Generated File */
package models.queries.customer

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.{LocalDate, ZonedDateTime}
import models.customer.CustomerRow

object CustomerRowQueries extends BaseQueries[CustomerRow]("customerRow", "customer") {
  override val fields = Seq(
    DatabaseField(title = "Customer Id", prop = "customerId", col = "customer_id", typ = IntegerType),
    DatabaseField(title = "Store Id", prop = "storeId", col = "store_id", typ = IntegerType),
    DatabaseField(title = "First Name", prop = "firstName", col = "first_name", typ = StringType),
    DatabaseField(title = "Last Name", prop = "lastName", col = "last_name", typ = StringType),
    DatabaseField(title = "Email", prop = "email", col = "email", typ = StringType),
    DatabaseField(title = "Address Id", prop = "addressId", col = "address_id", typ = IntegerType),
    DatabaseField(title = "Activebool", prop = "activebool", col = "activebool", typ = BooleanType),
    DatabaseField(title = "Create Date", prop = "createDate", col = "create_date", typ = DateType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType),
    DatabaseField(title = "Active", prop = "active", col = "active", typ = LongType)
  )
  override protected val pkColumns = Seq("customer_id")
  override protected val searchColumns = Seq("customer_id", "store_id", "first_name", "last_name", "email", "address_id")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(customerId: Int) = new GetByPrimaryKey(Seq(customerId))
  def getByPrimaryKeySeq(customerIdSeq: Seq[Int]) = new ColSeqQuery(column = "customer_id", values = customerIdSeq)

  final case class CountByAddressId(addressId: Int) extends ColCount(column = "address_id", values = Seq(addressId))
  final case class GetByAddressId(addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("address_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(addressId)
  )
  final case class GetByAddressIdSeq(addressIdSeq: Seq[Int]) extends ColSeqQuery(column = "address_id", values = addressIdSeq)

  final case class CountByCustomerId(customerId: Int) extends ColCount(column = "customer_id", values = Seq(customerId))
  final case class GetByCustomerId(customerId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("customer_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(customerId)
  )
  final case class GetByCustomerIdSeq(customerIdSeq: Seq[Int]) extends ColSeqQuery(column = "customer_id", values = customerIdSeq)

  final case class CountByEmail(email: String) extends ColCount(column = "email", values = Seq(email))
  final case class GetByEmail(email: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("email") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(email)
  )
  final case class GetByEmailSeq(emailSeq: Seq[String]) extends ColSeqQuery(column = "email", values = emailSeq)

  final case class CountByFirstName(firstName: String) extends ColCount(column = "first_name", values = Seq(firstName))
  final case class GetByFirstName(firstName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("first_name") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(firstName)
  )
  final case class GetByFirstNameSeq(firstNameSeq: Seq[String]) extends ColSeqQuery(column = "first_name", values = firstNameSeq)

  final case class CountByLastName(lastName: String) extends ColCount(column = "last_name", values = Seq(lastName))
  final case class GetByLastName(lastName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_name") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastName)
  )
  final case class GetByLastNameSeq(lastNameSeq: Seq[String]) extends ColSeqQuery(column = "last_name", values = lastNameSeq)

  final case class CountByStoreId(storeId: Int) extends ColCount(column = "store_id", values = Seq(storeId))
  final case class GetByStoreId(storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("store_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(storeId)
  )
  final case class GetByStoreIdSeq(storeIdSeq: Seq[Int]) extends ColSeqQuery(column = "store_id", values = storeIdSeq)

  def insert(model: CustomerRow) = new Insert(model)
  def insertBatch(models: Seq[CustomerRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(customerId: Int) = new RemoveByPrimaryKey(Seq[Any](customerId))

  def update(customerId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](customerId), fields)

  override def fromRow(row: Row) = CustomerRow(
    customerId = IntegerType(row, "customer_id"),
    storeId = IntegerType(row, "store_id"),
    firstName = StringType(row, "first_name"),
    lastName = StringType(row, "last_name"),
    email = StringType.opt(row, "email"),
    addressId = IntegerType(row, "address_id"),
    activebool = BooleanType(row, "activebool"),
    createDate = DateType(row, "create_date"),
    lastUpdate = TimestampZonedType.opt(row, "last_update"),
    active = LongType.opt(row, "active")
  )
}
