package models.graphql

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchema}
import models.graphql.sandbox.SandboxSchema
import sangria.execution.deferred.DeferredResolver
import sangria.schema._
import util.Version

import scala.concurrent.Future

object Schema extends GraphQLSchema {
  // Fetchers
  private[this] val modelFetchers = {
    Nil ++
      /* Start model fetchers */
      /* Projectile export section [boilerplay] */
      Seq(
        models.graphql.sync.SyncProgressRowSchema.syncProgressRowByPrimaryKeyFetcher,
        models.graphql.task.ScheduledTaskRunRowSchema.scheduledTaskRunRowByPrimaryKeyFetcher
      ) ++
        /* End model fetchers */
        Nil
  }

  override val resolver = DeferredResolver.fetchers(modelFetchers: _*)

  private[this] val customQueryFields = fields[GraphQLContext, Unit](
    Field(name = "status", fieldType = StringType, resolve = _ => Future.successful("OK")),
    Field(name = "version", fieldType = StringType, resolve = _ => Future.successful(Version.version))
  )

  // Query Types
  private[this] val baseQueryFields = customQueryFields ++ SandboxSchema.queryFields

  private[this] val modelQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start model query fields */
      /* Projectile export section [boilerplay] */
      models.graphql.sync.SyncProgressRowSchema.queryFields ++
      models.graphql.task.ScheduledTaskRunRowSchema.queryFields ++
      /* End model query fields */
      Nil
  }

  private[this] val enumQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start enum query fields */
      /* End enum query fields */
      Nil
  }

  private[this] val serviceQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start service methods */
      /* End service methods */
      Nil
  }

  override val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = (baseQueryFields ++ modelQueryFields ++ enumQueryFields ++ serviceQueryFields).sortBy(_.name)
  )

  // Mutation Types
  private[this] val baseMutationFields = sandbox.SandboxSchema.mutationFields

  private[this] val modelMutationFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start model mutation fields */
      /* Projectile export section [boilerplay] */
      models.graphql.sync.SyncProgressRowSchema.mutationFields ++
      models.graphql.task.ScheduledTaskRunRowSchema.mutationFields ++
      /* End model mutation fields */
      Nil
  }

  override val mutationType = ObjectType(
    name = "Mutation",
    description = "The main mutation interface.",
    fields = (baseMutationFields ++ modelMutationFields).sortBy(_.name)
  )
}
