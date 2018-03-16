package models.request

import java.time.LocalDateTime

import enumeratum.{CirceEnum, Enum, EnumEntry}
import io.circe.{Decoder, Encoder, Json}
import io.circe.java8.time._
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object RestRequest {
  implicit val jsonEncoder: Encoder[RestRequest] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestRequest] = deriveDecoder

  sealed abstract class ContentType(t: String) extends EnumEntry

  object ContentType extends Enum[ContentType] with CirceEnum[ContentType] {
    implicit val jsonEncoder: Encoder[ContentType] = deriveEncoder
    implicit val jsonDecoder: Decoder[ContentType] = deriveDecoder

    case object Form extends ContentType("application/x-www-form-urlencoded")
    case object Json extends ContentType("application/json")

    override val values = findValues
  }

  sealed trait Method extends EnumEntry

  object Method extends Enum[Method] with CirceEnum[Method] {
    implicit val jsonEncoder: Encoder[Method] = deriveEncoder
    implicit val jsonDecoder: Decoder[Method] = deriveDecoder

    case object Get extends Method
    case object Post extends Method

    override val values = findValues
  }
}

case class RestRequest(
    name: String,
    url: String,
    method: RestRequest.Method = RestRequest.Method.Get,
    contentType: RestRequest.ContentType = RestRequest.ContentType.Json,
    body: Option[Json] = None,
    source: String = "adhoc",
    author: String = "Unknown",
    created: LocalDateTime = util.DateUtils.now
) {
  lazy val bodyString = body.map(_.spaces2)
  lazy val bodySize = bodyString.map(_.length).getOrElse(0)
}
