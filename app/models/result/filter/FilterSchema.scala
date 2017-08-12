package models.result.filter

import models.graphql.{CommonSchema, GraphQLContext}
import sangria.macros.derive._
import sangria.schema._
import sangria.marshalling.circe._

import io.circe.generic.auto._

object FilterSchema {
  implicit val filterOpType = CommonSchema.deriveEnumeratumType[Filter.Op](
    name = "FilterOperation",
    description = "Various operation to apply in filters.",
    values = Filter.Op.values.map(t => t -> t.toString).toList
  )

  implicit val filterType = deriveObjectType[GraphQLContext, Filter](
    ObjectTypeDescription("Options for filtering the results.")
  )

  val filterInputType = deriveInputObjectType[Filter](
    InputObjectTypeName("FilterInput")
  )

  val reportFiltersArg = Argument("filters", OptionInputType(ListInputType(filterInputType)))
}
