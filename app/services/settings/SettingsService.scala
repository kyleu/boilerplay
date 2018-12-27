package services.settings

import models.ProjectileContext.serviceContext
import models.auth.Credentials
import models.result.data.DataField
import models.settings.{Setting, SettingKeyType}
import models.table.settings.SettingTable
import services.database.ApplicationDatabase
import services.database.slick.SlickQueryService.imports._
import util.Logging
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

@javax.inject.Singleton
class SettingsService @javax.inject.Inject() (tracing: TracingService, svc: SettingService) extends Logging {
  private[this] var settings = Seq.empty[Setting]
  private[this] var settingsMap = Map.empty[SettingKeyType, String]

  def apply(key: SettingKeyType) = settingsMap.getOrElse(key, key.default)
  def asBool(key: SettingKeyType) = apply(key) == "true"
  def getOrSet(key: SettingKeyType, s: => String)(implicit trace: TraceData) = tracing.traceBlocking("get.or.set") { td =>
    settingsMap.get(key) match {
      case Some(v) => Future.successful(v)
      case None => set(key, s)(td)
    }
  }

  def load()(implicit trace: TraceData) = tracing.trace("settings.service.load") { _ =>
    ApplicationDatabase.slick.run(SettingTable.query.result).map { set =>
      settingsMap = set.map(s => s.k -> s.v).toMap
      settings = SettingKeyType.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
      log.info(s"Loaded [${set.size}] system settings.")
    }
  }

  def isOverride(key: SettingKeyType) = settingsMap.isDefinedAt(key)

  def getAll = settings
  def getOverrides = settings.filter(s => isOverride(s.k))

  def set(key: SettingKeyType, value: String)(implicit trace: TraceData) = tracing.trace("settings.service.set") { _ =>
    val s = Setting(key, value)
    val creds = Credentials.system
    val ret = if (s.isDefault) {
      settingsMap = settingsMap - key
      svc.remove(creds, key)
    } else {
      svc.getByPrimaryKey(creds, key).flatMap {
        case Some(_) => svc.update(creds, key, Seq(DataField("v", Some(s.v))))
        case None => svc.insert(creds, s)
      }.map { _ =>
        settingsMap = settingsMap + (key -> value)
      }
    }
    ret.map { _ =>
      settings = SettingKeyType.values.map(k => Setting(k, settingsMap.getOrElse(k, k.default)))
      value
    }
  }

  def allowRegistration = asBool(SettingKeyType.AllowRegistration)
}
