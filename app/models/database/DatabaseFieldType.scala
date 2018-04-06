package models.database

import enumeratum.values.{StringEnum, StringEnumEntry}
import enumeratum.{CirceEnum, Enum, EnumEntry}
import org.postgresql.jdbc.PgArray
import org.postgresql.util.PGobject

sealed abstract class DatabaseFieldType[T](val key: String, val isNumeric: Boolean = false) extends EnumEntry {
  def apply(row: Row, col: String): T = row.as[T](col)
  def opt(row: Row, col: String): Option[T] = row.asOpt[T](col)
}

object DatabaseFieldType extends Enum[DatabaseFieldType[_]] with CirceEnum[DatabaseFieldType[_]] with DatabaseFieldHelper {
  case object StringType extends DatabaseFieldType[String]("string")
  case object EncryptedStringType extends DatabaseFieldType[String]("encrypted") {
    override def apply(row: Row, col: String) = util.EncryptionUtils.decrypt(row.as[String](col))
    override def opt(row: Row, col: String) = row.asOpt[String](col).map(v => util.EncryptionUtils.decrypt(v))
  }

  case object BooleanType extends DatabaseFieldType[Boolean]("boolean") {
    override def apply(row: Row, col: String) = boolCoerce(row.as[Any](col))
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map(boolCoerce)
  }
  case object ByteType extends DatabaseFieldType[Byte]("byte") {
    override def apply(row: Row, col: String) = byteCoerce(row.as[Any](col))
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map(byteCoerce)
  }
  case object ShortType extends DatabaseFieldType[Short]("short", isNumeric = true)
  case object IntegerType extends DatabaseFieldType[Int]("int", isNumeric = true)
  case object LongType extends DatabaseFieldType[Long]("long", isNumeric = true) {
    override def apply(row: Row, col: String) = longCoerce(row.as[Any](col))
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map(longCoerce)
  }
  case object FloatType extends DatabaseFieldType[Float]("float", isNumeric = true)
  case object DoubleType extends DatabaseFieldType[Double]("double", isNumeric = true)
  case object BigDecimalType extends DatabaseFieldType[BigDecimal]("decimal", isNumeric = true) {
    override def apply(row: Row, col: String) = bigDecimalCoerce(row.as[Any](col))
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map(bigDecimalCoerce)
  }

  case object DateType extends DatabaseFieldType[java.time.LocalDate]("date") {
    override def apply(row: Row, col: String) = row.as[java.sql.Date](col).toLocalDate
    override def opt(row: Row, col: String) = row.asOpt[java.sql.Date](col).map(_.toLocalDate)
  }
  case object TimeType extends DatabaseFieldType[java.time.LocalTime]("time") {
    override def apply(row: Row, col: String) = row.as[java.sql.Time](col).toLocalTime
    override def opt(row: Row, col: String) = row.asOpt[java.sql.Time](col).map(_.toLocalTime)
  }
  case object TimestampType extends DatabaseFieldType[java.time.LocalDateTime]("timestamp") {
    override def apply(row: Row, col: String) = row.as[java.sql.Timestamp](col).toLocalDateTime
    override def opt(row: Row, col: String) = row.asOpt[java.sql.Timestamp](col).map(_.toLocalDateTime)
  }

  case object RefType extends DatabaseFieldType[String]("ref")
  case object XmlType extends DatabaseFieldType[String]("xml")
  case object UuidType extends DatabaseFieldType[java.util.UUID]("uuid") {
    override def apply(row: Row, col: String) = uuidCoerce(row.as[Any](col))
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map(uuidCoerce)
  }

  final case class EnumType[T <: StringEnumEntry](t: StringEnum[T]) extends DatabaseFieldType[T]("enum") {
    override def apply(row: Row, col: String) = t.withValue(row.as[String](col))
    override def opt(row: Row, col: String) = row.asOpt[String](col).map(t.withValue)
  }

  case object ObjectType extends DatabaseFieldType[String]("object")
  case object StructType extends DatabaseFieldType[String]("struct")
  case object JsonType extends DatabaseFieldType[io.circe.Json]("json") {
    override def apply(row: Row, col: String) = util.JsonSerializers.parseJson(row.as[PGobject](col).getValue).right.get
    override def opt(row: Row, col: String) = row.asOpt[PGobject](col).map(x => util.JsonSerializers.parseJson(x.getValue).right.get)
  }

  case object CodeType extends DatabaseFieldType[String]("code")
  case object TagsType extends DatabaseFieldType[Seq[models.tag.Tag]]("tags") {
    override def apply(row: Row, col: String) = tagsCoerce(row.as[Any](col))
    override def opt(row: Row, col: String) = row.asOpt[Any](col).map(tagsCoerce)
  }

  case object ByteArrayType extends DatabaseFieldType[Array[Byte]]("byteArray") {
    override def apply(row: Row, col: String) = row.as[PgArray](col).getArray.asInstanceOf[Array[Byte]]
    override def opt(row: Row, col: String) = row.asOpt[PgArray](col).map(_.asInstanceOf[Array[Byte]])
  }
  case object IntArrayType extends DatabaseFieldType[Seq[Int]]("intArray") {
    override def apply(row: Row, col: String) = row.as[PgArray](col).getArray.asInstanceOf[Array[Any]].map(intCoerce)
    override def opt(row: Row, col: String) = row.asOpt[PgArray](col).map(_.getArray.asInstanceOf[Array[Any]].map(intCoerce))
  }
  case object LongArrayType extends DatabaseFieldType[Seq[Long]]("longArray") {
    override def apply(row: Row, col: String) = row.as[PgArray](col).getArray.asInstanceOf[Array[Long]]
    override def opt(row: Row, col: String) = row.asOpt[PgArray](col).map(_.asInstanceOf[Array[Long]])
  }
  case object StringArrayType extends DatabaseFieldType[Seq[String]]("stringArray") {
    override def apply(row: Row, col: String) = row.as[PgArray](col).getArray.asInstanceOf[Array[Any]].map(_.toString)
    override def opt(row: Row, col: String) = row.asOpt[PgArray](col).map(_.getArray.asInstanceOf[Array[Any]].map(_.toString))
  }
  case object UuidArrayType extends DatabaseFieldType[Seq[java.util.UUID]]("uuidArray") {
    override def apply(row: Row, col: String) = row.as[PgArray](col).getArray.asInstanceOf[Array[java.util.UUID]]
    override def opt(row: Row, col: String) = row.asOpt[PgArray](col).map(_.asInstanceOf[Array[java.util.UUID]])
  }

  case object UnknownType extends DatabaseFieldType[String]("unknown")

  override val values = findValues
}
