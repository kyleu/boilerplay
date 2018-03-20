package models.rest.http

import util.JsonSerializers._

object RestCookie {
  implicit val jsonEncoder: Encoder[RestCookie] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestCookie] = deriveDecoder

  def cookiesFrom(s: String) = s.split(';').map(_.trim).filterNot(_.isEmpty).map(_.split('=').toList match {
    case k :: Nil => RestCookie(k.trim, "")
    case k :: v :: Nil => RestCookie(k.trim, v.trim)
    case x => throw new IllegalStateException(s"Cannot parse [${x.mkString("=")}] as RestCookie.")
  })

  def headerFor(cookies: Seq[RestCookie]) = if (cookies.isEmpty) { None } else { Some(cookies.map(_.toHeaderValue).mkString("; ")) }
}

case class RestCookie(k: String, v: String) {
  def toHeaderValue = s"$k=$v" // TODO encode

}
