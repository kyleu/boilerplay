/* Generated File */
package models.queries.task

import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID
import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.queries.BaseQueries
import models.result.ResultFieldHelper
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.task.ScheduledTaskRun

object ScheduledTaskRunQueries extends BaseQueries[ScheduledTaskRun]("scheduledTaskRun", "scheduled_task_run") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Task", prop = "task", col = "task", typ = StringType),
    DatabaseField(title = "Arguments", prop = "arguments", col = "arguments", typ = StringArrayType),
    DatabaseField(title = "Status", prop = "status", col = "status", typ = StringType),
    DatabaseField(title = "Output", prop = "output", col = "output", typ = JsonType),
    DatabaseField(title = "Started", prop = "started", col = "started", typ = TimestampType),
    DatabaseField(title = "Completed", prop = "completed", col = "completed", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "task", "arguments", "status", "started")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

  def getByPrimaryKey(id: UUID) = GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  case class CountByArguments(arguments: Array[Any]) extends ColCount(column = "arguments", values = Seq(arguments))
  case class GetByArguments(arguments: Array[Any], orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("arguments") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(arguments)
  )
  case class GetByArgumentsSeq(argumentsSeq: Seq[Array[Any]]) extends ColSeqQuery(column = "arguments", values = argumentsSeq)

  case class CountById(id: UUID) extends ColCount(column = "id", values = Seq(id))
  case class GetById(id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(id)
  )
  case class GetByIdSeq(idSeq: Seq[UUID]) extends ColSeqQuery(column = "id", values = idSeq)

  case class CountByStarted(started: LocalDateTime) extends ColCount(column = "started", values = Seq(started))
  case class GetByStarted(started: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("started") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(started)
  )
  case class GetByStartedSeq(startedSeq: Seq[LocalDateTime]) extends ColSeqQuery(column = "started", values = startedSeq)

  case class CountByStatus(status: String) extends ColCount(column = "status", values = Seq(status))
  case class GetByStatus(status: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("status") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(status)
  )
  case class GetByStatusSeq(statusSeq: Seq[String]) extends ColSeqQuery(column = "status", values = statusSeq)

  case class CountByTask(task: String) extends ColCount(column = "task", values = Seq(task))
  case class GetByTask(task: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("task") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(task)
  )
  case class GetByTaskSeq(taskSeq: Seq[String]) extends ColSeqQuery(column = "task", values = taskSeq)

  def insert(model: ScheduledTaskRun) = Insert(model)
  def insertBatch(models: Seq[ScheduledTaskRun]) = InsertBatch(models)
  def create(dataFields: Seq[DataField]) = CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = UpdateFields(Seq[Any](id), fields)

  override def fromRow(row: Row) = ScheduledTaskRun(
    id = UuidType(row, "id"),
    task = StringType(row, "task"),
    arguments = StringArrayType(row, "arguments"),
    status = StringType(row, "status"),
    output = JsonType(row, "output"),
    started = TimestampType(row, "started"),
    completed = TimestampType(row, "completed")
  )
}
