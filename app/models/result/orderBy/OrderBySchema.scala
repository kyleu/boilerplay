package models.result.orderBy

import models.graphql.{CommonSchema, GraphQLContext}
import sangria.macros.derive._
import sangria.schema._
import sangria.marshalling.circe._

object OrderBySchema {
  implicit val orderByDirectionType: EnumType[OrderBy.Direction] = CommonSchema.deriveEnumeratumType[OrderBy.Direction](
    name = "OrderDirection",
    values = OrderBy.Direction.values
  )

  implicit val orderByType: ObjectType[GraphQLContext, OrderBy] = deriveObjectType[GraphQLContext, OrderBy](
    ObjectTypeDescription("Options for sorting the results.")
  )

  implicit val orderByInputType: InputObjectType[OrderBy] = deriveInputObjectType[OrderBy](
    InputObjectTypeName("OrderByInput")
  )

  val orderBysArg = Argument("orderBy", OptionInputType(ListInputType(orderByInputType)))
}
