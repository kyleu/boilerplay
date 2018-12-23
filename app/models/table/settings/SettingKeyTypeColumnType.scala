/* Generated File */
package models.table.settings

import models.settings.SettingKeyType
import services.database.slick.SlickQueryService.imports._
import slick.jdbc.JdbcType

object SettingKeyTypeColumnType {
  implicit val settingKeyTypeColumnType: JdbcType[SettingKeyType] = MappedColumnType.base[SettingKeyType, String](_.value, SettingKeyType.withValue)
}
