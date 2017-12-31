package services.query

import play.api.Configuration
import slick.jdbc.PostgresProfile.api._
import util.tracing.TracingService

object QueryService {
  val imports = slick.jdbc.PostgresProfile.api
}

class QueryService(val key: String, configPrefix: String) {
  private[this] var database: Option[Database] = None
  def db = database.getOrElse(throw new IllegalStateException("QueryService has not been started."))

  private[this] var tracingService: Option[TracingService] = None
  private[this] def tracer = tracingService.getOrElse(throw new IllegalStateException("QueryService has not been started."))

  def open(cfg: Configuration, tracing: TracingService) = {
    database.foreach(_ => throw new IllegalStateException("Database already initialized."))
    database = Some(Database.forConfig(configPrefix))
    tracingService = Some(tracing)
  }

  def close() = {
    database.foreach(_.close())
    database = None
    tracingService = None
  }
}
