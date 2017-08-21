package models.result.data

import sangria.ast
import sangria.schema._
import sangria.marshalling.circe._
import sangria.validation.ValueCoercionViolation

import io.circe.generic.auto._

object DataFieldSchema {
  case object VarCoercionViolation extends ValueCoercionViolation("String, bool or int value expected.")

  val varType = ScalarType[String](
    "Var",
    description = Some(
      "The `Var` scalar type appears in a JSON response as a String; however, it is not intended to be human-readable. " +
        "When expected as an input type, any string (such as `\"4\"`) or integer (such as `4`) input value will be accepted as an ID."
    ),
    coerceOutput = valueOutput,
    coerceUserInput = {
      case s: String => Right(s)
      case i: Boolean => Right(i.toString)
      case i: Int => Right(i.toString)
      case i: BigInt => Right(i.toString)
      case i: BigDecimal => Right(i.toString)
      case i: Long => Right(i.toString)
      case i: Float => Right(i.toString)
      case _ => Left(VarCoercionViolation)
    },
    coerceInput = {
      case ast.StringValue(id, _, _) => Right(id)
      case ast.BooleanValue(id, _, _) => Right(id.toString)
      case ast.IntValue(id, _, _) => Right(id.toString)
      case ast.BigIntValue(id, _, _) => Right(id.toString)
      case ast.BigDecimalValue(id, _, _) => Right(id.toString)
      case ast.FloatValue(id, _, _) => Right(id.toString)
      case _ => Left(VarCoercionViolation)
    }
  )

  val dataFieldInputType = InputObjectType[DataField](name = "DataFieldInput", fields = List(
    InputField("k", StringType),
    InputField("v", OptionInputType(varType))
  ))

  val dataFieldsArg = Argument("fields", ListInputType(dataFieldInputType))
}
