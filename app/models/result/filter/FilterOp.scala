package models.result.filter

import enumeratum._

sealed abstract class FilterOp extends EnumEntry {
  def vals(v: Seq[String]) = v
}

case object Equal extends FilterOp
case object NotEqual extends FilterOp
case object Like extends FilterOp
case object GreaterThanOrEqual extends FilterOp
case object LessThanOrEqual extends FilterOp

object FilterOp extends Enum[FilterOp] with CirceEnum[FilterOp] {
  override val values = scala.collection.immutable.IndexedSeq[FilterOp](Equal, NotEqual, Like, GreaterThanOrEqual, LessThanOrEqual)
}
