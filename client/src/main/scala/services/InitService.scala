package services

object InitService {
  private[this] var initialized = false

  def initIfNeeded() = if (!initialized) {
    init(false)
  }

  private[this] def init(debug: Boolean) = {
    Logging.init(debug)
    initialized = true
  }
}
