/* Generated File */
package models.sync

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime

object SyncProgressRow {
  implicit val jsonEncoder: Encoder[SyncProgressRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[SyncProgressRow] = deriveDecoder

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
