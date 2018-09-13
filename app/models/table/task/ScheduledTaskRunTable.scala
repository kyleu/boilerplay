/* Generated File */
package models.table.task

import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID
import services.database.SlickQueryService.imports._

object ScheduledTaskRunTable {
  val query = TableQuery[ScheduledTaskRunTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result
}

class ScheduledTaskRunTable(tag: Tag) extends Table[models.task.ScheduledTaskRun](tag, "scheduled_task_run") {
  val id = column[UUID]("id")
  val task = column[String]("task")
  val arguments = column[List[String]]("arguments")
  val status = column[String]("status")
  val output = column[Json]("output")
  val started = column[LocalDateTime]("started")
  val completed = column[LocalDateTime]("completed")

  val modelPrimaryKey = primaryKey("pk_scheduled_task_run", id)

  override val * = (id, task, arguments, status, output, started, completed) <> (
    (models.task.ScheduledTaskRun.apply _).tupled,
    models.task.ScheduledTaskRun.unapply
  )
}

