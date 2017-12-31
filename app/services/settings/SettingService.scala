package services.settings

import models.settings.{Setting, SettingKey}
import services.query.MainDatabase
import util.Logging
import util.tracing.{TraceData, TracingService}
import services.query.QueryService.imports._

import util.FutureUtils.serviceContext

@javax.inject.Singleton
class SettingService @javax.inject.Inject() (tracing: TracingService) extends Logging {
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

  def load()(implicit trace: TraceData) = tracing.trace("settings.service.load") { _ =>
    MainDatabase.db.run(SettingTable.query.result).map { set =>
      settingsMap = set.map(s => s.key -> s.value).toMap
      settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
      log.info(s"Loaded [${set.size}] system settings.")
    }
  }

  def isOverride(key: SettingKey) = settingsMap.isDefinedAt(key)

  def getAll = settings
  def getOverrides = settings.filter(s => isOverride(s.key))

  def set(key: SettingKey, value: String)(implicit trace: TraceData) = tracing.trace("settings.service.set") { _ =>
    val s = Setting(key, value)
    val ret = if (s.isDefault) {
      settingsMap = settingsMap - key
      MainDatabase.db.run(SettingTable.delete(key))
    } else {
      MainDatabase.db.run(SettingTable.getByPrimaryKey(key)).flatMap {
        case Some(_) => MainDatabase.db.run(SettingTable.update(s))
        case None => MainDatabase.db.run(SettingTable.insert(s))
      }.map { _ =>
        settingsMap = settingsMap + (key -> value)
      }
    }
    ret.map { _ =>
      settings = SettingKey.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
      value
    }
  }

  def allowRegistration = asBool(SettingKey.AllowRegistration)
}
