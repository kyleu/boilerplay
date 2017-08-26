package util

object NullUtils {
  val char = '∅'

  val inst = None.orNull

  def isNull(v: Any) = v == inst
  def notNull(v: Any) = v != inst
}
