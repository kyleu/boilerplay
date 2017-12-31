package services.database

import javax.sql.DataSource

import slick.dbio.NoStream
import slick.jdbc.PostgresProfile.api._
import slick.sql.SqlAction
import util.tracing.{TraceData, TracingService}

object SlickQueryService {
  val imports = slick.jdbc.PostgresProfile.api
}

class SlickQueryService(key: String, dataSource: DataSource, maxConnections: Int, tracingService: TracingService) {
  private[this] val db = Database.forDataSource(dataSource, maxConnections = Some(maxConnections))

  def run[R](act: SqlAction[R, NoStream, Nothing])(implicit parentTd: TraceData) = tracingService.trace("run") { td =>
    td.span.tag("sql", act.statements.mkString("\n\n"))
    db.run(act)
  }

  def close() = db.close()
}
