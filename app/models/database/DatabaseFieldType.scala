package models.database

import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.UUID

import enumeratum._

sealed abstract class DatabaseFieldType[T](val key: String, val isNumeric: Boolean = false) extends EnumEntry {
  def apply(row: Row, col: String): T = row.as[T](col)
  def opt(row: Row, col: String): Option[T] = row.asOpt[T](col)
}

object DatabaseFieldType extends Enum[DatabaseFieldType[_]] {
  case object StringType extends DatabaseFieldType[String]("string")
  case object BigDecimalType extends DatabaseFieldType[BigDecimal]("decimal", isNumeric = true) {
    override def apply(row: Row, col: String) = row.as[Any](col) match {
      case b: java.math.BigDecimal => new BigDecimal(b)
      case b: BigDecimal => b
    }
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map {
      case b: java.math.BigDecimal => new BigDecimal(b)
      case b: BigDecimal => b
    }
  }

  case object BooleanType extends DatabaseFieldType[Boolean]("boolean") {
    override def apply(row: Row, col: String) = row.as[Any](col) match {
      case b: Byte => b == 1.toByte
      case b: Boolean => b
    }
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map {
      case b: Byte => b == 1.toByte
      case b: Boolean => b
    }
  }

  case object ByteType extends DatabaseFieldType[Byte]("byte") {
    override def apply(row: Row, col: String) = row.as[Any](col) match {
      case i: Int => i.toByte
      case b: Byte => b
    }
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map {
      case i: Int => i.toByte
      case b: Byte => b
    }
  }

  case object ShortType extends DatabaseFieldType[Short]("short", isNumeric = true)
  case object IntegerType extends DatabaseFieldType[Int]("int", isNumeric = true)
  case object LongType extends DatabaseFieldType[Long]("long", isNumeric = true)
  case object FloatType extends DatabaseFieldType[Float]("float", isNumeric = true)
  case object DoubleType extends DatabaseFieldType[Double]("double", isNumeric = true)
  case object ByteArrayType extends DatabaseFieldType[Array[Byte]]("bytearray")

  case object DateType extends DatabaseFieldType[LocalDate]("date") {
    override def apply(row: Row, col: String) = row.as[java.sql.Date](col).toLocalDate
    override def opt(row: Row, col: String) = row.asOpt[java.sql.Date](col).map(_.toLocalDate)
  }
  case object TimeType extends DatabaseFieldType[LocalTime]("time") {
    override def apply(row: Row, col: String) = row.as[java.sql.Time](col).toLocalTime
    override def opt(row: Row, col: String) = row.asOpt[java.sql.Time](col).map(_.toLocalTime)
  }
  case object TimestampType extends DatabaseFieldType[LocalDateTime]("timestamp") {
    override def apply(row: Row, col: String) = row.as[java.sql.Timestamp](col).toLocalDateTime
    override def opt(row: Row, col: String) = row.asOpt[java.sql.Timestamp](col).map(_.toLocalDateTime)
  }

  case object RefType extends DatabaseFieldType[String]("ref")
  case object XmlType extends DatabaseFieldType[String]("xml")

  case object UuidType extends DatabaseFieldType[UUID]("uuid")

  case object ObjectType extends DatabaseFieldType[String]("object")
  case object StructType extends DatabaseFieldType[String]("struct")
  case object ArrayType extends DatabaseFieldType[Array[Byte]]("array")

  case object UnknownType extends DatabaseFieldType[String]("unknown")

  override val values = findValues
}
