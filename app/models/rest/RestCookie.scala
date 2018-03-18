package models.rest

import util.JsonSerializers._

object RestCookie {
  def cookiesFrom(s: String) = s.split(';').map(_.trim).filterNot(_.isEmpty).map(_.split('=').toList match {
    case k :: Nil => RestCookie(k.trim, "")
    case k :: v :: Nil => RestCookie(k.trim, v.trim)
    case x => throw new IllegalStateException(s"Cannot parse [${x.mkString("=")}] as RestCookie.")
  })

  def headerFor(cookies: Seq[RestCookie]) = if (cookies.isEmpty) { None } else { Some(cookies.map(_.toHeaderValue).mkString("; ")) }

  implicit val jsonEncoder: Encoder[RestCookie] = deriveEncoder
  implicit val jsonDecoder: Decoder[RestCookie] = deriveDecoder
}

case class RestCookie(k: String, v: String) {
  def toHeaderValue = s"$k=$v" // TODO encode

}
