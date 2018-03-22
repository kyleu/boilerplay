package models.settings

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class SettingKey(override val value: String, val title: String, val description: String, val default: String) extends StringEnumEntry

object SettingKey extends StringEnum[SettingKey] with StringCirceEnum[SettingKey] {
  case object AllowRegistration extends SettingKey(
    value = "AllowRegistration",
    title = "Allow Registration",
    description = "Determines if users are allowed to sign themselves up for the system.",
    default = "true"
  )
  case object DefaultNewUserRole extends SettingKey(
    value = "DefaultNewUserRole",
    title = "Default New User Role",
    description = "Determines the role to assign newly-registered users.",
    default = "admin"
  )

  override val values = findValues
}
