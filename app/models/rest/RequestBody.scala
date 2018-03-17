package models.rest

import io.circe.syntax._
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.apache.commons.codec.binary.Base64

object RequestBody {
  implicit val jsonEncoder: Encoder[RequestBody] = {
    case json: JsonContent => json.json
    case text: TextContent => Json.obj("_t" -> "text".asJson, "content" -> text.text.asJson)
    case b: BinaryContent => Json.obj("_t" -> "binary".asJson, "content" -> Base64.encodeBase64String(b.bytes).asJson)
  }

  implicit val jsonDecoder: Decoder[RequestBody] = (c: HCursor) => {
    c.downField("_t").as[String] match {
      case Right(t) => t match {
        case "text" => Right(TextContent(
          c.downField("content").as[String].getOrElse(throw new IllegalStateException("Missing text body content."))
        ))
        case "binary" => Right(BinaryContent(Base64.decodeBase64(
          c.downField("content").as[String].getOrElse(throw new IllegalStateException("Missing binary body content."))
        )))
      }
      case Left(_) => Right(JsonContent(c.value.spaces2))
    }
  }

  case class BinaryContent(b: Array[Byte]) extends RequestBody(b)
  case class TextContent(text: String) extends RequestBody(text.getBytes)
  case class JsonContent(jsonString: String) extends RequestBody(jsonString.getBytes) {
    val json = io.circe.parser.parse(jsonString) match {
      case Right(u) => u
      case Left(x) => Json.obj("status" -> "error".asJson, "error" -> "Invalid JSON".asJson, "message" -> x.getMessage.asJson, "content" -> jsonString.asJson)
    }
  }
}

sealed abstract class RequestBody(val bytes: Array[Byte], val isText: Boolean = true) {
  val size = bytes.length
}
