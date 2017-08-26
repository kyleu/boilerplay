package services

import LogHelper

object InitService {
  private[this] var initialized = false

  def initIfNeeded() = if(!initialized) {
    init(false)
  }

  private[this] def init(debug: Boolean) = {
    LogHelper.init(debug)
    initialized = true
  }
}
