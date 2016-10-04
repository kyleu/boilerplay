package models.user

import enumeratum.{Enum, EnumEntry}

sealed abstract class Language(val code: String) extends EnumEntry

object Language extends Enum[Language] {
  case object English extends Language("en")

  override val values = findValues
}
