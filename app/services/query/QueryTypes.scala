package services.query

import models.settings.SettingKey
import services.query.QueryService.imports._

object QueryTypes {
  implicit val settingKeyColumnType = MappedColumnType.base[SettingKey, String](b => b.toString, i => SettingKey.withName(i))
}
