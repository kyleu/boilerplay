package utils

object NullUtils {
  val inst = None.orNull

  def isNull(v: Any) = v == inst
  def notNull(v: Any) = v != inst
}
