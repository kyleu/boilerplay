package models.graphql

import sangria.schema._

object CommonSchema extends CommonSchemaTypes {
  val idArg = Argument("id", OptionInputType(IntType), description = "Returns model matching the provided id.")
  val keyArg = Argument("key", StringType, description = "Returns the model matching provided key.")
  val queryArg = Argument("q", OptionInputType(StringType), description = "Limits the returned results to those matching the provided value.")
  val limitArg = Argument("limit", OptionInputType(IntType), description = "Caps the number of returned results.")
  val offsetArg = Argument("offset", OptionInputType(IntType), description = "Offsets the returned results.")

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
