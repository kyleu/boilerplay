package models.audit

import util.DateUtils

import scalatags.Text.all._

object AuditConnectTemplate {
  def template() = li(cls := "collection-item")(
    div(cls := "right")(DateUtils.niceDateTime(DateUtils.now)),
    div(
      i(cls := s"fa ${models.template.Icons.connected}"),
      " Connected to server."
    )
  )
}
