package models.settings

case class Setting(key: SettingKey, value: String) {
  lazy val isDefault = value == key.default
  override def toString = s"$key=$value"
  lazy val asBool = value == "true"
}
