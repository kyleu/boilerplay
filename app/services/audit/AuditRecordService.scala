package services.audit

import java.util.UUID

import models.audit.AuditRecord
import models.queries.audit.AuditRecordQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class AuditRecordService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[AuditRecord]("auditRecord") {
  def getByPrimaryKey(id: UUID)(implicit trace: TraceData) = {
    traceB("get.by.primary.key")(td => ApplicationDatabase.query(AuditRecordQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeySeq(idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceB("get.by.primary.key.seq")(td => ApplicationDatabase.query(AuditRecordQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceB("get.all.count")(td => ApplicationDatabase.query(AuditRecordQueries.countAll(filters))(td))
  }
  override def getAll(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("get.all")(td => ApplicationDatabase.query(AuditRecordQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(q: String, filters: Seq[Filter])(implicit trace: TraceData) = {
    traceB("search.count")(td => ApplicationDatabase.query(AuditRecordQueries.searchCount(q, filters))(td))
  }
  override def search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("search")(td => ApplicationDatabase.query(AuditRecordQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("search.exact")(td => ApplicationDatabase.query(AuditRecordQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAuditId(auditId: UUID)(implicit trace: TraceData) = traceB("count.by.auditId") { td =>
    ApplicationDatabase.query(AuditRecordQueries.CountByAuditId(auditId))(td)
  }
  def getByAuditId(auditId: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceB("get.by.auditId")(td => ApplicationDatabase.query(AuditRecordQueries.GetByAuditId(auditId, orderBys, limit, offset))(td))
  }
  def getByAuditIdSeq(auditIdSeq: Seq[UUID])(implicit trace: TraceData) = traceB("get.by.auditId.seq") { td =>
    ApplicationDatabase.query(AuditRecordQueries.GetByAuditIdSeq(auditIdSeq))(td)
  }

  // Mutations
  def insert(model: AuditRecord)(implicit trace: TraceData) = {
    traceB("insert") { td =>
      ApplicationDatabase.execute(AuditRecordQueries.insert(model))(td) match {
        case 1 => getByPrimaryKey(model.id)(td)
        case x => throw new IllegalStateException("Unable to find newly-inserted Audit Record.")
      }
    }
  }
  def create(fields: Seq[DataField])(implicit trace: TraceData) = {
    traceB("create")(td => ApplicationDatabase.execute(AuditRecordQueries.create(fields))(td))
  }

  def remove(id: UUID)(implicit trace: TraceData) = {
    traceB("remove") { td =>
      ApplicationDatabase.query(AuditRecordQueries.getByPrimaryKey(id))(td) match {
        case Some(current) =>
          ApplicationDatabase.execute(AuditRecordQueries.removeByPrimaryKey(id))(td)
          current
        case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
      }
    }
  }

  def update(id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = traceB("update") { td =>
    ApplicationDatabase.query(AuditRecordQueries.getByPrimaryKey(id))(td) match {
      case Some(current) =>
        ApplicationDatabase.execute(AuditRecordQueries.update(id, fields))(td)
        ApplicationDatabase.query(AuditRecordQueries.getByPrimaryKey(id))(td) match {
          case Some(newModel) => newModel
          case None => throw new IllegalStateException(s"Cannot find AuditRecord matching [$id].")
        }
      case None => throw new IllegalStateException(s"Cannot find AuditRecord matching [$id].")
    }
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[AuditRecord])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, AuditRecordQueries.fields)(td))
  }
}
