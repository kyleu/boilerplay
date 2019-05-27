/* Generated File */
package models.queries.store

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.store.StaffRow

object StaffRowQueries extends BaseQueries[StaffRow]("staffRow", "staff") {
  override val fields = Seq(
    DatabaseField(title = "Staff Id", prop = "staffId", col = "staff_id", typ = IntegerType),
    DatabaseField(title = "First Name", prop = "firstName", col = "first_name", typ = StringType),
    DatabaseField(title = "Last Name", prop = "lastName", col = "last_name", typ = StringType),
    DatabaseField(title = "Address Id", prop = "addressId", col = "address_id", typ = IntegerType),
    DatabaseField(title = "Email", prop = "email", col = "email", typ = StringType),
    DatabaseField(title = "Store Id", prop = "storeId", col = "store_id", typ = IntegerType),
    DatabaseField(title = "Active", prop = "active", col = "active", typ = BooleanType),
    DatabaseField(title = "Username", prop = "username", col = "username", typ = StringType),
    DatabaseField(title = "Password", prop = "password", col = "password", typ = StringType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType),
    DatabaseField(title = "Picture", prop = "picture", col = "picture", typ = ByteArrayType)
  )
  override protected val pkColumns = Seq("staff_id")
  override protected val searchColumns = Seq("staff_id", "first_name", "last_name", "address_id", "email", "store_id", "username")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(staffId: Int) = new GetByPrimaryKey(Seq(staffId))
  def getByPrimaryKeySeq(staffIdSeq: Seq[Int]) = new ColSeqQuery(column = "staff_id", values = staffIdSeq)

  final case class CountByAddressId(addressId: Int) extends ColCount(column = "address_id", values = Seq(addressId))
  final case class GetByAddressId(addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("address_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(addressId)
  )
  final case class GetByAddressIdSeq(addressIdSeq: Seq[Int]) extends ColSeqQuery(column = "address_id", values = addressIdSeq)

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

  final case class CountByStaffId(staffId: Int) extends ColCount(column = "staff_id", values = Seq(staffId))
  final case class GetByStaffId(staffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("staff_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(staffId)
  )
  final case class GetByStaffIdSeq(staffIdSeq: Seq[Int]) extends ColSeqQuery(column = "staff_id", values = staffIdSeq)

  final case class CountByStoreId(storeId: Int) extends ColCount(column = "store_id", values = Seq(storeId))
  final case class GetByStoreId(storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("store_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(storeId)
  )
  final case class GetByStoreIdSeq(storeIdSeq: Seq[Int]) extends ColSeqQuery(column = "store_id", values = storeIdSeq)

  final case class CountByUsername(username: String) extends ColCount(column = "username", values = Seq(username))
  final case class GetByUsername(username: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("username") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(username)
  )
  final case class GetByUsernameSeq(usernameSeq: Seq[String]) extends ColSeqQuery(column = "username", values = usernameSeq)

  def insert(model: StaffRow) = new Insert(model)
  def insertBatch(models: Seq[StaffRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(staffId: Int) = new RemoveByPrimaryKey(Seq[Any](staffId))

  def update(staffId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](staffId), fields)

  override def fromRow(row: Row) = StaffRow(
    staffId = IntegerType(row, "staff_id"),
    firstName = StringType(row, "first_name"),
    lastName = StringType(row, "last_name"),
    addressId = IntegerType(row, "address_id"),
    email = StringType.opt(row, "email"),
    storeId = IntegerType(row, "store_id"),
    active = BooleanType(row, "active"),
    username = StringType(row, "username"),
    password = StringType.opt(row, "password"),
    lastUpdate = TimestampZonedType(row, "last_update"),
    picture = ByteArrayType.opt(row, "picture")
  )
}
