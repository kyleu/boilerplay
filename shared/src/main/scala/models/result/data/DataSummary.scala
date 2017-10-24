package models.result.data

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

object DataSummary {
  implicit val jsonEncoder: Encoder[DataSummary] = deriveEncoder
  implicit val jsonDecoder: Decoder[DataSummary] = deriveDecoder
}

case class DataSummary(
    model: String,
    pk: Seq[String],
    title: String
)
