package services.audit

import java.util.UUID

import models.audit._
import models.auth.Credentials
import models.queries.audit.AuditQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.{Application, Configuration}
import play.api.inject.Injector
import services.ModelServiceHelper
import services.database.SystemDatabase
import services.supervisor.ActorSupervisor
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}
import util.web.TracingWSClient

import scala.concurrent.Future

@javax.inject.Singleton
class AuditService @javax.inject.Inject() (
    override val tracing: TracingService, inject: Injector, config: Configuration, ws: TracingWSClient, lookup: AuditLookup
) extends ModelServiceHelper[Audit]("audit") {
  lazy val supervisor = inject.instanceOf(classOf[Application]).supervisor
  AuditHelper.init(this)
  lazy val cache = new AuditCache(supervisor, lookup)

  def callback(a: Audit)(implicit trace: TraceData) = if (a.records.exists(_.changes.nonEmpty)) {
    supervisor.tell(ActorSupervisor.Broadcast("audit", models.AuditNotification(a)), supervisor)
    AuditNotifications.postToSlack(ws, config.slackConfig, a)
    AuditNotifications.persist(a)
    log.info(a.changeLog)
    a
  } else {
    log.info(s"Ignoring audit [${a.id}], as it has no changes.")
    a
  }

  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => SystemDatabase.queryF(AuditQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => SystemDatabase.queryF(AuditQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => SystemDatabase.queryF(AuditQueries.countAll(filters))(td))
  }
  override def getAll(
    creds: Credentials, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("get.all")(td => SystemDatabase.queryF(AuditQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: String, filters: Seq[Filter])(implicit trace: TraceData) = {
    traceF("search.count")(td => SystemDatabase.queryF(AuditQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: String, filters: Seq[Filter], orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => SystemDatabase.queryF(AuditQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => SystemDatabase.queryF(AuditQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByUserId(user: UUID)(implicit trace: TraceData) = traceF("count.by.user") { td =>
    SystemDatabase.queryF(AuditQueries.CountByUserId(user))(td)
  }
  def getByUserId(creds: Credentials, id: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceF("get.by.user")(td => SystemDatabase.queryF(AuditQueries.GetByUserId(id, orderBys, limit, offset))(td))
  }
  def getByUserIdSeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.user.seq") { td =>
    SystemDatabase.queryF(AuditQueries.GetByUserIdSeq(idSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: Audit)(implicit trace: TraceData) = {
    traceF("insert")(td => SystemDatabase.executeF(AuditQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.id)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Audit.")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[Audit])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => SystemDatabase.executeF(AuditQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    SystemDatabase.executeF(AuditQueries.create(fields))(td)
    getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) => SystemDatabase.executeF(AuditQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find Audit matching [$id].")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Audit [$id].")
      case Some(_) => SystemDatabase.executeF(AuditQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) => newModel -> s"Updated [${fields.size}] fields of Audit [$id]."
          case None => throw new IllegalStateException(s"Cannot find Audit matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find Audit matching [$id].")
    })
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Audit])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, AuditQueries.fields)(td))
  }
}
