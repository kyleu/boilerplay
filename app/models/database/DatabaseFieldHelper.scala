package models.database

trait DatabaseFieldHelper {
  protected[this] def boolCoerce(x: Any) = x match {
    case b: Byte => b == 1.toByte
    case b: Boolean => b
  }

  protected[this] def byteCoerce(x: Any) = x match {
    case i: Int => i.toByte
    case b: Byte => b
  }

  protected[this] def tagsCoerce(x: Any) = x match {
    case m: java.util.HashMap[_, _] => models.tag.Tag.fromJavaMap(m)
  }
}
