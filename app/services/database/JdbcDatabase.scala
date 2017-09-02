package services.database

import models.database.{DatabaseConfig, RawQuery, Statement}
import util.tracing.{TraceData, TracingService}

import scala.concurrent.Future

abstract class JdbcDatabase(override val key: String, configPrefix: String) extends Database[String] {
  def open(cfg: play.api.Configuration, svc: TracingService) = {
    start(DatabaseConfig.fromConfig(cfg, configPrefix), svc)
  }

  override def transaction[A](f: (TraceData, String) => Future[A], conn: Option[String])(implicit traceData: TraceData) = ???

  override def execute(statement: Statement, conn: Option[String])(implicit traceData: TraceData) = ???

  override def query[A](query: RawQuery[A], conn: Option[String])(implicit traceData: TraceData) = ???

  override def close() = true
}
