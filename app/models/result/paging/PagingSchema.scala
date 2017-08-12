package models.result.paging

import models.graphql.GraphQLContext
import sangria.macros.derive._

object PagingSchema {
  implicit val pagingOptionsRangeType = deriveObjectType[GraphQLContext, PagingOptions.Range](
    ObjectTypeDescription("The row range of returned results.")
  )
  implicit val pagingOptionsType = deriveObjectType[GraphQLContext, PagingOptions](
    ObjectTypeDescription("Options for paging the results.")
  )
}
