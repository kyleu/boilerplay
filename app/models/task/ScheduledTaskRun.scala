/* Generated File */
package models.task

import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID
import models.result.data.{DataField, DataFieldModel, DataSummary}
import util.JsonSerializers._

object ScheduledTaskRun {
  implicit val jsonEncoder: Encoder[ScheduledTaskRun] = deriveEncoder
  implicit val jsonDecoder: Decoder[ScheduledTaskRun] = deriveDecoder

  def empty(id: UUID = UUID.randomUUID, task: String = "", arguments: Seq[String] = Seq.empty, status: String = "", output: Json = Json.obj(), started: LocalDateTime = util.DateUtils.now, completed: LocalDateTime = util.DateUtils.now) = {
    ScheduledTaskRun(id, task, arguments, status, output, started, completed)
  }
}

final case class ScheduledTaskRun(
    id: UUID,
    task: String,
    arguments: Seq[String],
    status: String,
    output: Json,
    started: LocalDateTime,
    completed: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("task", Some(task)),
    DataField("arguments", Some(arguments.toString)),
    DataField("status", Some(status)),
    DataField("output", Some(output.toString)),
    DataField("started", Some(started.toString)),
    DataField("completed", Some(completed.toString))
  )

  def toSummary = DataSummary(model = "scheduledTaskRun", pk = Seq(id.toString), title = s"$task / $arguments / $status / $started ($id)")
}
