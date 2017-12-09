package models.result.paging

import models.graphql.GraphQLContext
import sangria.macros.derive._
import sangria.schema.ObjectType

object PagingSchema {
  implicit val pagingOptionsRangeType: ObjectType[GraphQLContext, PagingOptions.Range] = deriveObjectType[GraphQLContext, PagingOptions.Range](
    ObjectTypeDescription("The row range of returned results.")
  )
  implicit val pagingOptionsType: ObjectType[GraphQLContext, PagingOptions] = deriveObjectType[GraphQLContext, PagingOptions](
    ObjectTypeDescription("Options for paging the results.")
  )
}
