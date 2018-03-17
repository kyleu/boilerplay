package models.rest

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

object RestConfigSection {
  implicit val jsonEncoder: Encoder[RestConfigSection] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestConfigSection] = deriveDecoder
}

case class RestConfigSection(
    name: String,
    defaults: Map[String, String],
    options: Map[String, Map[String, String]]
)
