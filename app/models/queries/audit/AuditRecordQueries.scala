package models.queries.audit

import java.util.UUID

import models.audit.{AuditField, AuditRecord}
import models.database.{DatabaseField, Row, Statement}
import models.database.DatabaseFieldType._
import models.queries.BaseQueries
import models.result.ResultFieldHelper
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import org.postgresql.jdbc.PgArray
import org.postgresql.util.PGobject
import io.circe.generic.auto._
import io.circe.java8.time._
import io.circe.syntax._
import io.circe.parser._

object AuditRecordQueries extends BaseQueries[AuditRecord]("auditRecord", "audit_record", "postgres") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Audit Id", prop = "auditId", col = "audit_id", typ = UuidType),
    DatabaseField(title = "Type", prop = "t", col = "t", typ = StringType),
    DatabaseField(title = "Pk", prop = "pk", col = "pk", typ = ArrayType),
    DatabaseField(title = "Changes", prop = "changes", col = "changes", typ = UnknownType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "t", "pk", "changes")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

  def getByPrimaryKey(id: UUID) = GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  case class CountByAuditId(auditId: UUID) extends ColCount(column = "audit_id", values = Seq(auditId))
  case class GetByAuditId(auditId: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends SeqQuery(
    whereClause = Some(quote("audit_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, engine, orderBys: _*),
    limit = limit, offset = offset, values = Seq(auditId.toString)
  )
  case class GetByAuditIdSeq(auditIdSeq: Seq[UUID]) extends ColSeqQuery(column = "audit_id", values = auditIdSeq)

  def insert(model: AuditRecord) = new Statement {
    override def name: String = "insert"
    override def sql: String = s"""insert into $tableName ($quotedColumns) values (?, ?, ?, ?, ?)"""
    private[this] val pkArray = "{ " + model.pk.map("\"" + _ + "\"").mkString(", ") + " }"
    private[this] val changesArray = "[ " + model.changes.map(_.asJson.noSpaces).mkString(", ") + " ]"
    override def values = Seq(model.id, model.auditId, model.t, pkArray, changesArray)
  }
  def insertBatch(models: Seq[AuditRecord]) = InsertBatch(models)
  def create(dataFields: Seq[DataField]) = CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = UpdateFields(Seq[Any](id), fields)

  override protected def fromRow(row: Row) = AuditRecord(
    id = UuidType(row, "id"),
    auditId = UuidType(row, "audit_id"),
    t = StringType(row, "t"),
    pk = row.as[PgArray]("pk").getArray.asInstanceOf[Array[String]],
    changes = parse(row.as[PGobject]("changes").getValue).right.get.as[List[AuditField]].right.get
  )
}
