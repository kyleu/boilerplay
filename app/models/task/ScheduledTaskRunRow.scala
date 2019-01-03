/* Generated File */
package models.task

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID

object ScheduledTaskRunRow {
  implicit val jsonEncoder: Encoder[ScheduledTaskRunRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[ScheduledTaskRunRow] = deriveDecoder

  def empty(id: UUID = UUID.randomUUID, task: String = "", arguments: List[String] = List.empty, status: String = "", output: Json = Json.obj(), started: LocalDateTime = DateUtils.now, completed: LocalDateTime = DateUtils.now) = {
    ScheduledTaskRunRow(id, task, arguments, status, output, started, completed)
  }
}

final case class ScheduledTaskRunRow(
    id: UUID,
    task: String,
    arguments: List[String],
    status: String,
    output: Json,
    started: LocalDateTime,
    completed: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("task", Some(task)),
    DataField("arguments", Some("{ " + arguments.mkString(", ") + " }")),
    DataField("status", Some(status)),
    DataField("output", Some(output.toString)),
    DataField("started", Some(started.toString)),
    DataField("completed", Some(completed.toString))
  )

  def toSummary = DataSummary(model = "scheduledTaskRunRow", pk = id.toString, title = s"task: $task, arguments: $arguments, status: $status, started: $started")
}
