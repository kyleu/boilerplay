package models.task.scheduled

import java.time.LocalDateTime
import java.util.UUID

import com.kyleu.projectile.util.DateUtils
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._
import io.circe.{Decoder, Encoder}

object ScheduledTaskOutput {
  implicit val jsonEncoder: Encoder[ScheduledTaskOutput] = deriveEncoder
  implicit val jsonDecoder: Decoder[ScheduledTaskOutput] = deriveDecoder

  final case class Log(msg: String, offset: Int)

  object Log {
    implicit val jsonEncoder: Encoder[ScheduledTaskOutput.Log] = deriveEncoder
    implicit val jsonDecoder: Decoder[ScheduledTaskOutput.Log] = deriveDecoder
  }
}

final case class ScheduledTaskOutput(
    userId: UUID,
    username: String,
    status: String,
    logs: Seq[ScheduledTaskOutput.Log],
    start: LocalDateTime,
    end: LocalDateTime
) {
  val durationMs = (DateUtils.toMillis(end) - DateUtils.toMillis(start)).toInt
}
