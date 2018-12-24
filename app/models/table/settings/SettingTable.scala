/* Generated File */
package models.table.settings

import models.settings.{Setting, SettingKeyType}
import models.table.settings.SettingKeyTypeColumnType.settingKeyTypeColumnType
import services.database.slick.SlickQueryService.imports._

object SettingTable {
  val query = TableQuery[SettingTable]

  def getByPrimaryKey(k: SettingKeyType) = query.filter(_.k === k).result.headOption
  def getByPrimaryKeySeq(kSeq: Seq[SettingKeyType]) = query.filter(_.k.inSet(kSeq)).result
}

class SettingTable(tag: slick.lifted.Tag) extends Table[Setting](tag, "setting_values") {
  val k = column[SettingKeyType]("k", O.PrimaryKey)
  val v = column[String]("v")

  override val * = (k, v) <> (
    (Setting.apply _).tupled,
    Setting.unapply
  )
}

