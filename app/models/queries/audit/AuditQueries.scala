package models.queries.audit

import java.util.UUID

import models.audit.Audit
import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.queries.BaseQueries
import models.result.ResultFieldHelper
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.tag.Tag

import scala.collection.JavaConverters._

object AuditQueries extends BaseQueries[Audit]("audit", "audit") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Act", prop = "act", col = "act", typ = StringType),
    DatabaseField(title = "App", prop = "app", col = "app", typ = StringType),
    DatabaseField(title = "Client", prop = "client", col = "client", typ = StringType),
    DatabaseField(title = "Server", prop = "server", col = "server", typ = StringType),
    DatabaseField(title = "User Id", prop = "userId", col = "user_id", typ = UuidType),
    DatabaseField(title = "Tags", prop = "tags", col = "tags", typ = UnknownType),
    DatabaseField(title = "Message", prop = "msg", col = "msg", typ = UnknownType),
    DatabaseField(title = "Started", prop = "started", col = "started", typ = TimestampType),
    DatabaseField(title = "Completed", prop = "completed", col = "completed", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "act", "app", "client", "server", "user_id", "tags")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

  def getByPrimaryKey(id: UUID) = GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  def insert(model: Audit) = Insert(model)
  def insertBatch(models: Seq[Audit]) = InsertBatch(models)
  def create(dataFields: Seq[DataField]) = CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = UpdateFields(Seq[Any](id), fields)

  case class CountByUserId(user: UUID) extends ColCount(column = "user_id", values = Seq(user))
  case class GetByUserId(user: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends SeqQuery(
    whereClause = Some(quote("user_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(user)
  )
  case class GetByUserIdSeq(userSeq: Seq[UUID]) extends ColSeqQuery(column = "user_id", values = userSeq)

  override protected def fromRow(row: Row) = Audit(
    id = UuidType(row, "id"),
    act = StringType(row, "act"),
    app = StringType(row, "app"),
    client = StringType(row, "client"),
    server = StringType(row, "server"),
    userId = UuidType(row, "user_id"),
    tags = Tag.fromJavaMap(row.as[Any]("tags").asInstanceOf[java.util.HashMap[String, String]]),
    msg = StringType(row, "msg"),
    started = TimestampType(row, "started"),
    completed = TimestampType(row, "completed")
  )

  override protected def toDataSeq(t: Audit) = {
    Seq(t.id, t.act, t.app, t.client, t.server, t.userId, models.tag.Tag.toJavaMap(t.tags), t.msg, t.started, t.completed)
  }
}
