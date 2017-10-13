package models.queries.audit

import java.util.UUID
import models.audit.Audit
import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.queries.BaseQueries
import models.result.data.DataField
import models.result.filter.Filter

import collection.JavaConverters._

object AuditQueries extends BaseQueries[Audit]("audit", "audit", "postgres") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Act", prop = "act", col = "act", typ = StringType),
    DatabaseField(title = "App", prop = "app", col = "app", typ = StringType),
    DatabaseField(title = "Client", prop = "client", col = "client", typ = StringType),
    DatabaseField(title = "Server", prop = "server", col = "server", typ = StringType),
    DatabaseField(title = "User Id", prop = "userId", col = "user_id", typ = LongType),
    DatabaseField(title = "Company Id", prop = "companyId", col = "company_id", typ = LongType),
    DatabaseField(title = "Tags", prop = "tags", col = "tags", typ = UnknownType),
    DatabaseField(title = "Started", prop = "started", col = "started", typ = TimestampType),
    DatabaseField(title = "Completed", prop = "completed", col = "completed", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "act", "app", "client", "server", "user_id", "company_id", "tags")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

  def getByPrimaryKey(id: UUID) = GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  def insert(model: Audit) = Insert(model)
  def create(dataFields: Seq[DataField]) = CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = UpdateFields(Seq[Any](id), fields)

  override protected def fromRow(row: Row) = Audit(
    id = UuidType(row, "id"),
    act = StringType(row, "act"),
    app = StringType.opt(row, "app"),
    client = StringType.opt(row, "client"),
    server = StringType.opt(row, "server"),
    user = LongType.opt(row, "user_id"),
    company = LongType.opt(row, "company_id"),
    tags = row.as[String]("tags").asInstanceOf[java.util.HashMap[String, String]].asScala.toMap,
    started = TimestampType(row, "started"),
    completed = TimestampType(row, "completed")
  )

  override protected def toDataSeq(t: Audit) = {
    val tags = new java.util.HashMap[String, String]()
    t.tags.foreach(tag => tags.put(tag._1, tag._2))
    Seq(t.id, t.act, t.app, t.client, t.server, t.user, t.company, tags, t.started, t.completed)
  }
}
