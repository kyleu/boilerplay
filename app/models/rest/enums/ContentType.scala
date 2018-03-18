package models.rest.enums

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed abstract class ContentType(val t: String) extends EnumEntry

object ContentType extends Enum[ContentType] with CirceEnum[ContentType] {
  case object Default extends ContentType(util.NullUtils.str)
  case object Form extends ContentType(MimeType.Form.mt)
  case object MultipartForm extends ContentType(MimeType.MultipartForm.mt)
  case object Json extends ContentType(MimeType.Json.mt)

  override val values = findValues
}
