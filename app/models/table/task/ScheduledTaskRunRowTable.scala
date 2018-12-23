/* Generated File */
package models.table.task

import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID
import models.task.ScheduledTaskRunRow
import services.database.slick.SlickQueryService.imports._

object ScheduledTaskRunRowTable {
  val query = TableQuery[ScheduledTaskRunRowTable]

  def getByPrimaryKey(id: UUID) = query.filter(_.id === id).result.headOption
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = query.filter(_.id.inSet(idSeq)).result
}

class ScheduledTaskRunRowTable(tag: slick.lifted.Tag) extends Table[ScheduledTaskRunRow](tag, "scheduled_task_run") {
  val id = column[UUID]("id", O.PrimaryKey)
  val task = column[String]("task")
  val arguments = column[List[String]]("arguments")
  val status = column[String]("status")
  val output = column[Json]("output")
  val started = column[LocalDateTime]("started")
  val completed = column[LocalDateTime]("completed")

  override val * = (id, task, arguments, status, output, started, completed) <> (
    (ScheduledTaskRunRow.apply _).tupled,
    ScheduledTaskRunRow.unapply
  )
}

