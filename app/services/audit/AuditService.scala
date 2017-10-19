package services.audit

import java.util.UUID

import models.audit.{Audit, AuditComplete, AuditField, AuditStart}
import models.queries.audit.AuditQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.{Application, Configuration}
import play.api.inject.Injector
import services.ModelServiceHelper
import services.database.SystemDatabase
import services.supervisor.ActorSupervisor
import util.tracing.{TraceData, TracingService}
import util.web.TracingWSClient
import util.{FutureUtils, Logging, NullUtils}

object AuditService extends Logging {
  private var inst: Option[AuditService] = None
  private def getInst = inst.getOrElse(throw new IllegalStateException("Not initialized."))

  def onAudit(audit: Audit)(implicit trace: TraceData) = getInst.callback(audit)

  def onStart(id: UUID, msg: AuditStart)(implicit traceData: TraceData) = getInst.cache.onStart(id, msg)
  def onComplete(msg: AuditComplete)(implicit traceData: TraceData) = getInst.cache.onComplete(msg)

  def onInsert(t: String, pk: Seq[String], fields: Seq[DataField])(implicit trace: TraceData) = {
    val msg = s"Inserted new [$t] with [${fields.size}] fields:"
    val auditId = UUID.randomUUID
    val records = Seq(Audit.Record(auditId = auditId, t = t, pk = pk, changes = fields.map(f => AuditField(f.k, None, f.v))))
    onAudit(Audit(id = auditId, act = "insert", msg = msg, records = records))
  }

  def onUpdate(t: String, ids: Seq[DataField], originalFields: Seq[DataField], newFields: Seq[DataField])(implicit trace: TraceData) = {
    def changeFor(f: DataField) = originalFields.find(_.k == f.k).flatMap {
      case o if f.v != o.v => Some(AuditField(f.k, o.v, f.v))
      case _ => None
    }
    val changes = newFields.flatMap(changeFor)
    val msg = s"Updated [${changes.size}] fields of $t[${ids.map(id => id.k + ": " + id.v.getOrElse(NullUtils.str)).mkString(", ")}]:\n"
    val auditId = UUID.randomUUID
    val records = Seq(Audit.Record(auditId = auditId, t = t, changes = changes))
    onAudit(Audit(id = auditId, act = "update", msg = msg, records = records))
  }

  def onRemove(t: String, pk: Seq[String], fields: Seq[DataField])(implicit trace: TraceData) = {
    val msg = s"Removed [$t] with [${fields.size}] fields:"
    val auditId = UUID.randomUUID
    val records = Seq(Audit.Record(auditId = auditId, t = t, pk = pk, changes = fields.map(f => AuditField(f.k, None, f.v))))
    onAudit(Audit(id = auditId, act = "remove", msg = msg, records = records))
  }
}

@javax.inject.Singleton
class AuditService @javax.inject.Inject() (
    override val tracing: TracingService, inject: Injector, config: Configuration, ws: TracingWSClient, lookup: AuditLookup, fu: FutureUtils
) extends ModelServiceHelper[Audit]("audit") {
  def getByPrimaryKey(id: UUID)(implicit trace: TraceData) = {
    traceB("get.by.primary.key")(td => SystemDatabase.query(AuditQueries.getByPrimaryKey(id))(td))
  }

  override def countAll(filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceB("get.all.count")(td => SystemDatabase.query(AuditQueries.countAll(filters))(td))
  }
  override def getAll(filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("get.all")(td => SystemDatabase.query(AuditQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(q: String, filters: Seq[Filter])(implicit trace: TraceData) = {
    traceB("search.count")(td => SystemDatabase.query(AuditQueries.searchCount(q, filters))(td))
  }
  override def search(q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("search")(td => SystemDatabase.query(AuditQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def countByAuthor(author: UUID)(implicit trace: TraceData) = traceB("count.by.author") { td =>
    SystemDatabase.query(AuditQueries.CountByAuthor(author))(td)
  }
  def getByAuthor(author: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceB("get.by.author")(td => SystemDatabase.query(AuditQueries.GetByAuthor(author, orderBys, limit, offset))(td))
  }
  def getByAuthorSeq(authorSeq: Seq[UUID])(implicit trace: TraceData) = traceB("get.by.author.seq") { td =>
    SystemDatabase.query(AuditQueries.GetByAuthorSeq(authorSeq))(td)
  }

  def remove(id: UUID)(implicit trace: TraceData) = {
    traceB("remove") { td =>
      SystemDatabase.query(AuditQueries.getByPrimaryKey(id))(td) match {
        case Some(current) =>
          SystemDatabase.execute(AuditQueries.removeByPrimaryKey(id))(td)
          current
        case None => throw new IllegalStateException(s"Cannot find Note matching [$id].")
      }
    }
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Audit])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, AuditQueries.fields)(td))
  }

  lazy val supervisor = inject.instanceOf(classOf[Application]).supervisor

  AuditService.inst.foreach(_ => throw new IllegalStateException("Double initialization."))
  AuditService.inst = Some(this)

  lazy val cache = new AuditCache(supervisor, lookup)

  def callback(a: Audit)(implicit trace: TraceData) = if (a.records.exists(_.changes.nonEmpty)) {
    supervisor ! ActorSupervisor.Broadcast(models.AuditNotification(a))
    AuditNotifications.postToSlack(ws, config.slackConfig, a)
    AuditNotifications.persist(a)
    log.info(a.changeLog)
    a
  } else {
    log.info(s"Ignoring audit [${a.id}], as it has no changes.")
    a
  }
}
