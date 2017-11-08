package models.audit

import java.util.UUID

import util.DateUtils

import scalatags.Text.all._

object AuditStartTemplate {
  def forMessage(id: UUID, msg: AuditStart) = li(cls := "collection-item")(
    div(cls := "right")(DateUtils.niceDateTime(DateUtils.now)),
    div(
      i(cls := s"fa ${models.template.Icons.started}"),
      s" Audit [$id] has started."
    ),
    table(cls := "bordered")(
      tbody(
        tr(th(style := "width: 20%")("Action"), td(msg.action)),
        tr(th(style := "width: 20%")("Application"), td(msg.app)),
        tr(th(style := "width: 20%")("Client"), td(msg.client)),
        tr(th(style := "width: 20%")("Tags"), td(models.tag.Tag.toString(msg.tags))),
        tr(th(style := "width: 20%")("Models"), td(msg.models.map(t => t.t + ": " + t.pk.mkString("/")).mkString(", ")))
      )
    )
  )
}
