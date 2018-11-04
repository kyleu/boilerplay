package models.settings

import util.JsonSerializers._

object Setting {
  implicit val jsonEncoder: Encoder[Setting] = deriveEncoder
  implicit val jsonDecoder: Decoder[Setting] = deriveDecoder
}

final case class Setting(k: SettingKey, v: String) {
  lazy val isDefault = v == k.default
  override def toString = s"$k=$v"
  lazy val asBool = v == "true"
}
