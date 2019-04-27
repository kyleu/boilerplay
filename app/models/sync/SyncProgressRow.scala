/* Generated File */
package models.sync

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime

object SyncProgressRow {
  implicit val jsonEncoder: Encoder[SyncProgressRow] = (r: SyncProgressRow) => io.circe.Json.obj(
    ("key", r.key.asJson),
    ("status", r.status.asJson),
    ("message", r.message.asJson),
    ("lastTime", r.lastTime.asJson)
  )

  implicit val jsonDecoder: Decoder[SyncProgressRow] = (c: io.circe.HCursor) => for {
    key <- c.downField("key").as[String]
    status <- c.downField("status").as[String]
    message <- c.downField("message").as[String]
    lastTime <- c.downField("lastTime").as[LocalDateTime]
  } yield SyncProgressRow(key, status, message, lastTime)

  def empty(
    key: String = "",
    status: String = "",
    message: String = "",
    lastTime: LocalDateTime = DateUtils.now
  ) = {
    SyncProgressRow(key, status, message, lastTime)
  }
}

final case class SyncProgressRow(
    key: String,
    status: String,
    message: String,
    lastTime: LocalDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("key", Some(key)),
    DataField("status", Some(status)),
    DataField("message", Some(message)),
    DataField("lastTime", Some(lastTime.toString))
  )

  def toSummary = DataSummary(model = "syncProgressRow", pk = key.toString, title = s"status: $status, message: $message, lastTime: $lastTime")
}
