/* Generated File */
package models.template

import com.kyleu.projectile.models.config.NavMenu

object ComponentMenu {
  val menu: Seq[NavMenu] = Seq(
    /* Start component menu items */
    /* Projectile export section [boilerplay] */
    NavMenu(key = "audit", title = "Audit", url = None, children = Seq(
      NavMenu(key = "audit", title = "Audits", url = Some(controllers.admin.audit.routes.AuditRowController.list().url), icon = Some(models.template.Icons.auditRow)),
      NavMenu(key = "audit_record", title = "Audit Records", url = Some(controllers.admin.audit.routes.AuditRecordRowController.list().url), icon = Some(models.template.Icons.auditRecordRow))
    ), flatSection = true),
    NavMenu(key = "auth", title = "Auth", url = None, children = Seq(
      NavMenu(key = "oauth2_info", title = "OAuth2 Infos", url = Some(controllers.admin.auth.routes.Oauth2InfoRowController.list().url), icon = Some(models.template.Icons.oauth2InfoRow)),
      NavMenu(key = "password_info", title = "Password Infos", url = Some(controllers.admin.auth.routes.PasswordInfoRowController.list().url), icon = Some(models.template.Icons.passwordInfoRow)),
      NavMenu(key = "system_user", title = "System Users", url = Some(controllers.admin.auth.routes.SystemUserRowController.list().url), icon = Some(models.template.Icons.systemUserRow))
    ), flatSection = true),
    NavMenu(key = "ddl", title = "Ddl", url = None, children = Seq(
      NavMenu(key = "flyway_schema_history", title = "Flyway Schema Histories", url = Some(controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.list().url), icon = Some(models.template.Icons.flywaySchemaHistoryRow))
    ), flatSection = true),
    NavMenu(key = "note", title = "Note", url = None, children = Seq(
      NavMenu(key = "note", title = "Notes", url = Some(controllers.admin.note.routes.NoteRowController.list().url), icon = Some(models.template.Icons.noteRow))
    ), flatSection = true),
    NavMenu(key = "settings", title = "Settings", url = None, children = Seq(
      NavMenu(key = "setting_values", title = "Settings", url = Some(controllers.admin.settings.routes.SettingController.list().url), icon = Some(models.template.Icons.setting))
    ), flatSection = true),
    NavMenu(key = "sync", title = "Sync", url = None, children = Seq(
      NavMenu(key = "sync_progress", title = "Sync Progresses", url = Some(controllers.admin.sync.routes.SyncProgressRowController.list().url), icon = Some(models.template.Icons.syncProgressRow))
    ), flatSection = true),
    NavMenu(key = "task", title = "Task", url = None, children = Seq(
      NavMenu(key = "scheduled_task_run", title = "Scheduled Task Runs", url = Some(controllers.admin.task.routes.ScheduledTaskRunRowController.list().url), icon = Some(models.template.Icons.scheduledTaskRunRow))
    ), flatSection = true)
  /* End component menu items */
  )
}
