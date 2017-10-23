package services.settings

import models.queries.settings.SettingQueries
import models.settings.{Setting, SettingKey}
import services.database.ApplicationDatabase
import util.Logging
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class SettingsService @javax.inject.Inject() (tracing: TracingService) extends Logging {
  private[this] var settings = Seq.empty[Setting]
  private[this] var settingsMap = Map.empty[SettingKey, String]

  def apply(key: SettingKey) = settingsMap.getOrElse(key, key.default)
  def asBool(key: SettingKey) = apply(key) == "true"
  def getOrSet(key: SettingKey, s: => String)(implicit trace: TraceData) = tracing.traceBlocking("get.or.set") { td =>
    settingsMap.get(key) match {
      case Some(v) => v
      case None => set(key, s)(td)
    }
  }

  def load()(implicit trace: TraceData) = tracing.traceBlocking("settings.service.load") { td =>
    val x = ApplicationDatabase.query(SettingQueries.getAll())(td).map(s => s.key -> s.value).toMap
    settingsMap = x
    settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
    log.debug(s"Loaded [${x.size}] system settings.")
  }

  def isOverride(key: SettingKey) = settingsMap.isDefinedAt(key)

  def getAll = settings
  def getOverrides = settings.filter(s => isOverride(s.key))

  def set(key: SettingKey, value: String)(implicit trace: TraceData) = tracing.traceBlocking("settings.service.set") { td =>
    val s = Setting(key, value)
    if (s.isDefault) {
      settingsMap = settingsMap - key
      ApplicationDatabase.execute(SettingQueries.removeByPrimaryKey(key))
    } else {
      ApplicationDatabase.transaction { (txTd, conn) =>
        ApplicationDatabase.query(SettingQueries.getByPrimaryKey(key), Some(conn))(txTd) match {
          case Some(_) => ApplicationDatabase.execute(SettingQueries.Update(s), Some(conn))(txTd)
          case None => ApplicationDatabase.execute(SettingQueries.insert(s), Some(conn))(txTd)
        }
        settingsMap = settingsMap + (key -> value)
      }(td)
    }
    settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
    value
  }

  def allowRegistration = asBool(SettingKey.AllowRegistration)
}
