/* Generated File */
package services.audit

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.ApplicationDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.util.UUID
import models.audit.AuditRecordRow
import models.queries.audit.AuditRecordRowQueries
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

@javax.inject.Singleton
class AuditRecordRowService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[AuditRecordRow]("auditRecordRow") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(AuditRecordRowQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, id: UUID)(implicit trace: TraceData) = getByPrimaryKey(creds, id).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load auditRecordRow with id [$id]."))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = if (idSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(AuditRecordRowQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(AuditRecordRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(AuditRecordRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(AuditRecordRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(AuditRecordRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(AuditRecordRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAuditId(creds: Credentials, auditId: UUID)(implicit trace: TraceData) = traceF("count.by.auditId") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.CountByAuditId(auditId))(td)
  }
  def getByAuditId(creds: Credentials, auditId: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.auditId") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.GetByAuditId(auditId, orderBys, limit, offset))(td)
  }
  def getByAuditIdSeq(creds: Credentials, auditIdSeq: Seq[UUID])(implicit trace: TraceData) = if (auditIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.auditId.seq") { td =>
      ApplicationDatabase.queryF(AuditRecordRowQueries.GetByAuditIdSeq(auditIdSeq))(td)
    }
  }

  def countById(creds: Credentials, id: UUID)(implicit trace: TraceData) = traceF("count.by.id") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.CountById(id))(td)
  }
  def getById(creds: Credentials, id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.id") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.GetById(id, orderBys, limit, offset))(td)
  }
  def getByIdSeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = if (idSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.id.seq") { td =>
      ApplicationDatabase.queryF(AuditRecordRowQueries.GetByIdSeq(idSeq))(td)
    }
  }

  def countByPk(creds: Credentials, pk: List[String])(implicit trace: TraceData) = traceF("count.by.pk") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.CountByPk(pk))(td)
  }
  def getByPk(creds: Credentials, pk: List[String], orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.pk") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.GetByPk(pk, orderBys, limit, offset))(td)
  }
  def getByPkSeq(creds: Credentials, pkSeq: Seq[List[String]])(implicit trace: TraceData) = if (pkSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.pk.seq") { td =>
      ApplicationDatabase.queryF(AuditRecordRowQueries.GetByPkSeq(pkSeq))(td)
    }
  }

  def countByT(creds: Credentials, t: String)(implicit trace: TraceData) = traceF("count.by.t") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.CountByT(t))(td)
  }
  def getByT(creds: Credentials, t: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.t") { td =>
    ApplicationDatabase.queryF(AuditRecordRowQueries.GetByT(t, orderBys, limit, offset))(td)
  }
  def getByTSeq(creds: Credentials, tSeq: Seq[String])(implicit trace: TraceData) = if (tSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.t.seq") { td =>
      ApplicationDatabase.queryF(AuditRecordRowQueries.GetByTSeq(tSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: AuditRecordRow)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(AuditRecordRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.id)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Audit Record.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[AuditRecordRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(AuditRecordRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(AuditRecordRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(AuditRecordRowQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find AuditRecordRow matching [$id].")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Audit Record [$id].")
      case Some(_) => ApplicationDatabase.executeF(AuditRecordRowQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Audit Record [$id]."
          case None => throw new IllegalStateException(s"Cannot find AuditRecordRow matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find AuditRecordRow matching [$id].")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[AuditRecordRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, AuditRecordRowQueries.fields)(td))
  }
}
