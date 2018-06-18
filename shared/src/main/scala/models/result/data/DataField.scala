package models.result.data

import util.JsonSerializers._

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object DataField {
  implicit val jsonEncoder: Encoder[DataField] = deriveEncoder
  implicit val jsonDecoder: Decoder[DataField] = deriveDecoder
}

@JSExportTopLevel("DataField")
case class DataField(@JSExport k: String, v: Option[String]) {
  @JSExport val value = v.getOrElse(util.NullUtils.inst)
}
