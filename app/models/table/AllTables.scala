/* Generated File */
package models.table

import services.database.slick.SlickQueryService.imports._

object AllTables {
  val schema = Seq(
    models.table.audit.AuditRecordTable.query.schema,
    models.table.audit.AuditTable.query.schema,
    models.table.ddl.FlywaySchemaHistoryTable.query.schema,
    models.table.note.NoteTable.query.schema,
    models.table.settings.SettingTable.query.schema,
    models.table.sync.SyncProgressTable.query.schema,
    models.table.task.ScheduledTaskRunTable.query.schema,
    models.table.user.SystemUserTable.query.schema
  )
}
