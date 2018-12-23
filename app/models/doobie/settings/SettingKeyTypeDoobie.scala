/* Generated File */
package models.doobie.settings

import models.settings.SettingKeyType
import services.database.doobie.DoobieQueryService.Imports._

object SettingKeyTypeDoobie {
  implicit val settingKeyTypeMeta: Meta[SettingKeyType] = pgEnumStringOpt("SettingKeyType", SettingKeyType.withValueOpt, _.value)
}
