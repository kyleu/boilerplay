package services.settings

import models.settings.{Setting, SettingKey}
import services.query.QueryService.imports._
import services.query.QueryTypes._

object SettingTable {
  val query = TableQuery[SettingTable]

  def getByPrimaryKey(key: SettingKey) = query.filter(_.key === key).result.headOption

  def insert(s: Setting) = query += s
  def update(s: Setting) = query.filter(_.key === s.key).map(_.value).update(s.value)
  def delete(key: SettingKey) = query.filter(_.key === key).delete
}

class SettingTable(tag: Tag) extends Table[Setting](tag, "setting_values") {
  import services.query.QueryTypes.settingKeyColumnType

  def key = column[SettingKey]("k", O.PrimaryKey)
  def value = column[String]("v")

  override val * = (key, value) <> (Setting.tupled, Setting.unapply)
}
