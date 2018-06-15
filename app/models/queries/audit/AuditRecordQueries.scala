/* Generated File */
package models.queries.audit

import io.circe.Json
import java.util.UUID
import models.audit.AuditRecord
import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.queries.BaseQueries
import models.result.ResultFieldHelper
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy

object AuditRecordQueries extends BaseQueries[AuditRecord]("auditRecord", "audit_record") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Audit Id", prop = "auditId", col = "audit_id", typ = UuidType),
    DatabaseField(title = "T", prop = "t", col = "t", typ = StringType),
    DatabaseField(title = "Pk", prop = "pk", col = "pk", typ = StringArrayType),
    DatabaseField(title = "Changes", prop = "changes", col = "changes", typ = JsonType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "t", "pk")

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

  final case class CountByAuditId(auditId: UUID) extends ColCount(column = "audit_id", values = Seq(auditId))
  final case class GetByAuditId(auditId: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("audit_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(auditId)
  )
  final case class GetByAuditIdSeq(auditIdSeq: Seq[UUID]) extends ColSeqQuery(column = "audit_id", values = auditIdSeq)

  final case class CountById(id: UUID) extends ColCount(column = "id", values = Seq(id))
  final case class GetById(id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(id)
  )
  final case class GetByIdSeq(idSeq: Seq[UUID]) extends ColSeqQuery(column = "id", values = idSeq)

  final case class CountByPk(pk: Array[Any]) extends ColCount(column = "pk", values = Seq(pk))
  final case class GetByPk(pk: Array[Any], orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("pk") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(pk)
  )
  final case class GetByPkSeq(pkSeq: Seq[Array[Any]]) extends ColSeqQuery(column = "pk", values = pkSeq)

  final case class CountByT(t: String) extends ColCount(column = "t", values = Seq(t))
  final case class GetByT(t: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("t") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(t)
  )
  final case class GetByTSeq(tSeq: Seq[String]) extends ColSeqQuery(column = "t", values = tSeq)

  def insert(model: AuditRecord) = new Insert(model)
  def insertBatch(models: Seq[AuditRecord]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = new RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq[Any](id), fields)

  override def fromRow(row: Row) = AuditRecord(
    id = UuidType(row, "id"),
    auditId = UuidType(row, "audit_id"),
    t = StringType(row, "t"),
    pk = StringArrayType(row, "pk"),
    changes = JsonType(row, "changes")
  )
}
