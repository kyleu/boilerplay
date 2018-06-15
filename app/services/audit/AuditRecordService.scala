package services.audit

import java.util.UUID

import models.audit.AuditRecord
import models.auth.Credentials
import models.database.{Query, Row}
import models.queries.audit.AuditRecordQueries
import models.queries.audit.AuditRecordQueries.{fromRow, tableName}
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

object AuditRecordService {
  final case class GetByModel(model: String, pk: Seq[Any]) extends Query[Seq[AuditRecord]] {
    override val name = "get.audit.records.by.model"
    override val sql = s"""select * from "$tableName" where "t" = ? and "pk" = ?::character varying[]"""
    override val values: Seq[Any] = Seq(model, pk)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }
}

@javax.inject.Singleton
class AuditRecordService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[AuditRecord]("auditRecord") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(AuditRecordQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(AuditRecordQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  def getByModel(creds: Credentials, model: String, pk: Any*)(implicit trace: TraceData) = {
    ApplicationDatabase.queryF(AuditRecordService.GetByModel(model, pk))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(AuditRecordQueries.countAll(filters))(td))
  }
  override def getAll(
    creds: Credentials, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(AuditRecordQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter])(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(AuditRecordQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(AuditRecordQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(AuditRecordQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAuditId(creds: Credentials, auditId: UUID)(implicit trace: TraceData) = traceF("count.by.auditId") { td =>
    ApplicationDatabase.queryF(AuditRecordQueries.CountByAuditId(auditId))(td)
  }
  def getByAuditId(creds: Credentials, auditId: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceF("get.by.auditId")(td => ApplicationDatabase.queryF(AuditRecordQueries.GetByAuditId(auditId, orderBys, limit, offset))(td))
  }
  def getByAuditIdSeq(creds: Credentials, auditIdSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.auditId.seq") { td =>
    ApplicationDatabase.queryF(AuditRecordQueries.GetByAuditIdSeq(auditIdSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: AuditRecord)(implicit trace: TraceData) = {
    traceF("insert")(td => ApplicationDatabase.executeF(AuditRecordQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.id)(td).map {
        case Some(n) =>
          services.audit.AuditHelper.onInsert("AuditRecord", Seq(n.id.toString), n.toDataFields, creds)
          model
        case _ => throw new IllegalStateException("Unable to retrieve newly-inserted Audit Record.")
      }
      case _ => throw new IllegalStateException("Unable to find newly-inserted Audit Record.")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[AuditRecord])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(AuditRecordQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(AuditRecordQueries.create(fields))(td)
    getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) => ApplicationDatabase.executeF(AuditRecordQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find AuditRecord matching [$id].")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Audit Record [$id].")
      case Some(_) => ApplicationDatabase.executeF(AuditRecordQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) => newModel -> s"Updated [${fields.size}] fields of Audit Record [$id]."
          case None => throw new IllegalStateException(s"Cannot find AuditRecord matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find AuditRecord matching [$id].")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[AuditRecord])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, AuditRecordQueries.fields)(td))
  }
}
