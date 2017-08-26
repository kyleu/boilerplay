package models.result.orderBy

import enumeratum._

object OrderBy {
  sealed abstract class Direction(val sql: String) extends EnumEntry

  object Direction extends Enum[Direction] with CirceEnum[Direction] {
    case object Ascending extends Direction("asc")
    case object Descending extends Direction("desc")

    def fromBoolAsc(b: Boolean) = if (b) { Ascending } else { Descending }
    override val values = findValues
  }

  def forVals(col: Option[String], asc: Boolean) = col.map(c => OrderBy(col = c, dir = OrderBy.Direction.fromBoolAsc(asc)))
}

case class OrderBy(
  col: String = "?",
  dir: OrderBy.Direction = OrderBy.Direction.Ascending
)
