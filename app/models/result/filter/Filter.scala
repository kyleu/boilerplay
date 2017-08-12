package models.result.filter

import enumeratum._

object Filter {
  sealed abstract class Op extends EnumEntry {
    def vals(v: Seq[String]) = v
  }

  object Op extends Enum[Filter.Op] with CirceEnum[Filter.Op] {
    case object Equal extends Filter.Op
    case object NotEqual extends Filter.Op
    case object Contains extends Filter.Op {
      override def vals(v: Seq[String]): Seq[String] = v.map("%" + _.replaceAllLiterally("%", "%%") + "%")
    }
    case object StartsWith extends Filter.Op {
      override def vals(v: Seq[String]): Seq[String] = v.map(_.replaceAllLiterally("%", "%%") + "%")
    }
    case object EndsWith extends Filter.Op {
      override def vals(v: Seq[String]): Seq[String] = v.map("%" + _.replaceAllLiterally("%", "%%"))
    }
    case object GreaterThanOrEqual extends Filter.Op
    case object LessThanOrEqual extends Filter.Op

    override val values = findValues
  }
}

case class Filter(
  k: String = "?",
  o: Filter.Op = Filter.Op.Equal,
  v: Seq[String] = Nil
)
