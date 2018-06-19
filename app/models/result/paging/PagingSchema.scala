package models.result.paging

import graphql.GraphQLContext
import sangria.macros.derive._
import sangria.schema.ObjectType

object PagingSchema {
  implicit val pagingOptionsRangeType: ObjectType[GraphQLContext, PagingOptions.Range] = deriveObjectType[GraphQLContext, PagingOptions.Range]()
  implicit val pagingOptionsType: ObjectType[GraphQLContext, PagingOptions] = deriveObjectType[GraphQLContext, PagingOptions]()
}
