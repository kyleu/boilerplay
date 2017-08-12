package models.user

import enumeratum._

sealed abstract class Permission(val id: String) extends EnumEntry {
  override val toString = id
}

object Permission extends Enum[Permission] with CirceEnum[Permission] {
  case object Visitor extends Permission("visitor")
  case object User extends Permission("user")
  case object Administrator extends Permission("admin")
  case object Private extends Permission("private")

  override def values = findValues
}
