/* Generated File */
package models.doobie.settings

import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.settings.SettingKeyType

object SettingKeyTypeDoobie {
  implicit val settingKeyTypeMeta: Meta[SettingKeyType] = pgEnumStringOpt("SettingKeyType", SettingKeyType.withValueOpt, _.value)
}
