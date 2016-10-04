package models.ddl

import models.database.Database
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import utils.Logging

import scala.concurrent.Future

object MasterDdl extends Logging {
  val tables = Seq(
    CreateUsersTable,
    CreatePasswordInfoTable,
    CreateSettingsTable
  )

  def update() = Future.sequence(tables.map { t =>
    Database.query(DdlQueries.DoesTableExist(t.tableName)).flatMap { exists =>
      if (exists) {
        Future.successful(0)
      } else {
        log.info(s"Creating missing table [${t.tableName}].")
        Database.execute(t)
      }
    }
  })

  def wipe() = {
    log.warn("Wiping database schema.")
    val tableNames = tables.reverseMap(_.tableName)
    tableNames.map { tableName =>
      Database.execute(DdlQueries.DropTable(tableName))
    }
  }
}
