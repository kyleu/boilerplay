package models.rest.http

import models.rest.request.QueryParam
import util.JsonSerializers._

object RestUrl {
  implicit val jsonEncoder: Encoder[RestUrl] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestUrl] = deriveDecoder
}

case class RestUrl(
  protocol: Protocol = Protocol.Http,
  domain: String = "localhost",
  port: Option[Int] = None,
  path: String = "",
  queryParams: Seq[QueryParam] = Nil,
) {
  lazy val queryParamString = if (queryParams.isEmpty) { "" } else { "?" + queryParams.mkString("&") }
  lazy val location = if (path.isEmpty) { "/" } else { path } + queryParamString

  override lazy val toString = s"${protocol.value}://$domain${port.map(":" + _).getOrElse("")}/${location.stripPrefix("/")}"
}
