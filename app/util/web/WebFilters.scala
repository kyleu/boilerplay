package util.web

import javax.inject.Inject

import play.api.http.HttpFilters
import play.filters.gzip.GzipFilter

class WebFilters @Inject() (tracing: TracingFilter, logging: LoggingFilter, gzip: GzipFilter) extends HttpFilters {
  override def filters = Seq(tracing, logging, gzip)
}
