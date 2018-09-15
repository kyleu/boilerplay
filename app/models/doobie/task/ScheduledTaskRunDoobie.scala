/* Generated File */
package models.doobie.task

import cats.data.NonEmptyList
import java.util.UUID
import models.doobie.DoobieQueries
import models.task.ScheduledTaskRun
import services.database.DoobieQueryService.Imports._

object ScheduledTaskRunDoobie extends DoobieQueries[ScheduledTaskRun]("scheduled_task_run") {
  override protected val countFragment = fr"""select count(*) from "scheduled_task_run""""
  override protected val selectFragment = fr"""select "id", "task", "arguments", "status", "output", "started", "completed" from "scheduled_task_run""""

  override protected val columns = Seq("id", "task", "arguments", "status", "output", "started", "completed")
  override protected val searchColumns = Seq("id", "task", "arguments", "status", "started")

  override protected def searchFragment(q: String) = {
    fr""""id"::text = $q or "task"::text = $q or "arguments"::text = $q or "status"::text = $q or "output"::text = $q or "started"::text = $q or "completed"::text = $q"""
  }

  def getByPrimaryKey(id: UUID) = (selectFragment ++ whereAnd(fr"id = $id")).query[Option[ScheduledTaskRun]].unique
  def getByPrimaryKeySeq(idSeq: NonEmptyList[UUID]) = (selectFragment ++ in(fr"id", idSeq)).query[ScheduledTaskRun].to[Seq]
}

