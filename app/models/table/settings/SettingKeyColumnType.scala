/* Generated File */
package models.table.settings

import models.settings.SettingKey
import services.database.SlickQueryService.imports._
import slick.jdbc.JdbcType

object SettingKeyColumnType {
  implicit val settingKeyColumnType: JdbcType[SettingKey] = MappedColumnType.base[SettingKey, String](_.value, SettingKey.withValue)
}
