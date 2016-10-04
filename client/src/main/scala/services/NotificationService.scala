package services

import scala.scalajs.js

object NotificationService {
  private[this] val materialize = js.Dynamic.global.Materialize
  private[this] var lastError: Option[(String, String)] = None

  def info(reason: String, content: String, duration: Int = 2500) = {
    materialize.toast(reason + ": " + content, duration)
  }

  def warn(reason: String, content: String, duration: Int = 2500) = {
    materialize.toast(reason + ": " + content, duration)
  }

  def error(reason: String, content: String, duration: Int = 2500) = {
    lastError = Some(reason -> content)
    materialize.toast(reason + ": " + content, duration)
  }

  def getLastError = lastError
}
