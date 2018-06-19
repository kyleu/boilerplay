package graphql

import sangria.schema._
import sangria.validation.{FloatCoercionViolation, ValueCoercionViolation}
import util.NullUtils

protected[graphql] trait CommonSchemaValueTypes {
  case object ByteCoercionViolation extends ValueCoercionViolation("Byte value expected in the range of an 8-bit number.")
  case object ShortCoercionViolation extends ValueCoercionViolation("Short value expected in the range of a 16-bit number.")

  implicit val shortType: ScalarType[Short] = ScalarType[Short](
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

  implicit val byteType: ScalarType[Byte] = ScalarType[Byte](
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

  implicit val floatType: ScalarType[Float] = ScalarType[Float](
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
