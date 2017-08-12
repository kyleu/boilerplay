package models.settings

import enumeratum._

sealed abstract class SettingKey(val title: String, val description: String, val default: String) extends EnumEntry

object SettingKey extends Enum[SettingKey] with CirceEnum[SettingKey] {
  case object AllowRegistration extends SettingKey(
    title = "Allow Registration",
    description = "Determines if users are allowed to sign themselves up for the system.",
    default = "true"
  )

  case object DefaultNewUserRole extends SettingKey(
    title = "Default New User Role",
    description = "Determines the role to assign newly-registered users.",
    default = "admin"
  )

  override val values = findValues
}
