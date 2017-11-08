package models.audit

import util.DateUtils

import scalatags.Text.all._

object AuditCompleteTemplate {
  def forMessage(msg: AuditComplete) = li(cls := "collection-item")(
    div(cls := "right")(DateUtils.niceDateTime(DateUtils.now)),
    div(
      i(cls := s"fa ${models.template.Icons.completed}"),
      s" Audit [${msg.id}] has completed."
    ),
    table(cls := "bordered")(
      tbody(
        tr(th(style := "width: 20%")("Message"), td(msg.msg)),
        tr(th(style := "width: 20%")("Tags"), td(models.tag.Tag.toString(msg.tags))),
        tr(th(style := "width: 20%")("Inserted"), td(msg.inserted.map(t => t.t + ": " + t.pk.mkString("/")).mkString(", ")))
      )
    )
  )
}
