package models.template

import models.audit.Audit
import util.{DateUtils, NullUtils}

import scalatags.Text.all._

object AuditTemplate {
  def forMessage(msg: Audit) = div(
    div(cls := "right")(DateUtils.niceDateTime(DateUtils.now)),
    div(
      i(cls := s"fa ${models.template.Icons.audit}"),
      s" Audit [${msg.id}] saved successfully."
    ),
    table(cls := "bordered")(
      tbody(
        tr(th(style := "width: 20%")("Action"), td(msg.act)),
        tr(th(style := "width: 20%")("Application"), td(msg.app)),
        tr(th(style := "width: 20%")("Client"), td(msg.client)),
        tr(th(style := "width: 20%")("Server"), td(msg.server)),
        tr(th(style := "width: 20%")("User"), td(msg.userId.map(_.toString))),
        tr(th(style := "width: 20%")("Tags"), td(msg.tags.map(t => t._1 + ": " + t._2).mkString(", "))),
        tr(th(style := "width: 20%")("Message"), td(msg.msg)),
        tr(th(style := "width: 20%")("Records"), td(msg.records.map { r =>
          div(style := "border: 1px solid #aaa; margin: 10px 0; padding: 6px;")(
            strong(r.t + ": " + r.pk.mkString("/")),
            em(cls := "right")(r.id.toString),
            table(
              thead(tr(th("Field"), th("Old"), th("New"))),
              tbody(r.changes.map { field =>
                tr(td(field.k), td(check(field.o)), td(check(field.n)))
              })
            )
          )
        })),
        tr(th(style := "width: 20%")("Started"), td(msg.started.toString)),
        tr(th(style := "width: 20%")("Completed"), td(msg.completed.toString))
      )
    )
  )

  private[this] def check(s: Option[String]) = s.getOrElse(NullUtils.str)
}
