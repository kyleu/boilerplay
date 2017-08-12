package models.result.orderBy

import models.graphql.{CommonSchema, GraphQLContext}
import sangria.macros.derive._
import sangria.schema._
import sangria.marshalling.circe._

import io.circe.generic.auto._

object OrderBySchema {
  implicit val orderByDirectionType = CommonSchema.deriveEnumeratumType[OrderBy.Direction](
    name = "OrderDirection",
    description = "The direction used to sort the results.",
    values = OrderBy.Direction.values.map(t => t -> t.toString).toList
  )

  implicit val orderByType = deriveObjectType[GraphQLContext, OrderBy](
    ObjectTypeDescription("Options for sorting the results.")
  )

  implicit val orderByInputType = deriveInputObjectType[OrderBy](
    InputObjectTypeName("OrderByInput")
  )

  val orderBysArg = Argument("orderBy", OptionInputType(ListInputType(orderByInputType)))
}
