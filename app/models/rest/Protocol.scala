package models.rest

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class Protocol(override val value: String, val port: Int) extends StringEnumEntry

object Protocol extends StringEnum[Protocol] with StringCirceEnum[Protocol] {
  case object Http extends Protocol("http", 80)
  case object Https extends Protocol("https", 443)

  override val values = findValues
}

