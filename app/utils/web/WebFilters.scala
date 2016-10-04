package utils.web

import javax.inject.Inject

import play.api.http.HttpFilters
import play.filters.gzip.GzipFilter

class WebFilters @Inject() (customLoggingFilter: LoggingFilter, gzipFilter: GzipFilter) extends HttpFilters {
  override def filters = Seq(customLoggingFilter, gzipFilter)
}
