package utils.play

import play.api.http.HttpFilters
import play.filters.headers.SecurityHeadersFilter

class PlaySecurityFilters @javax.inject.Inject() (securityHeadersFilter: SecurityHeadersFilter) extends HttpFilters {
  def filters = Seq(securityHeadersFilter)
}
