package models.result.data

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

object DataField {
  implicit val jsonEncoder: Encoder[DataField] = deriveEncoder
  implicit val jsonDecoder: Decoder[DataField] = deriveDecoder
}

case class DataField(k: String, v: Option[String])
