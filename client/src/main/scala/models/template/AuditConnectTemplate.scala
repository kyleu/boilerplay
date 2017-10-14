package models.template

import util.DateUtils

import scalatags.Text.all._

object AuditConnectTemplate {
  def template() = div(
    div(cls := "right")(DateUtils.niceDateTime(DateUtils.now)),
    div(
      i(cls := s"fa ${models.template.Icons.connected}"),
      " Connected to server."
    )
  )
}
