package com.kyleu.projectile.util

import java.util.TimeZone

import org.scalajs.dom
import org.scalajs.dom.raw.{Element, Event}
import scalatags.Text.all._

import scala.scalajs.js.Dynamic.global

object Logging {
  TimeZone.setDefault(TimeZone.getTimeZone("UTC"))

  private[this] val showDebug = false
  private[this] var initialized = false
  private[this] var container: Option[Element] = None

  private[this] def logElement(level: String, msg: String) = container.foreach { parent =>
    val el = div(cls := "log-message")(div(cls := "log-timestamp")(JavaScriptUtils.niceCurrentTime()), msg)
    val domEl = dom.document.createElement("div")
    domEl.innerHTML = el.toString()
    parent.appendChild(domEl)
    parent.scrollTop = parent.scrollHeight.toDouble
  }

  def init() = {
    if (initialized) {
      throw new IllegalStateException("Logging initialized twice!")
    }
    installErrorHandler()
    container = Option(dom.document.getElementById("log-container"))
    initialized = true
  }

  def logJs(o: scalajs.js.Any) = {
    global.window.debug = o
    global.console.log(o)
  }

  def debug(msg: String): Unit = if (showDebug) {
    logElement("debug", msg)
    global.console.log(msg)
  }

  def info(msg: String): Unit = {
    logElement("info", msg)
    global.console.info(msg)
  }

  def warn(msg: String): Unit = {
    logElement("warn", msg)
    global.console.warn(msg)
  }

  def error(msg: String): Unit = {
    logElement("error", msg)
    global.console.error(msg)
  }

  private[this] def installErrorHandler() = {
    if (showDebug) {
      dom.window.onerror = (e: Event, source: String, lineno: Int, colno: Int, _: Any) => {
        info(s"Script error [$e] encountered in [$source:$lineno:$colno]")
        error(e.toString)
      }
    }
  }
}
