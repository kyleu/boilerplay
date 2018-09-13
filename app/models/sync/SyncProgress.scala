/* Generated File */
package models.sync

import java.time.LocalDateTime
import models.result.data.{DataField, DataFieldModel, DataSummary}
import util.JsonSerializers._

object SyncProgress {
  implicit val jsonEncoder: Encoder[SyncProgress] = deriveEncoder
  implicit val jsonDecoder: Decoder[SyncProgress] = deriveDecoder

  def empty(key: String = "", status: String = "", message: String = "", lastTime: LocalDateTime = util.DateUtils.now) = {
    SyncProgress(key, status, message, lastTime)
  }
}

final case class SyncProgress(
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

  def toSummary = DataSummary(model = "syncProgress", pk = Seq(key.toString), title = s"$status / $message / $lastTime ($key)")
}
