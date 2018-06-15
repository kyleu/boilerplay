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
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(id: UUID) = new GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  final case class CountByArguments(arguments: Array[Any]) extends ColCount(column = "arguments", values = Seq(arguments))
  final case class GetByArguments(arguments: Array[Any], orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("arguments") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(arguments)
  )
  final case class GetByArgumentsSeq(argumentsSeq: Seq[Array[Any]]) extends ColSeqQuery(column = "arguments", values = argumentsSeq)

  final case class CountById(id: UUID) extends ColCount(column = "id", values = Seq(id))
  final case class GetById(id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(id)
  )
  final case class GetByIdSeq(idSeq: Seq[UUID]) extends ColSeqQuery(column = "id", values = idSeq)

  final case class CountByStarted(started: LocalDateTime) extends ColCount(column = "started", values = Seq(started))
  final case class GetByStarted(started: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("started") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(started)
  )
  final case class GetByStartedSeq(startedSeq: Seq[LocalDateTime]) extends ColSeqQuery(column = "started", values = startedSeq)

  final case class CountByStatus(status: String) extends ColCount(column = "status", values = Seq(status))
  final case class GetByStatus(status: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("status") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(status)
  )
  final case class GetByStatusSeq(statusSeq: Seq[String]) extends ColSeqQuery(column = "status", values = statusSeq)

  final case class CountByTask(task: String) extends ColCount(column = "task", values = Seq(task))
  final case class GetByTask(task: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("task") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(task)
  )
  final case class GetByTaskSeq(taskSeq: Seq[String]) extends ColSeqQuery(column = "task", values = taskSeq)

  def insert(model: ScheduledTaskRun) = new Insert(model)
  def insertBatch(models: Seq[ScheduledTaskRun]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = new RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq[Any](id), fields)

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
