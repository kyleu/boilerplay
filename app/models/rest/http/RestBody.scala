package models.rest.http

import java.util.Base64

import io.circe.syntax._
import io.circe.{HCursor, Json}
import util.JsonSerializers._

object RestBody {
  implicit val jsonEncoder: Encoder[RestBody] = {
    case json: JsonContent => json.json
    case text: TextContent => Json.obj("_t" -> "text".asJson, "content" -> text.text.asJson)
    case b: BinaryContent => Json.obj("_t" -> "binary".asJson, "content" -> Base64.getEncoder.encode(b.bytes).asJson)
  }

  implicit val jsonDecoder: Decoder[RestBody] = (c: HCursor) => {
    c.downField("_t").as[String] match {
      case Right(t) => t match {
        case "text" => Right(TextContent(
          c.downField("content").as[String].getOrElse(throw new IllegalStateException("Missing text body content."))
        ))
        case "binary" => Right(BinaryContent(Base64.getDecoder.decode(
          c.downField("content").as[String].getOrElse(throw new IllegalStateException("Missing binary body content."))
        )))
      }
      case Left(_) => Right(JsonContent(c.value.spaces2))
    }
  }

  case class BinaryContent(b: Array[Byte]) extends RestBody(b)
  case class TextContent(text: String) extends RestBody(text.getBytes)
  case class JsonContent(jsonString: String) extends RestBody(jsonString.getBytes) {
    val json = io.circe.parser.parse(jsonString) match {
      case Right(u) => u
      case Left(x) => Json.obj("status" -> "error".asJson, "error" -> "Invalid JSON".asJson, "message" -> x.getMessage.asJson, "content" -> jsonString.asJson)
    }
  }

  def fromBytes(mimeType: Option[MimeType], bytes: Array[Byte]) = mimeType match {
    case Some(MimeType.Json) => JsonContent(new String(bytes))
    case _ => TextContent(new String(bytes)) // TODO others
  }
}

sealed abstract class RestBody(val bytes: Array[Byte], val isText: Boolean = true) {
  val size = bytes.length
}
