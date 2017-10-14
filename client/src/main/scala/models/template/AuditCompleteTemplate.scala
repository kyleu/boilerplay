package models.template

import models.audit.AuditComplete
import util.DateUtils

import scalatags.Text.all._

object AuditCompleteTemplate {
  def forMessage(msg: AuditComplete) = div(
    div(cls := "right")(DateUtils.niceDateTime(DateUtils.now)),
    div(
      i(cls := s"fa ${models.template.Icons.completed}"),
      s" Audit [${msg.id}] has completed."
    ),
    table(cls := "bordered")(
      tbody(
        tr(th(style := "width: 20%")("Message"), td(msg.msg)),
        tr(th(style := "width: 20%")("Tags"), td(msg.tags.map(t => t._1 + ": " + t._2).mkString(", "))),
        tr(th(style := "width: 20%")("Inserted"), td(msg.inserted.map(t => t.t + ": " + t.pk.mkString("/")).mkString(", ")))
      )
    )
  )
}
