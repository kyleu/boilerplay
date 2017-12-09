package models.result.filter

import models.graphql.{CommonSchema, GraphQLContext}
import sangria.macros.derive._
import sangria.schema._
import sangria.marshalling.circe._
import models.result.data.DataFieldSchema

object FilterSchema {
  implicit val filterOpType: EnumType[FilterOp] = CommonSchema.deriveEnumeratumType[FilterOp](
    name = "FilterOperation",
    description = "Various operation to apply in filters.",
    values = FilterOp.values.map(t => t -> t.toString).toList
  )

  implicit val filterType: ObjectType[GraphQLContext, Filter] = deriveObjectType[GraphQLContext, Filter](
    ObjectTypeDescription("Options for filtering the results.")
  )

  val filterInputType = InputObjectType[Filter](name = "FilterInput", fields = List(
    InputField("k", StringType),
    InputField("o", filterOpType),
    InputField("v", ListInputType(DataFieldSchema.varType))
  ))

  val reportFiltersArg = Argument("filters", OptionInputType(ListInputType(filterInputType)))
}
