/* Generated File */
package models.queries.audit

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID
import models.audit.AuditRow

object AuditRowQueries extends BaseQueries[AuditRow]("auditRow", "audit") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Act", prop = "act", col = "act", typ = StringType),
    DatabaseField(title = "App", prop = "app", col = "app", typ = StringType),
    DatabaseField(title = "Client", prop = "client", col = "client", typ = StringType),
    DatabaseField(title = "Server", prop = "server", col = "server", typ = StringType),
    DatabaseField(title = "User Id", prop = "userId", col = "user_id", typ = UuidType),
    DatabaseField(title = "Tags", prop = "tags", col = "tags", typ = JsonType),
    DatabaseField(title = "Msg", prop = "msg", col = "msg", typ = StringType),
    DatabaseField(title = "Started", prop = "started", col = "started", typ = TimestampType),
    DatabaseField(title = "Completed", prop = "completed", col = "completed", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "act", "app", "client", "server", "user_id")

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

  final case class CountByAct(act: String) extends ColCount(column = "act", values = Seq(act))
  final case class GetByAct(act: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("act") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(act)
  )
  final case class GetByActSeq(actSeq: Seq[String]) extends ColSeqQuery(column = "act", values = actSeq)

  final case class CountByApp(app: String) extends ColCount(column = "app", values = Seq(app))
  final case class GetByApp(app: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("app") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(app)
  )
  final case class GetByAppSeq(appSeq: Seq[String]) extends ColSeqQuery(column = "app", values = appSeq)

  final case class CountByClient(client: String) extends ColCount(column = "client", values = Seq(client))
  final case class GetByClient(client: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("client") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(client)
  )
  final case class GetByClientSeq(clientSeq: Seq[String]) extends ColSeqQuery(column = "client", values = clientSeq)

  final case class CountById(id: UUID) extends ColCount(column = "id", values = Seq(id))
  final case class GetById(id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(id)
  )
  final case class GetByIdSeq(idSeq: Seq[UUID]) extends ColSeqQuery(column = "id", values = idSeq)

  final case class CountByServer(server: String) extends ColCount(column = "server", values = Seq(server))
  final case class GetByServer(server: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("server") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(server)
  )
  final case class GetByServerSeq(serverSeq: Seq[String]) extends ColSeqQuery(column = "server", values = serverSeq)

  final case class CountByUserId(userId: UUID) extends ColCount(column = "user_id", values = Seq(userId))
  final case class GetByUserId(userId: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("user_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(userId)
  )
  final case class GetByUserIdSeq(userIdSeq: Seq[UUID]) extends ColSeqQuery(column = "user_id", values = userIdSeq)

  def insert(model: AuditRow) = new Insert(model)
  def insertBatch(models: Seq[AuditRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(id: UUID) = new RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq[Any](id), fields)

  override def fromRow(row: Row) = AuditRow(
    id = UuidType(row, "id"),
    act = StringType(row, "act"),
    app = StringType(row, "app"),
    client = StringType(row, "client"),
    server = StringType(row, "server"),
    userId = UuidType(row, "user_id"),
    tags = JsonType(row, "tags"),
    msg = StringType(row, "msg"),
    started = TimestampType(row, "started"),
    completed = TimestampType(row, "completed")
  )
}
