/* Generated File */
package models.table.settings

import models.settings.SettingKey
import services.database.SlickQueryService.imports._

object SettingKeyColumnType {
  implicit val settingKeyColumnType = MappedColumnType.base[SettingKey, String](_.value, SettingKey.withValue)
}
