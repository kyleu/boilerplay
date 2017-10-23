package services.audit

import java.util.UUID

import models.audit._
import models.queries.audit.AuditQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.{Application, Configuration}
import play.api.inject.Injector
import services.ModelServiceHelper
import services.database.{ApplicationDatabase, SystemDatabase}
import services.supervisor.ActorSupervisor
import util.FutureUtils
import util.tracing.{TraceData, TracingService}
import util.web.TracingWSClient

@javax.inject.Singleton
class AuditService @javax.inject.Inject() (
    override val tracing: TracingService, inject: Injector, config: Configuration, ws: TracingWSClient, lookup: AuditLookup, fu: FutureUtils
) extends ModelServiceHelper[Audit]("audit") {
  def getByPrimaryKey(id: UUID)(implicit trace: TraceData) = {
    traceB("get.by.primary.key")(td => SystemDatabase.query(AuditQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeySeq(idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceB("get.by.primary.key.seq")(td => ApplicationDatabase.query(AuditQueries.getByPrimaryKeySeq(idSeq))(td))
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

  def searchExact(q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceB("search.exact")(td => ApplicationDatabase.query(AuditQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByUserId(user: UUID)(implicit trace: TraceData) = traceB("count.by.user") { td =>
    SystemDatabase.query(AuditQueries.CountByUserId(user))(td)
  }
  def getByUserId(user: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int])(implicit trace: TraceData) = {
    traceB("get.by.user")(td => SystemDatabase.query(AuditQueries.GetByUserId(user, orderBys, limit, offset))(td))
  }
  def getByUserIdSeq(userSeq: Seq[UUID])(implicit trace: TraceData) = traceB("get.by.user.seq") { td =>
    SystemDatabase.query(AuditQueries.GetByUserIdSeq(userSeq))(td)
  }

  // Mutations
  def insert(model: Audit)(implicit trace: TraceData) = {
    traceB("insert")(td => ApplicationDatabase.execute(AuditQueries.insert(model))(td) match {
      case 1 => getByPrimaryKey(model.id)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Audit.")
    })
  }
  def insertBatch(models: Seq[Audit])(implicit trace: TraceData) = {
    traceB("insertBatch")(td => ApplicationDatabase.execute(AuditQueries.insertBatch(models))(td))
  }
  def create(fields: Seq[DataField])(implicit trace: TraceData) = traceB("create") { td =>
    ApplicationDatabase.execute(AuditQueries.create(fields))(td)
    services.audit.AuditHelper.onInsert("Audit", Seq(fieldVal(fields, "id")), fields)
    getByPrimaryKey(UUID.fromString(fieldVal(fields, "id")))
  }

  def remove(id: UUID)(implicit trace: TraceData) = {
    traceB("remove")(td => ApplicationDatabase.query(AuditQueries.getByPrimaryKey(id))(td) match {
      case Some(current) =>
        ApplicationDatabase.execute(AuditQueries.removeByPrimaryKey(id))(td)
        current
      case None => throw new IllegalStateException(s"Cannot find Audit matching [$id].")
    })
  }

  def update(id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceB("update")(td => ApplicationDatabase.query(AuditQueries.getByPrimaryKey(id))(td) match {
      case Some(current) if fields.isEmpty => current -> s"No changes required for Audit [$id]."
      case Some(current) =>
        ApplicationDatabase.execute(AuditQueries.update(id, fields))(td)
        ApplicationDatabase.query(AuditQueries.getByPrimaryKey(id))(td) match {
          case Some(newModel) =>
            services.audit.AuditHelper.onUpdate("Audit", Seq(DataField("id", Some(id.toString))), current.toDataFields, fields)
            newModel -> s"Updated [${fields.size}] fields of Audit [$id]."
          case None => throw new IllegalStateException(s"Cannot find Audit matching [$id].")
        }
      case None => throw new IllegalStateException(s"Cannot find Audit matching [$id].")
    })
  }

  def csvFor(operation: String, totalCount: Int, rows: Seq[Audit])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, AuditQueries.fields)(td))
  }

  lazy val supervisor = inject.instanceOf(classOf[Application]).supervisor
  AuditHelper.init(this)
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
