/* Generated File */
package models.sync

import java.time.LocalDateTime
import models.result.data.{DataField, DataFieldModel, DataSummary}
import util.DateUtils
import util.JsonSerializers._

object SyncProgressRow {
  implicit val jsonEncoder: Encoder[SyncProgressRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[SyncProgressRow] = deriveDecoder

  def empty(key: String = "", status: String = "", message: String = "", lastTime: LocalDateTime = DateUtils.now) = {
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

  def toSummary = DataSummary(model = "syncProgressRow", pk = Seq(key.toString), title = s"$status / $message / $lastTime ($key)")
}
