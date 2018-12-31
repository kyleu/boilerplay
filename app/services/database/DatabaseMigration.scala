package services.database

import com.kyleu.projectile.models.database.{Row, SingleRowQuery}
import com.kyleu.projectile.services.database.{ApplicationDatabase, JdbcDatabase}
import org.flywaydb.core.Flyway
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData

trait DatabaseMigration extends Logging { this: JdbcDatabase =>
  def migrate() = {
    val flywayExists = query(new SingleRowQuery[Boolean] {
      override def name = "DoesFlywayTableExist"
      override def sql = "select count(*) x from information_schema.tables where table_name = 'flyway_schema_history'"
      override def map(row: Row) = row.as[Long]("x") > 0
    })(TraceData.noop)

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
