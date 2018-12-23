/* Generated File */
package models.table

import services.database.slick.SlickQueryService.imports._

object AllTables {
  val schema = Seq(
    models.table.audit.AuditRecordRowTable.query.schema,
    models.table.audit.AuditRowTable.query.schema,
    models.table.ddl.FlywaySchemaHistoryRowTable.query.schema,
    models.table.note.NoteRowTable.query.schema,
    models.table.settings.SettingTable.query.schema,
    models.table.sync.SyncProgressRowTable.query.schema,
    models.table.task.ScheduledTaskRunRowTable.query.schema,
    models.table.user.SystemUserTable.query.schema
  )
}
