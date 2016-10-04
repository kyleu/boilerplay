package models.settings

import enumeratum._

sealed abstract class SettingKey(val id: String, val title: String, val description: String, val default: String) extends EnumEntry {
  override def toString = id
}

object SettingKey extends Enum[SettingKey] {
  case object AllowRegistration extends SettingKey(
    id = "allow-registration",
    title = "Allow Registration",
    description = "Determines if users are allowed to sign themselves up for the system.",
    default = "true"
  )

  case object DefaultNewUserRole extends SettingKey(
    id = "default-new-user-role",
    title = "Default New User Role",
    description = "Determines the role to assign newly-registered users.",
    default = "admin"
  )

  override val values = findValues
}
