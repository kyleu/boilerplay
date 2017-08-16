package models.graphql

import java.util.UUID

import org.apache.commons.codec.binary.Base64
import sangria.schema._
import sangria.validation.{FloatCoercionViolation, ValueCoercionViolation}
import util.NullUtils

import scala.util.{Failure, Success, Try}

protected[graphql] trait CommonSchemaTypes {
  case object ByteCoercionViolation extends ValueCoercionViolation("Byte value expected in the range of an 8-bit number.")
  case object ShortCoercionViolation extends ValueCoercionViolation("Short value expected in the range of a 16-bit number.")
  case object UuidCoercionViolation extends ValueCoercionViolation("UUID value expected in format [00000000-0000-0000-0000-000000000000].")
  case object Base64CoercionViolation extends ValueCoercionViolation("Base64-encoded value expected.")

  private[this] def parseUuid(s: String) = Try(UUID.fromString(s)) match {
    case Success(u) => Right(u)
    case Failure(_) => Left(UuidCoercionViolation)
  }

  implicit val shortType = ScalarType[Short](
    name = "Short",
    description = Some("A 16 bit number."),
    coerceOutput = (u, _) => u.toInt,
    coerceUserInput = {
      case i: Int if i <= Short.MaxValue && i >= Short.MinValue => Right(i.toShort)
      case _ => Left(ShortCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.IntValue(i, _, _) => Right(i.toShort)
      case _ => Left(ShortCoercionViolation)
    }
  )

  implicit val uuidType = ScalarType[UUID](
    name = "UUID",
    description = Some("A string representing a UUID, in format [00000000-0000-0000-0000-000000000000]."),
    coerceOutput = (u, _) => u.toString,
    coerceUserInput = {
      case s: String => parseUuid(s)
      case _ => Left(UuidCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.StringValue(s, _, _) => parseUuid(s)
      case _ => Left(UuidCoercionViolation)
    }
  )

  implicit val byteType = ScalarType[Byte](
    name = "Byte",
    description = Some("A single byte, expressed as an integer."),
    coerceOutput = (u, _) => u.toInt,
    coerceUserInput = {
      case i: Int => Right(i.toByte)
      case _ => Left(ByteCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.IntValue(i, _, _) => Right(i.toByte)
      case _ => Left(ByteCoercionViolation)
    }
  )

  implicit val byteArrayType = ScalarType[Array[Byte]](
    name = "Base64",
    description = Some("A binary array of bytes, encoded with Base64."),
    coerceOutput = (u, _) => Base64.encodeBase64(u),
    coerceUserInput = {
      case s: String => Right(Base64.decodeBase64(s))
      case _ => Left(Base64CoercionViolation)
    },
    coerceInput = {
      case sangria.ast.StringValue(s, _, _) => Right(Base64.decodeBase64(s))
      case _ => Left(Base64CoercionViolation)
    }
  )

  implicit val floatType = ScalarType[Float](
    name = "FloatFixed",
    description = Some("The `FloatFixed` scalar type represents signed double-precision fractional values as specified by [IEEE 754]."),
    coerceOutput = (v, _) => { if (java.lang.Float.isNaN(v) || java.lang.Float.isInfinite(v)) { NullUtils.inst } else { v } },
    coerceUserInput = {
      case i: Int => Right(i.toFloat)
      case i: Long => Right(i.toFloat)
      case i: BigInt if i.isValidFloat => Right(i.floatValue)
      case d: Double => Right(d.toFloat)
      case d: BigDecimal if d.isDecimalFloat => Right(d.floatValue)
      case _ => Left(FloatCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.FloatValue(d, _, _) => Right(d.toFloat)
      case sangria.ast.BigDecimalValue(d, _, _) if d.isDecimalFloat => Right(d.floatValue)
      case sangria.ast.IntValue(i, _, _) => Right(i.toFloat)
      case sangria.ast.BigIntValue(i, _, _) if i.isValidFloat => Right(i.floatValue)
      case _ => Left(FloatCoercionViolation)
    }
  )
}
