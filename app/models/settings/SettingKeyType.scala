package models.settings

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class SettingKeyType(override val value: String, val title: String, val description: String, val default: String) extends StringEnumEntry

object SettingKeyType extends StringEnum[SettingKeyType] with StringCirceEnum[SettingKeyType] {
  case object AllowRegistration extends SettingKeyType(
    value = "AllowRegistration",
    title = "Allow Registration",
    description = "Determines if users are allowed to sign themselves up for the system.",
    default = "true"
  )
  case object DefaultNewUserRole extends SettingKeyType(
    value = "DefaultNewUserRole",
    title = "Default New User Role",
    description = "Determines the role to assign newly-registered users.",
    default = "admin"
  )

  override val values = findValues
}
