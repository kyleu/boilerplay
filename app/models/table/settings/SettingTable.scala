package models.table.settings

import models.settings.{Setting, SettingKey}
import services.database.SlickQueryService.imports._
import slick.jdbc.JdbcType

object SettingTable {
  implicit val settingKeyColumnType: JdbcType[SettingKey] = MappedColumnType.base[SettingKey, String](
    tmap = b => b.toString,
    tcomap = i => SettingKey.withValue(i)
  )

  val query = TableQuery[SettingTable]

  def getByPrimaryKey(key: SettingKey) = query.filter(_.key === key).result.headOption

  def insert(s: Setting) = query += s
  def update(s: Setting) = query.filter(_.key === s.key).map(_.value).update(s.value)
  def delete(key: SettingKey) = query.filter(_.key === key).delete
}

class SettingTable(tag: Tag) extends Table[Setting](tag, "setting_values") {
  import models.table.settings.SettingTable.settingKeyColumnType

  def key = column[SettingKey]("k", O.PrimaryKey)
  def value = column[String]("v")

  override val * = (key, value) <> ((Setting.apply _).tupled, Setting.unapply)
}
