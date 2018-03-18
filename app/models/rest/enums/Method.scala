package models.rest.enums

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class Method(override val value: String) extends StringEnumEntry

object Method extends StringEnum[Method] with StringCirceEnum[Method] {
  case object Get extends Method("get")
  case object Head extends Method("head")
  case object Post extends Method("post")
  case object Put extends Method("put")
  case object Delete extends Method("delete")
  case object Connect extends Method("connect")
  case object Options extends Method("options")
  case object Patch extends Method("patch")

  override val values = findValues
}
