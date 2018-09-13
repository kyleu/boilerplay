/* Generated File */
package models.doobie.task

import cats.data.NonEmptyList
import java.util.UUID
import models.task.ScheduledTaskRun
import services.database.DoobieQueryService.Imports._

object ScheduledTaskRunDoobie {
  val countFragment = fr"select count(*) from scheduled_task_run"
  val selectFragment = fr"select id, task, arguments, status, output, started, completed from scheduled_task_run"

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[ScheduledTaskRun].to[Seq]

  def getByPrimaryKey(id: UUID) = (selectFragment ++ fr"where id = $id").query[Option[ScheduledTaskRun]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[ScheduledTaskRun].to[Seq]
}

