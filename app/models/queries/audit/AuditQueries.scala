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

  def insert(model: Audit) = new Insert(model)
  def insertBatch(models: Seq[Audit]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = new RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq[Any](id), fields)

  final case class CountByUserId(user: UUID) extends ColCount(column = "user_id", values = Seq(user))
  final case class GetByUserId(user: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends SeqQuery(
    whereClause = Some(quote("user_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(user)
  )
  final case class GetByUserIdSeq(userSeq: Seq[UUID]) extends ColSeqQuery(column = "user_id", values = userSeq)

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
    Seq[Any](t.id, t.act, t.app, t.client, t.server, t.userId, models.tag.Tag.toJavaMap(t.tags), t.msg, t.started, t.completed)
  }
}
