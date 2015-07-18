package utils.json

import models._
import play.api.libs.functional.syntax._
import play.api.libs.json._

object RequestMessageSerializers {
  private[this] val malformedRequestReads = Json.reads[MalformedRequest]
  private[this] val pingReads = Json.reads[Ping]
  // case object [GetVersion]
  private[this] val debugInfoReads = Json.reads[DebugInfo]

  private[this] val setPreferenceReads = Json.reads[SetPreference]

  implicit val requestMessageReads: Reads[RequestMessage] = (
    (__ \ 'c).read[String] and
    (__ \ 'v).read[JsValue]
  ) { (c, v) =>
      val jsResult = c match {
        case "MalformedRequest" => malformedRequestReads.reads(v)
        case "Ping" => pingReads.reads(v)
        case "GetVersion" => JsSuccess(GetVersion)
        case "DebugInfo" => debugInfoReads.reads(v)

        case "SetPreference" => setPreferenceReads.reads(v)

        case _ => JsSuccess(MalformedRequest("UnknownType", s"c: $c, v: ${Json.stringify(v)}"))
      }
      jsResult match {
        case rm: JsSuccess[RequestMessage @unchecked] => rm.get
        case e: JsError => throw new IllegalArgumentException(s"Error parsing json for [$c]: $JsError")
      }
    }
}
