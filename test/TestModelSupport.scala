/* Generated File */
import com.kyleu.projectile.models.result.data.DataFieldModel
import com.kyleu.projectile.services.Credentials
import com.kyleu.projectile.util.tracing.TraceData

object TestModelSupport {
  private[this] val creds = Credentials.noop
  private[this] implicit val td: TraceData = TraceData.noop

  def insert(m: DataFieldModel) = m match {
    case model: models.audit.AuditRow => TestServices.auditRowService.insert(creds, model)
    case model: models.audit.AuditRecordRow => TestServices.auditRecordRowService.insert(creds, model)
    case model: models.ddl.FlywaySchemaHistoryRow => TestServices.flywaySchemaHistoryRowService.insert(creds, model)
    case model: models.note.NoteRow => TestServices.noteRowService.insert(creds, model)
    case model: models.auth.Oauth2InfoRow => TestServices.oauth2InfoRowService.insert(creds, model)
    case model: models.auth.PasswordInfoRow => TestServices.passwordInfoRowService.insert(creds, model)
    case model: models.task.ScheduledTaskRunRow => TestServices.scheduledTaskRunRowService.insert(creds, model)
    case model: models.settings.Setting => TestServices.settingService.insert(creds, model)
    case model: models.sync.SyncProgressRow => TestServices.syncProgressRowService.insert(creds, model)
    case model: models.auth.SystemUserRow => TestServices.systemUserRowService.insert(creds, model)
    case model => throw new IllegalStateException(s"Unable to insert unhandled model [$model]")
  }

  def remove(m: DataFieldModel) = m match {
    case model: models.audit.AuditRow => TestServices.auditRowService.remove(creds, model.id)
    case model: models.audit.AuditRecordRow => TestServices.auditRecordRowService.remove(creds, model.id)
    case model: models.ddl.FlywaySchemaHistoryRow => TestServices.flywaySchemaHistoryRowService.remove(creds, model.installedRank)
    case model: models.note.NoteRow => TestServices.noteRowService.remove(creds, model.id)
    case model: models.auth.Oauth2InfoRow => TestServices.oauth2InfoRowService.remove(creds, model.provider, model.key)
    case model: models.auth.PasswordInfoRow => TestServices.passwordInfoRowService.remove(creds, model.provider, model.key)
    case model: models.task.ScheduledTaskRunRow => TestServices.scheduledTaskRunRowService.remove(creds, model.id)
    case model: models.settings.Setting => TestServices.settingService.remove(creds, model.k)
    case model: models.sync.SyncProgressRow => TestServices.syncProgressRowService.remove(creds, model.key)
    case model: models.auth.SystemUserRow => TestServices.systemUserRowService.remove(creds, model.id)
    case model => throw new IllegalStateException(s"Unable to remove unhandled model [$model]")
  }
}
