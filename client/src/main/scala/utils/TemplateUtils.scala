package utils

import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.jquery.{JQuery, JQueryEventObject, jQuery => $}

import scalatags.Text.all._
import scalatags.Text.tags2.time

object TemplateUtils {
  def map(jq: JQuery, f: (JQuery) => (String, String)) = jq.map { e: Element => f($(e)) }

  def clickHandler(jq: JQuery, f: JQuery => Unit) = jq.click { (e: JQueryEventObject) =>
    f($(e.currentTarget))
    false
  }

  def changeHandler(jq: JQuery, f: (JQuery) => Unit) = jq.change { (e: JQueryEventObject) =>
    f($(e.currentTarget))
    false
  }

  @SuppressWarnings(Array("VarClosure"))
  def keyUpHandler(jq: JQuery, f: (JQuery, Int) => Unit) = jq.keyup { (e: JQueryEventObject) =>
    f($(e.currentTarget), e.which)
    false
  }

  def relativeTime() = $("time.timeago").each { (e: dom.Element) =>
    val el = $(e)
    val moment = scalajs.js.Dynamic.global.moment(el.attr("datetime"))
    el.text(moment.fromNow().toString)
  }

  def cleanForId(s: String) = s.replaceAllLiterally("$", "")

  def toIsoString(l: Long) = new scalajs.js.Date(l.toDouble).toISOString

  def toTimeago(dt: String) = time(cls := "timeago", title := dt, attr("datetime") := dt)(dt)
}
