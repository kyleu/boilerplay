package models.graphql

import sangria.execution.deferred.DeferredResolver
import sangria.schema._

import scala.concurrent.Future

object Schema {
  val resolver = DeferredResolver.fetchers()

  val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = fields[GraphQLContext, Unit](
      Field(name = "hello", fieldType = StringType, resolve = c => Future.successful("Hi there!"))
    )
  )

  // Schema
  val schema = sangria.schema.Schema(
    query = queryType,
    mutation = None,
    subscription = None,
    additionalTypes = Nil
  )
}
