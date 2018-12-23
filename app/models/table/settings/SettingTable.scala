package models.table.settings

import models.settings.{Setting, SettingKeyType}
import services.database.slick.SlickQueryService.imports._
import slick.jdbc.JdbcType

object SettingTable {
  implicit val settingKeyColumnType: JdbcType[SettingKeyType] = MappedColumnType.base[SettingKeyType, String](
    tmap = b => b.toString,
    tcomap = i => SettingKeyType.withValue(i)
  )

  val query = TableQuery[SettingTable]

  def getByPrimaryKey(key: SettingKeyType) = query.filter(_.key === key).result.headOption

  def insert(s: Setting) = query += s
  def update(s: Setting) = query.filter(_.key === s.k).map(_.value).update(s.v)
  def delete(key: SettingKeyType) = query.filter(_.key === key).delete
}

class SettingTable(tag: Tag) extends Table[Setting](tag, "setting_values") {
  import models.table.settings.SettingTable.settingKeyColumnType

  def key = column[SettingKeyType]("k", O.PrimaryKey)
  def value = column[String]("v")

  override val * = (key, value) <> ((Setting.apply _).tupled, Setting.unapply)
}
