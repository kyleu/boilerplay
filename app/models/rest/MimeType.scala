package models.rest

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed abstract class MimeType(val t: String, val isReturned: Boolean = true) extends EnumEntry

object MimeType extends Enum[MimeType] with CirceEnum[MimeType] {
  case object Text extends MimeType("text/plain")
  case object Html extends MimeType("text/html")
  case object Json extends MimeType("application/json")
  case object Xml extends MimeType("application/xml")
  case object Xhtml extends MimeType("application/xhtml+xml")
  case object Csv extends MimeType("text/csv")
  case object Css extends MimeType("text/css")
  case object JavaScript extends MimeType("text/javascript")
  case object EventStream extends MimeType("text/event-stream")
  case object Binary extends MimeType("text/event-stream")
  case object CacheManifest extends MimeType("text/cache-manifest")

  case object Form extends MimeType("application/x-www-form-urlencoded", isReturned = false)
  case object MultipartForm extends MimeType("multipart/form-data", isReturned = false)

  case class Custom(mimeType: String) extends MimeType(mimeType)

  override val values = findValues
}
