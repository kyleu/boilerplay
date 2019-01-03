package models.settings

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.JsonSerializers._

object Setting {
  implicit val jsonEncoder: Encoder[Setting] = deriveEncoder
  implicit val jsonDecoder: Decoder[Setting] = deriveDecoder

  def empty(k: SettingKeyType = SettingKeyType.DefaultNewUserRole, v: String = "") = Setting(k, v)
}

final case class Setting(k: SettingKeyType, v: String) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("k", Some(k.toString)),
    DataField("v", Some(v))
  )
  lazy val isDefault = v == k.default
  override def toString = s"$k=$v"
  lazy val asBool = v == "true"

  def toSummary = DataSummary(model = "flywaySchemaHistory", pk = k.toString, title = s"$k / $v")
}
