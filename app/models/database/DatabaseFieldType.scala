package models.database

import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.UUID

import enumeratum._
import util.JodaDateUtils

sealed abstract class DatabaseFieldType[T](val key: String, val isNumeric: Boolean = false) extends EnumEntry {
  def apply(row: Row, col: String): T = row.as[T](col)
  def opt(row: Row, col: String): Option[T] = row.asOpt[T](col)
}

object DatabaseFieldType extends Enum[DatabaseFieldType[_]] with JodaDateUtils {
  case object StringType extends DatabaseFieldType[String]("string")
  case object BigDecimalType extends DatabaseFieldType[BigDecimal]("decimal", isNumeric = true)
  case object BooleanType extends DatabaseFieldType[Boolean]("boolean")
  case object ByteType extends DatabaseFieldType[Byte]("byte")
  case object ShortType extends DatabaseFieldType[Short]("short", isNumeric = true)
  case object IntegerType extends DatabaseFieldType[Int]("int", isNumeric = true)
  case object LongType extends DatabaseFieldType[Long]("long", isNumeric = true)
  case object FloatType extends DatabaseFieldType[Float]("float", isNumeric = true)
  case object DoubleType extends DatabaseFieldType[Double]("double", isNumeric = true)
  case object ByteArrayType extends DatabaseFieldType[Array[Byte]]("bytearray")

  case object DateType extends DatabaseFieldType[LocalDate]("date") {
    override def apply(row: Row, col: String) = fromJoda(row.as[org.joda.time.LocalDate](col))
    override def opt(row: Row, col: String) = row.asOpt[org.joda.time.LocalDate](col).map(fromJoda)
  }
  case object TimeType extends DatabaseFieldType[LocalTime]("time") {
    override def apply(row: Row, col: String) = fromJoda(row.as[org.joda.time.LocalTime](col))
    override def opt(row: Row, col: String) = row.asOpt[org.joda.time.LocalTime](col).map(fromJoda)
  }
  case object TimestampType extends DatabaseFieldType[LocalDateTime]("timestamp") {
    override def apply(row: Row, col: String) = fromJoda(row.as[org.joda.time.LocalDateTime](col))
    override def opt(row: Row, col: String) = row.asOpt[org.joda.time.LocalDateTime](col).map(fromJoda)
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
