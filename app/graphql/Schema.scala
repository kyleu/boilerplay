package graphql

import com.kyleu.projectile.graphql.GraphQLContext
import models.graphql.sandbox
import models.graphql.sandbox.SandboxSchema
import sangria.execution.deferred.DeferredResolver
import sangria.schema._
import util.Config

import scala.concurrent.Future

object Schema {
  // Fetchers
  val modelFetchers = {
    Nil ++
      /* Start model fetchers */
      /* Projectile export section [boilerplay] */
      Seq(
        models.graphql.audit.AuditRecordRowSchema.auditRecordRowByAuditIdFetcher,
        models.graphql.audit.AuditRecordRowSchema.auditRecordRowByPrimaryKeyFetcher,
        models.graphql.audit.AuditRowSchema.auditRowByPrimaryKeyFetcher,
        models.graphql.ddl.FlywaySchemaHistoryRowSchema.flywaySchemaHistoryRowByPrimaryKeyFetcher,
        models.graphql.note.NoteRowSchema.noteRowByAuthorFetcher,
        models.graphql.note.NoteRowSchema.noteRowByPrimaryKeyFetcher,
        models.graphql.settings.SettingSchema.settingByPrimaryKeyFetcher,
        models.graphql.sync.SyncProgressRowSchema.syncProgressRowByPrimaryKeyFetcher,
        models.graphql.task.ScheduledTaskRunRowSchema.scheduledTaskRunRowByPrimaryKeyFetcher,
        models.graphql.user.SystemUserRowSchema.systemUserRowByPrimaryKeyFetcher
      ) ++
        /* End model fetchers */
        Nil
  }

  val resolver = DeferredResolver.fetchers(modelFetchers: _*)

  private[this] val customQueryFields = fields[GraphQLContext, Unit](
    Field(name = "status", fieldType = StringType, resolve = _ => Future.successful("OK")),
    Field(name = "version", fieldType = StringType, resolve = _ => Future.successful(Config.version))
  )

  // Query Types
  val baseQueryFields = customQueryFields ++ SandboxSchema.queryFields

  val modelQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start model query fields */
      /* Projectile export section [boilerplay] */
      models.graphql.audit.AuditRecordRowSchema.queryFields ++
      models.graphql.audit.AuditRowSchema.queryFields ++
      models.graphql.ddl.FlywaySchemaHistoryRowSchema.queryFields ++
      models.graphql.note.NoteRowSchema.queryFields ++
      models.graphql.settings.SettingSchema.queryFields ++
      models.graphql.sync.SyncProgressRowSchema.queryFields ++
      models.graphql.task.ScheduledTaskRunRowSchema.queryFields ++
      models.graphql.user.SystemUserRowSchema.queryFields ++
      /* End model query fields */
      Nil
  }

  val enumQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start enum query fields */
      /* Projectile export section [boilerplay] */
      models.graphql.settings.SettingKeyTypeSchema.queryFields ++
      /* End enum query fields */
      Nil
  }

  val serviceQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start service methods */
      /* End service methods */
      Nil
  }

  val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = (baseQueryFields ++ modelQueryFields ++ enumQueryFields ++ serviceQueryFields).sortBy(_.name)
  )

  // Mutation Types
  val baseMutationFields = sandbox.SandboxSchema.mutationFields

  val modelMutationFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start model mutation fields */
      /* Projectile export section [boilerplay] */
      models.graphql.audit.AuditRecordRowSchema.mutationFields ++
      models.graphql.audit.AuditRowSchema.mutationFields ++
      models.graphql.ddl.FlywaySchemaHistoryRowSchema.mutationFields ++
      models.graphql.note.NoteRowSchema.mutationFields ++
      models.graphql.settings.SettingSchema.mutationFields ++
      models.graphql.sync.SyncProgressRowSchema.mutationFields ++
      models.graphql.task.ScheduledTaskRunRowSchema.mutationFields ++
      models.graphql.user.SystemUserRowSchema.mutationFields ++
      /* End model mutation fields */
      Nil
  }

  val mutationType = ObjectType(
    name = "Mutation",
    description = "The main mutation interface.",
    fields = (baseMutationFields ++ modelMutationFields).sortBy(_.name)
  )

  // Schema
  val schema = sangria.schema.Schema(
    query = queryType,
    mutation = Some(mutationType),
    subscription = None,
    additionalTypes = Nil
  )
}
