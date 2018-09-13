/* Generated File */
package services.task

import java.time.LocalDateTime
import java.util.UUID
import models.auth.Credentials
import models.queries.task.ScheduledTaskRunQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.task.ScheduledTaskRun
import scala.concurrent.Future
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class ScheduledTaskRunService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[ScheduledTaskRun]("scheduledTaskRun") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(ScheduledTaskRunQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, id: UUID)(implicit trace: TraceData) = getByPrimaryKey(creds, id).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load scheduledTaskRun with id [$id]."))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(ScheduledTaskRunQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(ScheduledTaskRunQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(ScheduledTaskRunQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(ScheduledTaskRunQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(ScheduledTaskRunQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(ScheduledTaskRunQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByArguments(creds: Credentials, arguments: Array[Any])(implicit trace: TraceData) = traceF("count.by.arguments") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.CountByArguments(arguments))(td)
  }
  def getByArguments(creds: Credentials, arguments: Array[Any], orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.arguments") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByArguments(arguments, orderBys, limit, offset))(td)
  }
  def getByArgumentsSeq(creds: Credentials, argumentsSeq: Seq[Array[Any]])(implicit trace: TraceData) = traceF("get.by.arguments.seq") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByArgumentsSeq(argumentsSeq))(td)
  }

  def countById(creds: Credentials, id: UUID)(implicit trace: TraceData) = traceF("count.by.id") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.CountById(id))(td)
  }
  def getById(creds: Credentials, id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.id") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetById(id, orderBys, limit, offset))(td)
  }
  def getByIdSeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = traceF("get.by.id.seq") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByIdSeq(idSeq))(td)
  }

  def countByStarted(creds: Credentials, started: LocalDateTime)(implicit trace: TraceData) = traceF("count.by.started") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.CountByStarted(started))(td)
  }
  def getByStarted(creds: Credentials, started: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.started") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByStarted(started, orderBys, limit, offset))(td)
  }
  def getByStartedSeq(creds: Credentials, startedSeq: Seq[LocalDateTime])(implicit trace: TraceData) = traceF("get.by.started.seq") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByStartedSeq(startedSeq))(td)
  }

  def countByStatus(creds: Credentials, status: String)(implicit trace: TraceData) = traceF("count.by.status") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.CountByStatus(status))(td)
  }
  def getByStatus(creds: Credentials, status: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.status") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByStatus(status, orderBys, limit, offset))(td)
  }
  def getByStatusSeq(creds: Credentials, statusSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.status.seq") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByStatusSeq(statusSeq))(td)
  }

  def countByTask(creds: Credentials, task: String)(implicit trace: TraceData) = traceF("count.by.task") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.CountByTask(task))(td)
  }
  def getByTask(creds: Credentials, task: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.task") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByTask(task, orderBys, limit, offset))(td)
  }
  def getByTaskSeq(creds: Credentials, taskSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.task.seq") { td =>
    ApplicationDatabase.queryF(ScheduledTaskRunQueries.GetByTaskSeq(taskSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: ScheduledTaskRun)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(ScheduledTaskRunQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.id)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Scheduled Task Run.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[ScheduledTaskRun])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(ScheduledTaskRunQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(ScheduledTaskRunQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(ScheduledTaskRunQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find ScheduledTaskRun matching [$id].")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Scheduled Task Run [$id].")
      case Some(_) => ApplicationDatabase.executeF(ScheduledTaskRunQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Scheduled Task Run [$id]."
          case None => throw new IllegalStateException(s"Cannot find ScheduledTaskRun matching [$id].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find ScheduledTaskRun matching [$id].")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[ScheduledTaskRun])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, ScheduledTaskRunQueries.fields)(td))
  }
}
