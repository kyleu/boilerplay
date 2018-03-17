package models.result.data

import util.JsonSerializers._

object DataSummary {
  implicit val jsonEncoder: Encoder[DataSummary] = deriveEncoder
  implicit val jsonDecoder: Decoder[DataSummary] = deriveDecoder
}

case class DataSummary(
    model: String,
    pk: Seq[String],
    title: String
)
