package util

object NullUtils {
  val char = '∅'
  val str = char.toString

  val inst = None.orNull

  def isNull(v: Any) = v == inst
  def notNull(v: Any) = v != inst
}
