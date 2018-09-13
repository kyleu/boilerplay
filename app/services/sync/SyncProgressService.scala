/* Generated File */
package services.sync

import java.time.LocalDateTime
import models.auth.Credentials
import models.queries.sync.SyncProgressQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.sync.SyncProgress
import scala.concurrent.Future
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class SyncProgressService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[SyncProgress]("syncProgress") {
  def getByPrimaryKey(creds: Credentials, key: String)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(SyncProgressQueries.getByPrimaryKey(key))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, key: String)(implicit trace: TraceData) = getByPrimaryKey(creds, key).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load syncProgress with key [$key]."))
  }
  def getByPrimaryKeySeq(creds: Credentials, keySeq: Seq[String])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(SyncProgressQueries.getByPrimaryKeySeq(keySeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(SyncProgressQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(SyncProgressQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(SyncProgressQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(SyncProgressQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(SyncProgressQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByKey(creds: Credentials, key: String)(implicit trace: TraceData) = traceF("count.by.key") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.CountByKey(key))(td)
  }
  def getByKey(creds: Credentials, key: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.key") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByKey(key, orderBys, limit, offset))(td)
  }
  def getByKeySeq(creds: Credentials, keySeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.key.seq") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByKeySeq(keySeq))(td)
  }

  def countByLastTime(creds: Credentials, lastTime: LocalDateTime)(implicit trace: TraceData) = traceF("count.by.lastTime") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.CountByLastTime(lastTime))(td)
  }
  def getByLastTime(creds: Credentials, lastTime: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.lastTime") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByLastTime(lastTime, orderBys, limit, offset))(td)
  }
  def getByLastTimeSeq(creds: Credentials, lastTimeSeq: Seq[LocalDateTime])(implicit trace: TraceData) = traceF("get.by.lastTime.seq") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByLastTimeSeq(lastTimeSeq))(td)
  }

  def countByMessage(creds: Credentials, message: String)(implicit trace: TraceData) = traceF("count.by.message") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.CountByMessage(message))(td)
  }
  def getByMessage(creds: Credentials, message: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.message") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByMessage(message, orderBys, limit, offset))(td)
  }
  def getByMessageSeq(creds: Credentials, messageSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.message.seq") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByMessageSeq(messageSeq))(td)
  }

  def countByStatus(creds: Credentials, status: String)(implicit trace: TraceData) = traceF("count.by.status") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.CountByStatus(status))(td)
  }
  def getByStatus(creds: Credentials, status: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.status") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByStatus(status, orderBys, limit, offset))(td)
  }
  def getByStatusSeq(creds: Credentials, statusSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.status.seq") { td =>
    ApplicationDatabase.queryF(SyncProgressQueries.GetByStatusSeq(statusSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: SyncProgress)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(SyncProgressQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.key)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Sync Progress.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[SyncProgress])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(SyncProgressQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(SyncProgressQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "key"))
    }
  }

  def remove(creds: Credentials, key: String)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, key)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(SyncProgressQueries.removeByPrimaryKey(key))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find SyncProgress matching [$key].")
    })
  }

  def update(creds: Credentials, key: String, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, key)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Sync Progress [$key].")
      case Some(_) => ApplicationDatabase.executeF(SyncProgressQueries.update(key, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, key)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Sync Progress [$key]."
          case None => throw new IllegalStateException(s"Cannot find SyncProgress matching [$key].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find SyncProgress matching [$key].")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[SyncProgress])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, SyncProgressQueries.fields)(td))
  }
}
