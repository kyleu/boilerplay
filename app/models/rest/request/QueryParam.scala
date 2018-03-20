package models.rest.request

import util.JsonSerializers._

object QueryParam {
  implicit val jsonEncoder: Encoder[QueryParam] = deriveEncoder
  implicit val jsonDecoder: Decoder[QueryParam] = deriveDecoder
}

case class QueryParam(k: String, v: String) {
  override def toString = s"$k=$v" // TODO encode
}
