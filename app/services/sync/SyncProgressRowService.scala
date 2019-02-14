/* Generated File */
package services.sync

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.ApplicationDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.LocalDateTime
import models.queries.sync.SyncProgressRowQueries
import models.sync.SyncProgressRow
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

@javax.inject.Singleton
class SyncProgressRowService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[SyncProgressRow]("syncProgressRow") {
  def getByPrimaryKey(creds: Credentials, key: String)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(SyncProgressRowQueries.getByPrimaryKey(key))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, key: String)(implicit trace: TraceData) = getByPrimaryKey(creds, key).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load syncProgressRow with key [$key]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, keySeq: Seq[String])(implicit trace: TraceData) = if (keySeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(SyncProgressRowQueries.getByPrimaryKeySeq(keySeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(SyncProgressRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(SyncProgressRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(SyncProgressRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(SyncProgressRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(SyncProgressRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByKey(creds: Credentials, key: String)(implicit trace: TraceData) = traceF("count.by.key") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.CountByKey(key))(td)
  }
  def getByKey(creds: Credentials, key: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.key") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.GetByKey(key, orderBys, limit, offset))(td)
  }
  def getByKeySeq(creds: Credentials, keySeq: Seq[String])(implicit trace: TraceData) = if (keySeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.key.seq") { td =>
      ApplicationDatabase.queryF(SyncProgressRowQueries.GetByKeySeq(keySeq))(td)
    }
  }

  def countByLastTime(creds: Credentials, lastTime: LocalDateTime)(implicit trace: TraceData) = traceF("count.by.lastTime") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.CountByLastTime(lastTime))(td)
  }
  def getByLastTime(creds: Credentials, lastTime: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.lastTime") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.GetByLastTime(lastTime, orderBys, limit, offset))(td)
  }
  def getByLastTimeSeq(creds: Credentials, lastTimeSeq: Seq[LocalDateTime])(implicit trace: TraceData) = if (lastTimeSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.lastTime.seq") { td =>
      ApplicationDatabase.queryF(SyncProgressRowQueries.GetByLastTimeSeq(lastTimeSeq))(td)
    }
  }

  def countByMessage(creds: Credentials, message: String)(implicit trace: TraceData) = traceF("count.by.message") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.CountByMessage(message))(td)
  }
  def getByMessage(creds: Credentials, message: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.message") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.GetByMessage(message, orderBys, limit, offset))(td)
  }
  def getByMessageSeq(creds: Credentials, messageSeq: Seq[String])(implicit trace: TraceData) = if (messageSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.message.seq") { td =>
      ApplicationDatabase.queryF(SyncProgressRowQueries.GetByMessageSeq(messageSeq))(td)
    }
  }

  def countByStatus(creds: Credentials, status: String)(implicit trace: TraceData) = traceF("count.by.status") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.CountByStatus(status))(td)
  }
  def getByStatus(creds: Credentials, status: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.status") { td =>
    ApplicationDatabase.queryF(SyncProgressRowQueries.GetByStatus(status, orderBys, limit, offset))(td)
  }
  def getByStatusSeq(creds: Credentials, statusSeq: Seq[String])(implicit trace: TraceData) = if (statusSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.status.seq") { td =>
      ApplicationDatabase.queryF(SyncProgressRowQueries.GetByStatusSeq(statusSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: SyncProgressRow)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(SyncProgressRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.key)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Sync Progress.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[SyncProgressRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(SyncProgressRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(SyncProgressRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "key"))
    }
  }

  def remove(creds: Credentials, key: String)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, key)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(SyncProgressRowQueries.removeByPrimaryKey(key))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find SyncProgressRow matching [$key]")
    })
  }

  def update(creds: Credentials, key: String, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, key)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Sync Progress [$key]")
      case Some(_) => ApplicationDatabase.executeF(SyncProgressRowQueries.update(key, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, key)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Sync Progress [$key]"
          case None => throw new IllegalStateException(s"Cannot find SyncProgressRow matching [$key]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find SyncProgressRow matching [$key]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[SyncProgressRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, SyncProgressRowQueries.fields)(td))
  }
}
