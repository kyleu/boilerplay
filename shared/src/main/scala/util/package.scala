package object util {
  def ise(msg: String): Nothing = throw new IllegalStateException(msg)
  def ise(msg: String, ex: Throwable): Nothing = throw new IllegalStateException(msg, ex)
}
