package services.settings

import models.queries.settings.SettingQueries
import models.settings.{Setting, SettingKey}
import utils.FutureUtils.defaultContext
import services.database.Database

object SettingsService {
  private[this] var settings = Seq.empty[Setting]
  private[this] var settingsMap = Map.empty[SettingKey, String]

  def apply(key: SettingKey) = settingsMap.getOrElse(key, key.default)
  def asBool(key: SettingKey) = apply(key) == "true"
  def getOrSet(key: SettingKey, s: => String) = settingsMap.getOrElse(key, set(key, s))

  def load() = Database.query(SettingQueries.getAll).map(_.map(s => s.key -> s.value).toMap).map { x =>
    settingsMap = x
    settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
  }

  def isOverride(key: SettingKey) = settingsMap.isDefinedAt(key)

  def getAll = settings
  def getOverrides = settings.filter(s => isOverride(s.key))

  def set(key: SettingKey, value: String) = {
    val s = Setting(key, value)
    if (s.isDefault) {
      settingsMap = settingsMap - key
      Database.execute(SettingQueries.removeById(key))
    } else {
      Database.transaction { conn =>
        Database.query(SettingQueries.getById(key), Some(conn)).map {
          case Some(_) => Database.execute(SettingQueries.Update(s), Some(conn))
          case None => Database.execute(SettingQueries.insert(s), Some(conn))
        }
      }
      settingsMap = settingsMap + (key -> value)
    }
    settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
    value
  }

  def allowRegistration = asBool(SettingKey.AllowRegistration)
}
