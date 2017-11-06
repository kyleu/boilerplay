package util

import org.scalajs.dom
import org.scalajs.dom.raw.Event
import org.scalajs.jquery.{JQuery, jQuery => $}
import scalatags.Text.all._

import scala.scalajs.js.Dynamic.global

object Logging {
  private[this] val showDebug = false
  private[this] var initialized = false
  private[this] var container: Option[JQuery] = None

  private[this] def logElement(level: String, msg: String) = container.foreach { parent =>
    val el = div(cls := "log-message")(div(cls := "log-timestamp")(util.DateUtils.niceTime(util.DateUtils.now.toLocalTime)), msg)
    parent.append(el.toString)
  }

  def init(debug: Boolean) = {
    if (initialized) {
      throw new IllegalStateException("Logging initialized twice!")
    }
    installErrorHandler()

    val logContainerEl = $("#log-container")
    if (logContainerEl.length == 1) {
      container = Some(logContainerEl)
    }

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

  private[this] def installErrorHandler() = if (!initialized) {
    initialized = true
    if (showDebug) {
      dom.window.onerror = (e: Event, source: String, lineno: Int, colno: Int) => {
        info(s"Script error [$e] encountered in [$source:$lineno:$colno]")
        error(e.toString)
      }
    }
  }
}
