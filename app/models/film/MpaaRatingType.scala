/* Generated File */
package models.film

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class MpaaRatingType(override val value: String) extends StringEnumEntry {
  override def toString = value
}

object MpaaRatingType extends StringEnum[MpaaRatingType] with StringCirceEnum[MpaaRatingType] {
  case object G extends MpaaRatingType("G")
  case object Pg extends MpaaRatingType("PG")
  case object Pg13 extends MpaaRatingType("PG13")
  case object R extends MpaaRatingType("R")
  case object Nc17 extends MpaaRatingType("NC17")

  override val values = findValues
}
