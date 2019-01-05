package services.audit

import com.google.inject.Injector
import com.kyleu.projectile.models.auth.UserCredentials
import com.kyleu.projectile.models.result.data.DataFieldModel
import services.audit.AuditArgs._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData

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
    case "auditrecordrow" => injector.getInstance(classOf[services.audit.AuditRecordRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "auditrow" => injector.getInstance(classOf[services.audit.AuditRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "flywayschemahistoryrow" => injector.getInstance(classOf[services.ddl.FlywaySchemaHistoryRowService]).getByPrimaryKey(creds, longArg(arg(0)))
    case "noterow" => injector.getInstance(classOf[services.note.NoteRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "oauth2inforow" => injector.getInstance(classOf[services.auth.Oauth2InfoRowService]).getByPrimaryKey(creds, stringArg(arg(0)), stringArg(arg(1)))
    case "passwordinforow" => injector.getInstance(classOf[services.auth.PasswordInfoRowService]).getByPrimaryKey(creds, stringArg(arg(0)), stringArg(arg(1)))
    case "scheduledtaskrunrow" => injector.getInstance(classOf[services.task.ScheduledTaskRunRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    case "setting" => injector.getInstance(classOf[services.settings.SettingService]).getByPrimaryKey(creds, enumArg(models.settings.SettingKeyType)(arg(0)))
    case "syncprogressrow" => injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByPrimaryKey(creds, stringArg(arg(0)))
    case "systemuserrow" => injector.getInstance(classOf[services.user.SystemUserRowService]).getByPrimaryKey(creds, uuidArg(arg(0)))
    /* End registry lookups */
    case _ =>
      log.warn(s"Attempted to load invalid object type [$key:${arg(0)}].")
      Future.successful(None)
  }
}
