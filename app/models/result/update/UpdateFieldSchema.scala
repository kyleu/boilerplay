package models.result.update

import sangria.ast
import sangria.macros.derive.InputObjectTypeName
import sangria.macros.derive._
import sangria.schema._
import sangria.marshalling.circe._
import sangria.validation.ValueCoercionViolation

object UpdateFieldSchema {
  case object VarCoercionViolation extends ValueCoercionViolation("String, bool or int value expected.")

  val varType = ScalarType[String](
    "Var",
    description = Some(
      "The `Var` scalar type appears in a JSON response as a String; however, it is not intended to be human-readable. " +
        "When expected as an input type, any string (such as `\"4\"`) or integer (such as `4`) input value will be accepted as an ID."
    ),
    coerceOutput = valueOutput,
    coerceUserInput = {
      case s: String ⇒ Right(s)
      case i: Boolean ⇒ Right(i.toString)
      case i: Int ⇒ Right(i.toString)
      case i: BigInt ⇒ Right(i.toString)
      case i: BigDecimal ⇒ Right(i.toString)
      case i: Long ⇒ Right(i.toString)
      case i: Float ⇒ Right(i.toString)
      case _ ⇒ Left(VarCoercionViolation)
    },
    coerceInput = {
      case ast.StringValue(id, _, _) ⇒ Right(id)
      case ast.BooleanValue(id, _, _) ⇒ Right(id.toString)
      case ast.IntValue(id, _, _) ⇒ Right(id.toString)
      case ast.BigIntValue(id, _, _) ⇒ Right(id.toString)
      case ast.BigDecimalValue(id, _, _) ⇒ Right(id.toString)
      case ast.FloatValue(id, _, _) ⇒ Right(id.toString)
      case _ ⇒ Left(VarCoercionViolation)
    }
  )

  val updateFieldType = deriveInputObjectType[UpdateField](
    InputObjectTypeName("OrderByInput")
  )

  //val updateFieldsArg = Argument("fields", ListInputType(updateFieldType))
}
