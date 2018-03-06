package models.database

import java.util.UUID

trait DatabaseFieldHelper {
  protected[this] def boolCoerce(x: Any) = x match {
    case b: Byte => b == 1.toByte
    case b: Boolean => b
  }

  protected[this] def byteCoerce(x: Any) = x match {
    case i: Int => i.toByte
    case b: Byte => b
  }

  protected[this] def intCoerce(x: Any) = x match {
    case i: Int => i
    case l: Long => l.toInt
  }

  protected[this] def longCoerce(x: Any) = x match {
    case i: Int => i.toLong
    case l: Long => l
  }

  protected[this] def bigDecimalCoerce(x: Any) = x match {
    case b: java.math.BigDecimal => new BigDecimal(b)
    case b: BigDecimal => b
  }

  protected[this] def uuidCoerce(x: Any) = x match {
    case u: UUID => u
    case s: String if s.length == 36 => UUID.fromString(s)
  }

  protected[this] def tagsCoerce(x: Any) = x match {
    case m: java.util.HashMap[_, _] => models.tag.Tag.fromJavaMap(m)
  }
}
