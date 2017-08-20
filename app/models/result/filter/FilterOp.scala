package models.result.filter

import enumeratum._

sealed abstract class FilterOp extends EnumEntry {
  def vals(v: Seq[String]) = v
}

object FilterOp extends Enum[FilterOp] with CirceEnum[FilterOp] {
  case object Equal extends FilterOp
  case object NotEqual extends FilterOp
  case object Contains extends FilterOp {
    override def vals(v: Seq[String]): Seq[String] = v.map("%" + _.replaceAllLiterally("%", "%%") + "%")
  }
  case object StartsWith extends FilterOp {
    override def vals(v: Seq[String]): Seq[String] = v.map(_.replaceAllLiterally("%", "%%") + "%")
  }
  case object EndsWith extends FilterOp {
    override def vals(v: Seq[String]): Seq[String] = v.map("%" + _.replaceAllLiterally("%", "%%"))
  }
  case object GreaterThanOrEqual extends FilterOp
  case object LessThanOrEqual extends FilterOp

  override val values = findValues
}
