package services.settings

import models.queries.settings.SettingQueries
import models.settings.{Setting, SettingKey}
import util.FutureUtils.databaseContext
import services.database.SystemDatabase
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class SettingsService @javax.inject.Inject() (tracing: TracingService) {
  private[this] var settings = Seq.empty[Setting]
  private[this] var settingsMap = Map.empty[SettingKey, String]

  def apply(key: SettingKey) = settingsMap.getOrElse(key, key.default)
  def asBool(key: SettingKey) = apply(key) == "true"
  def getOrSet(key: SettingKey, s: => String)(implicit trace: TraceData) = tracing.trace("get.or.set") { td =>
    settingsMap.get(key) match {
      case Some(v) => Future.successful(v)
      case None => set(key, s)(td)
    }
  }

  def load()(implicit trace: TraceData) = tracing.trace("settings.service.load") { td =>
    SystemDatabase.query(SettingQueries.getAll())(td).map(_.map(s => s.key -> s.value).toMap).map { x =>
      settingsMap = x
      settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
    }
  }

  def isOverride(key: SettingKey) = settingsMap.isDefinedAt(key)

  def getAll = Future.successful(settings)
  def getOverrides = settings.filter(s => isOverride(s.key))

  def set(key: SettingKey, value: String)(implicit trace: TraceData) = tracing.trace("settings.service.set") { td =>
    val s = Setting(key, value)
    val f = if (s.isDefault) {
      settingsMap = settingsMap - key
      SystemDatabase.execute(SettingQueries.removeByPrimaryKey(key))
    } else {
      SystemDatabase.transaction { (txTd, conn) =>
        SystemDatabase.query(SettingQueries.getByPrimaryKey(key), Some(conn))(txTd).map {
          case Some(_) => SystemDatabase.execute(SettingQueries.Update(s), Some(conn))(txTd)
          case None => SystemDatabase.execute(SettingQueries.insert(s), Some(conn))(txTd)
        }.map(_ => settingsMap = settingsMap + (key -> value))
      }(td)
    }
    f.map { _ =>
      settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
      value
    }
  }

  def allowRegistration = asBool(SettingKey.AllowRegistration)
}
