package models.graphql

import java.util.UUID

import sangria.schema._
import sangria.validation.ValueCoercionViolation

import scala.util.{Failure, Success, Try}

object CommonSchema {
  val idArg = Argument("id", OptionInputType(IntType), description = "Returns model matching the provided id.")
  val keyArg = Argument("key", StringType, description = "Returns the model matching provided key.")
  val limitArg = Argument("limit", OptionInputType(IntType), description = "Caps the number of returned results.")

  case object UuidCoercionViolation extends ValueCoercionViolation("UUID value expected in format [00000000-0000-0000-0000-000000000000].")

  private[this] def parseUuid(s: String) = Try(UUID.fromString(s)) match {
    case Success(u) => Right(u)
    case Failure(_) => Left(UuidCoercionViolation)
  }

  implicit val uuidType = ScalarType[UUID](
    name = "UUID",
    description = Some("A string representing a UUID, in format [00000000-0000-0000-0000-000000000000]."),
    coerceOutput = (u, caps) => u.toString,
    coerceUserInput = {
      case s: String => parseUuid(s)
      case _ => Left(UuidCoercionViolation)
    },
    coerceInput = {
      case sangria.ast.StringValue(s, _, _) => parseUuid(s)
      case _ => Left(UuidCoercionViolation)
    }
  )

  def deriveEnumeratumType[T <: enumeratum.EnumEntry](name: String, description: String, values: Seq[(T, String)]) = EnumType(
    name = name,
    description = Some(description),
    values = values.map(t => EnumValue(name = t._1.toString, value = t._1, description = Some(t._2))).toList
  )

  def deriveStringEnumeratumType[T <: enumeratum.values.StringEnumEntry](name: String, description: String, values: Seq[(T, String)]) = EnumType(
    name = name,
    description = Some(description),
    values = values.map(t => EnumValue(name = t._1.toString, value = t._1, description = Some(t._2))).toList
  )
}
