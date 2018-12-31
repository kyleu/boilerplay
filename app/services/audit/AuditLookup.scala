package services.audit

import com.google.inject.Injector
import models.auth.UserCredentials
import com.kyleu.projectile.models.result.data.DataFieldModel
import services.audit.AuditArgs._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData
import services.ddl.FlywaySchemaHistoryRowService
import services.note.NoteRowService
import services.settings.SettingService
import services.sync.SyncProgressRowService
import services.task.ScheduledTaskRunRowService
import services.user.SystemUserService

import scala.concurrent.Future

@javax.inject.Singleton
class AuditLookup @javax.inject.Inject() (injector: Injector) extends Logging {
  def getByPk(creds: UserCredentials, model: String, pk: String*)(implicit traceData: TraceData) = {
    getModel(creds, model, getArg(pk.toIndexedSeq, _))
  }

  private[this] def getModel(
    creds: UserCredentials, key: String, arg: Int => String
  )(implicit traceData: TraceData): Future[Option[DataFieldModel]] = key.toLowerCase match {
    /* Start registry lookups */
    /* Projectile export section [boilerplay] */
    case "auditrecordrow" => injector.getInstance(classOf[AuditRecordRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "auditrow" => injector.getInstance(classOf[AuditRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "flywayschemahistoryrow" => injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByPrimaryKey(creds, longArg(arg(0)))
    case "noterow" => injector.getInstance(classOf[NoteRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "scheduledtaskrunrow" => injector.getInstance(classOf[ScheduledTaskRunRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "setting" => injector.getInstance(classOf[SettingService]).getByPrimaryKey(creds, enumArg(models.settings.SettingKeyType)(arg(0)))
    case "syncprogressrow" => injector.getInstance(classOf[SyncProgressRowService]).getByPrimaryKey(creds, stringArg(arg(0)))
    case "systemuser" => injector.getInstance(classOf[SystemUserService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    /* End registry lookups */
    case _ =>
      log.warn(s"Attempted to load invalid object type [$key:${arg(0)}].")
      Future.successful(None)
  }
}
