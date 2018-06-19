package models.result.filter

import graphql.CommonSchema
import graphql.GraphQLContext
import sangria.macros.derive._
import sangria.schema._
import sangria.marshalling.circe._
import models.result.data.DataFieldSchema

object FilterSchema {
  implicit val filterOpType: EnumType[FilterOp] = CommonSchema.deriveEnumeratumType[FilterOp](
    name = "FilterOperation",
    values = FilterOp.values
  )

  implicit val filterType: ObjectType[GraphQLContext, Filter] = deriveObjectType[GraphQLContext, Filter]()

  val filterInputType = InputObjectType[Filter](name = "FilterInput", fields = List(
    InputField("k", StringType),
    InputField("o", filterOpType),
    InputField("v", ListInputType(DataFieldSchema.varType))
  ))

  val reportFiltersArg = Argument("filters", OptionInputType(ListInputType(filterInputType)))
}
