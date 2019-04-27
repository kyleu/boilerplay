/* Generated File */
package models.task

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import io.circe.Json
import java.time.LocalDateTime
import java.util.UUID

object ScheduledTaskRunRow {
  implicit val jsonEncoder: Encoder[ScheduledTaskRunRow] = (r: ScheduledTaskRunRow) => io.circe.Json.obj(
    ("id", r.id.asJson),
    ("task", r.task.asJson),
    ("arguments", r.arguments.asJson),
    ("status", r.status.asJson),
    ("output", r.output.asJson),
    ("started", r.started.asJson),
    ("completed", r.completed.asJson)
  )

  implicit val jsonDecoder: Decoder[ScheduledTaskRunRow] = (c: io.circe.HCursor) => for {
    id <- c.downField("id").as[UUID]
    task <- c.downField("task").as[String]
    arguments <- c.downField("arguments").as[List[String]]
    status <- c.downField("status").as[String]
    output <- c.downField("output").as[Json]
    started <- c.downField("started").as[LocalDateTime]
    completed <- c.downField("completed").as[LocalDateTime]
  } yield ScheduledTaskRunRow(id, task, arguments, status, output, started, completed)

  def empty(
    id: UUID = UUID.randomUUID,
    task: String = "",
    arguments: List[String] = List.empty,
    status: String = "",
    output: Json = Json.obj(),
    started: LocalDateTime = DateUtils.now,
    completed: LocalDateTime = DateUtils.now
  ) = {
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
