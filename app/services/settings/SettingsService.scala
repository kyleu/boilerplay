package services.settings

import models.queries.settings.SettingQueries
import models.settings.{Setting, SettingKey}
import util.FutureUtils.databaseContext
import services.database.Database
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class SettingsService @javax.inject.Inject() (tracing: TracingService) {
  private[this] var settings = Seq.empty[Setting]
  private[this] var settingsMap = Map.empty[SettingKey, String]

  def apply(key: SettingKey) = settingsMap.getOrElse(key, key.default)
  def asBool(key: SettingKey) = apply(key) == "true"
  def getOrSet(key: SettingKey, s: => String)(implicit trace: TraceData) = tracing.trace("get.or.set")(_ => settingsMap.getOrElse(key, set(key, s)))

  def load()(implicit trace: TraceData) = tracing.trace("get.or.set") { tn =>
    Database.query(SettingQueries.getAll())(tn).map(_.map(s => s.key -> s.value).toMap).map { x =>
      settingsMap = x
      settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
    }
  }

  def isOverride(key: SettingKey) = settingsMap.isDefinedAt(key)

  def getAll = Future.successful(settings)
  def getOverrides = settings.filter(s => isOverride(s.key))

  def set(key: SettingKey, value: String)(implicit trace: TraceData) = {
    val s = Setting(key, value)
    if (s.isDefault) {
      settingsMap = settingsMap - key
      Database.execute(SettingQueries.removeByPrimaryKey(key))
    } else {
      Database.transaction { conn =>
        Database.query(SettingQueries.getByPrimaryKey(key), Some(conn)).map {
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
