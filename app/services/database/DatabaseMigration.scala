package services.database

import models.database.{Row, SingleRowQuery}
import org.flywaydb.core.Flyway
import util.Logging
import util.tracing.TraceData

trait DatabaseMigration extends Logging { this: JdbcDatabase =>
  def migrate() = {
    val flywayExists = query(new SingleRowQuery[Boolean] {
      override def name = "DoesFlywayTableExist"
      override def sql = "select count(*) x from information_schema.tables where table_name = 'flyway_schema_history'"
      override def map(row: Row) = row.as[Long]("x") > 0
    })(new TraceData)

    val flyway = if (!flywayExists) {
      val f = Flyway.configure().dataSource(ApplicationDatabase.source).baselineVersion("0").load()
      log.info("Initializing Flyway database migrations")
      f.baseline()
      f
    } else {
      Flyway.configure().dataSource(ApplicationDatabase.source).load()
    }
    val numApplied = flyway.migrate()
    if (numApplied > 0) {
      log.info(s"Applied [$numApplied] new database migrations")
    }
  }
}
