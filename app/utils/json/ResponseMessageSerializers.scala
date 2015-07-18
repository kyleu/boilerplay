package utils.json

import models._
import play.api.libs.json._

object ResponseMessageSerializers {
  private[this] val serverErrorWrites = Json.writes[ServerError]
  private[this] val pongWrites = Json.writes[Pong]
  private[this] val versionResponseWrites = Json.writes[VersionResponse]
  private[this] val disconnectedResponseWrites = Json.writes[Disconnected]

  implicit val responseMessageWrites = Writes[ResponseMessage] { r: ResponseMessage =>
    val json = r match {
      case se: ServerError => serverErrorWrites.writes(se)
      case p: Pong => pongWrites.writes(p)
      case vr: VersionResponse => versionResponseWrites.writes(vr)
      case SendDebugInfo => JsObject(Nil)
      case d: Disconnected => disconnectedResponseWrites.writes(d)

      case _ => throw new IllegalArgumentException(s"Unhandled ResponseMessage type [${r.getClass.getSimpleName}].")
    }
    JsObject(Seq("c" -> JsString(r.getClass.getSimpleName.replace("$", "")), "v" -> json))
  }

  val messageSetWrites = Writes[MessageSet] { ms: MessageSet =>
    JsObject(Seq("c" -> JsString("MessageSet"), "v" -> JsObject(Seq("messages" -> JsArray(ms.messages.map(responseMessageWrites.writes))))))
  }
}
