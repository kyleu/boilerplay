/* Generated File */
package models.table

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._

object AllTables {
  val schema = Seq(
    models.table.audit.AuditRecordRowTable.query.schema,
    models.table.audit.AuditRowTable.query.schema,
    models.table.auth.Oauth2InfoRowTable.query.schema,
    models.table.auth.PasswordInfoRowTable.query.schema,
    models.table.auth.SystemUserRowTable.query.schema,
    models.table.ddl.FlywaySchemaHistoryRowTable.query.schema,
    models.table.note.NoteRowTable.query.schema,
    models.table.settings.SettingTable.query.schema,
    models.table.sync.SyncProgressRowTable.query.schema,
    models.table.task.ScheduledTaskRunRowTable.query.schema
  )
}
