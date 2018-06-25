package graphql

import models.tag.Tag
import sangria.macros.derive.{deriveInputObjectType, deriveObjectType}
import sangria.schema._

object CommonSchema extends CommonSchemaValueTypes with CommonSchemaReferenceTypes {
  val idArg = Argument("id", OptionInputType(IntType))
  val keyArg = Argument("key", StringType)
  val queryArg = Argument("q", OptionInputType(StringType))
  val limitArg = Argument("limit", OptionInputType(IntType))
  val offsetArg = Argument("offset", OptionInputType(IntType))

  def deriveEnumeratumType[T <: enumeratum.EnumEntry](name: String, values: Seq[T]) = EnumType[T](
    name = name,
    description = None,
    values = values.map(t => EnumValue(name = t.toString, value = t)).toList
  )

  def deriveStringEnumeratumType[T <: enumeratum.values.StringEnumEntry](name: String, values: Seq[T]) = EnumType[T](
    name = name,
    description = None,
    values = values.map(t => EnumValue(name = t.toString, value = t)).toList
  )

  implicit lazy val tagType: ObjectType[GraphQLContext, Tag] = deriveObjectType()
  implicit lazy val tagInputType: InputObjectType[Tag] = deriveInputObjectType()
}
