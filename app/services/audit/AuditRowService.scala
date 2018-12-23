/* Generated File */
package services.audit

import java.util.UUID

import models.audit.AuditRow
import models.auth.Credentials
import models.queries.audit.AuditRowQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.tag.Tag

import scala.concurrent.Future
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.CsvUtils
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class AuditRowService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[AuditRow]("auditRow") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(AuditRowQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, id: UUID)(implicit trace: TraceData) = getByPrimaryKey(creds, id).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load auditRow with id [$id]."))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(AuditRowQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(AuditRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(AuditRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(AuditRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(AuditRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(AuditRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAct(creds: Credentials, act: String)(implicit trace: TraceData) = traceF("count.by.act") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.CountByAct(act))(td)
  }
  def getByAct(creds: Credentials, act: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.act") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByAct(act, orderBys, limit, offset))(td)
  }
  def getByActSeq(creds: Credentials, actSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.act.seq") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByActSeq(actSeq))(td)
  }

  def countByApp(creds: Credentials, app: String)(implicit trace: TraceData) = traceF("count.by.app") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.CountByApp(app))(td)
  }
  def getByApp(creds: Credentials, app: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.app") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByApp(app, orderBys, limit, offset))(td)
  }
  def getByAppSeq(creds: Credentials, appSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.app.seq") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByAppSeq(appSeq))(td)
  }

  def countByClient(creds: Credentials, client: String)(implicit trace: TraceData) = traceF("count.by.client") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.CountByClient(client))(td)
  }
  def getByClient(creds: Credentials, client: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.client") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByClient(client, orderBys, limit, offset))(td)
  }
  def getByClientSeq(creds: Credentials, clientSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.client.seq") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByClientSeq(clientSeq))(td)
  }

  def countById(creds: Credentials, id: UUID)(implicit trace: TraceData) = traceF("count.by.id") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.CountById(id))(td)
  }
  def getById(creds: Credentials, id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.id") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetById(id, orderBys, limit, offset))(td)
  }
  def getByIdSeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.id.seq") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByIdSeq(idSeq))(td)
  }

  def countByServer(creds: Credentials, server: String)(implicit trace: TraceData) = traceF("count.by.server") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.CountByServer(server))(td)
  }
  def getByServer(creds: Credentials, server: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.server") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByServer(server, orderBys, limit, offset))(td)
  }
  def getByServerSeq(creds: Credentials, serverSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.server.seq") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByServerSeq(serverSeq))(td)
  }

  def countByTags(creds: Credentials, tags: List[Tag])(implicit trace: TraceData) = traceF("count.by.tags") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.CountByTags(tags))(td)
  }
  def getByTags(creds: Credentials, tags: List[Tag], orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.tags") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByTags(tags, orderBys, limit, offset))(td)
  }
  def getByTagsSeq(creds: Credentials, tagsSeq: Seq[List[Tag]])(implicit trace: TraceData) = traceF("get.by.tags.seq") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByTagsSeq(tagsSeq))(td)
  }

  def countByUserId(creds: Credentials, userId: UUID)(implicit trace: TraceData) = traceF("count.by.userId") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.CountByUserId(userId))(td)
  }
  def getByUserId(creds: Credentials, userId: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.userId") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByUserId(userId, orderBys, limit, offset))(td)
  }
  def getByUserIdSeq(creds: Credentials, userIdSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.userId.seq") { td =>
    ApplicationDatabase.queryF(AuditRowQueries.GetByUserIdSeq(userIdSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: AuditRow)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(AuditRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.id)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Audit.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[AuditRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(AuditRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(AuditRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(AuditRowQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find AuditRow matching [$id].")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Audit [$id].")
      case Some(_) => ApplicationDatabase.executeF(AuditRowQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Audit [$id]."
          case None => throw new IllegalStateException(s"Cannot find AuditRow matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find AuditRow matching [$id].")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[AuditRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, AuditRowQueries.fields)(td))
  }
}
