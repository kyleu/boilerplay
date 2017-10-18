package models.result.data

import io.circe.generic.semiauto._

object DataField {
  implicit val encoder = deriveEncoder[DataField]
  implicit val decoder = deriveDecoder[DataField]
}

case class DataField(k: String, v: Option[String])
