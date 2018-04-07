package services.database

import javax.sql.DataSource

import models.table.PostgresProfileEx.api._
import slick.dbio.NoStream
import slick.sql.SqlAction
import util.tracing.{TraceData, TracingService}

object SlickQueryService {
  val imports = models.table.PostgresProfileEx.api
}

class SlickQueryService(key: String, dataSource: DataSource, maxConnections: Int, tracingService: TracingService) {
  private[this] val db = Database.forDataSource(
    ds = dataSource,
    maxConnections = Some(maxConnections)
  )

  def run[R](act: SqlAction[R, NoStream, Nothing])(implicit parentTd: TraceData) = tracingService.trace("run") { td =>
    td.tag("sql", act.statements.mkString("\n\n"))
    db.run(act)
  }

  def close() = db.close()
}
