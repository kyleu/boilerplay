package utils

import enumeratum.{Enum, EnumEntry}

sealed abstract class Language(val code: String) extends EnumEntry

object Language extends Enum[Language] {
  case object English extends Language("en")
  case object Arabic extends Language("ar")
  case object Chinese extends Language("zh")
  case object Croation extends Language("hr")
  case object Czech extends Language("cs")
  case object Dutch extends Language("nl")
  case object French extends Language("fr")
  case object German extends Language("de")
  case object Hebrew extends Language("iw")
  case object Hindi extends Language("hi")
  case object Italian extends Language("it")
  case object Japanese extends Language("ja")
  case object Korean extends Language("ko")
  case object Polish extends Language("pl")
  case object Portugeuse extends Language("pt")
  case object Spanish extends Language("es")
  case object Swedish extends Language("sv")
  case object Thai extends Language("th")
  case object Vietnamese extends Language("vi")

  override val values = findValues
}
