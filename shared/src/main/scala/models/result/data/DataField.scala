package models.result.data

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object DataField {
  implicit val jsonEncoder: Encoder[DataField] = deriveEncoder
  implicit val jsonDecoder: Decoder[DataField] = deriveDecoder
}

@JSExportTopLevel(util.Config.projectId + ".DataField")
case class DataField(@JSExport k: String, v: Option[String]) {
  @JSExport val value = v.getOrElse(util.NullUtils.inst)
}
