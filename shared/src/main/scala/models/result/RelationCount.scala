package models.result

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto._

object RelationCount {
  implicit val jsonEncoder: Encoder[RelationCount] = deriveEncoder
  implicit val jsonDecoder: Decoder[RelationCount] = deriveDecoder
}

case class RelationCount(model: String, field: String, count: Int)
