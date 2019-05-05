/* Generated File */
package models.table

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._

object AllTables {
  val schema = Seq(
    models.table.sync.SyncProgressRowTable.query.schema,
    models.table.task.ScheduledTaskRunRowTable.query.schema
  )
}
