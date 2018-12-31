/* Generated File */
package models.table.settings

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import models.settings.SettingKeyType
import slick.jdbc.JdbcType

object SettingKeyTypeColumnType {
  implicit val settingKeyTypeColumnType: JdbcType[SettingKeyType] = MappedColumnType.base[SettingKeyType, String](_.value, SettingKeyType.withValue)
}
