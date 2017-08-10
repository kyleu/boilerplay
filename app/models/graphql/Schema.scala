package models.graphql

import models.sandbox.SandboxSchema
import models.settings.SettingSchema
import models.user.UserSchema
import sangria.execution.deferred.{DeferredResolver, Fetcher}
import sangria.schema._

object Schema {
  val modelFetchers: Seq[Fetcher[GraphQLContext, _, _, _]] = {
    // Start model fetchers
    Nil
    // End model fetchers
  }

  val resolver: DeferredResolver[GraphQLContext] = DeferredResolver.fetchers(UserSchema.userByIdFetcher +: modelFetchers: _*)

  val modelQueryFields = {
    // Start model query fields
    Nil
    // End model query fields
  }

  val queryFields = {
    val fields = modelQueryFields ++ UserSchema.queryFields ++ SettingSchema.queryFields ++ SandboxSchema.queryFields
    fields.sortBy(_.name)
  }

  val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = queryFields
  )

  val modelMutationFields = {
    // Start model mutation fields
    Nil
    // End model mutation fields
  }

  val mutationFields = SandboxSchema.mutationFields ++ modelMutationFields

  val mutationType = ObjectType(
    name = "Mutation",
    description = "The main mutation interface.",
    fields = mutationFields
  )

  val schema = sangria.schema.Schema(
    query = queryType,
    mutation = Some(mutationType)
  )
}
