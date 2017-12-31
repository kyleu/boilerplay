package services.query

import java.sql.{Date, Time, Timestamp}
import java.time.{LocalDate, LocalDateTime, LocalTime}

import models.settings.SettingKey
import services.query.QueryService.imports._

object QueryTypes {
  implicit val settingKeyColumnType = MappedColumnType.base[SettingKey, String](b => b.toString, i => SettingKey.withName(i))

  implicit val localDateColumnType = MappedColumnType.base[LocalDate, Date](l => Date.valueOf(l), d => d.toLocalDate)
  implicit val localTimeColumnType = MappedColumnType.base[LocalTime, Time](l => Time.valueOf(l), t => t.toLocalTime)
  implicit val localDateTimeColumnType = MappedColumnType.base[LocalDateTime, Timestamp](l => Timestamp.valueOf(l), t => t.toLocalDateTime)
}
