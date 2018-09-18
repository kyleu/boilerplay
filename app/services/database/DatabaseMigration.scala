package services.database

import models.database.{Row, SingleRowQuery}
import org.flywaydb.core.Flyway
import util.Logging
import util.tracing.TraceData

trait DatabaseMigration extends Logging { this: JdbcDatabase =>
  def migrate() = {
    val flyway = new Flyway()
    flyway.setDataSource(ApplicationDatabase.source)

    val flywayExists = query(new SingleRowQuery[Boolean] {
      override def name = "DoesFlywayTableExist"
      override def sql = "select count(*) x from information_schema.tables where table_name = 'flyway_schema_history'"
      override def map(row: Row) = row.as[Long]("x") > 0
    })(new TraceData)

    if (!flywayExists) {
      log.info("Initializing Flyway database migrations")
      flyway.setBaselineVersionAsString("0")
      flyway.baseline()
    }
    val numApplied = flyway.migrate()
    if (numApplied > 0) {
      log.info(s"Applied [$numApplied] new database migrations")
    }
  }
}
