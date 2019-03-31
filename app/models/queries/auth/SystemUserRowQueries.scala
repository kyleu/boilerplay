/* Generated File */
package models.queries.auth

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.LocalDateTime
import java.util.UUID
import models.auth.SystemUserRow

object SystemUserRowQueries extends BaseQueries[SystemUserRow]("systemUserRow", "system_user") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Username", prop = "username", col = "username", typ = StringType),
    DatabaseField(title = "Provider", prop = "provider", col = "provider", typ = StringType),
    DatabaseField(title = "Key", prop = "key", col = "key", typ = StringType),
    DatabaseField(title = "Role", prop = "role", col = "role", typ = StringType),
    DatabaseField(title = "Created", prop = "created", col = "created", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "username", "provider", "key")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(id: UUID) = new GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  final case class CountById(id: UUID) extends ColCount(column = "id", values = Seq(id))
  final case class GetById(id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(id)
  )
  final case class GetByIdSeq(idSeq: Seq[UUID]) extends ColSeqQuery(column = "id", values = idSeq)

  final case class CountByKey(key: String) extends ColCount(column = "key", values = Seq(key))
  final case class GetByKey(key: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("key") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(key)
  )
  final case class GetByKeySeq(keySeq: Seq[String]) extends ColSeqQuery(column = "key", values = keySeq)

  final case class CountByProvider(provider: String) extends ColCount(column = "provider", values = Seq(provider))
  final case class GetByProvider(provider: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("provider") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(provider)
  )
  final case class GetByProviderSeq(providerSeq: Seq[String]) extends ColSeqQuery(column = "provider", values = providerSeq)

  final case class CountByUsername(username: String) extends ColCount(column = "username", values = Seq(username))
  final case class GetByUsername(username: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("username") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(username)
  )
  final case class GetByUsernameSeq(usernameSeq: Seq[String]) extends ColSeqQuery(column = "username", values = usernameSeq)

  def insert(model: SystemUserRow) = new Insert(model)
  def insertBatch(models: Seq[SystemUserRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(id: UUID) = new RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq[Any](id), fields)

  override def fromRow(row: Row) = SystemUserRow(
    id = UuidType(row, "id"),
    username = StringType.opt(row, "username"),
    provider = StringType(row, "provider"),
    key = StringType(row, "key"),
    role = StringType(row, "role"),
    created = TimestampType(row, "created")
  )
}
