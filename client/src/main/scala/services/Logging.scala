package services

import org.scalajs.dom
import org.scalajs.dom.raw.Event

import scala.scalajs.js.Dynamic.global

object Logging {
  private[this] val showDebug = false
  private[this] var initialized = false

  def init(debug: Boolean) = {
    if (initialized) {
      throw new IllegalStateException("Logging initialized twice!")
    }
    installErrorHandler()
    initialized = true
  }

  def logJs(o: scalajs.js.Any) = {
    global.window.debug = o
    global.console.log(o)
  }

  def debug(msg: String): Unit = if (showDebug) {
    global.console.log(msg)
  }
  def info(msg: String): Unit = {
    global.console.info(msg)
  }
  def warn(msg: String): Unit = {
    global.console.warn(msg)
  }
  def error(msg: String): Unit = {
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
