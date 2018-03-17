package models.rest.config

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object RestConfigSection {
  implicit val jsonEncoder: Encoder[RestConfigSection] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestConfigSection] = deriveDecoder
}

case class RestConfigSection(
    name: String,
    defaults: Map[String, String],
    options: Map[String, Map[String, String]]
)
