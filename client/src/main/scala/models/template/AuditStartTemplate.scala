package models.template

import java.util.UUID

import models.audit.AuditStart
import util.DateUtils

import scalatags.Text.all._

object AuditStartTemplate {
  def forMessage(id: UUID, msg: AuditStart) = div(
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
        tr(th(style := "width: 20%")("Server"), td(msg.server)),
        tr(th(style := "width: 20%")("User"), td(msg.user)),
        tr(th(style := "width: 20%")("Company"), td(msg.company)),
        tr(th(style := "width: 20%")("Tags"), td(msg.tags.map(t => t._1 + ": " + t._2).mkString(", "))),
        tr(th(style := "width: 20%")("Models"), td(msg.models.map(t => t.t + ": " + t.pk.mkString("/")).mkString(", ")))
      )
    )
  )
}
