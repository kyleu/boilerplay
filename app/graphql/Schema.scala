package graphql

import sangria.execution.deferred.DeferredResolver
import sangria.schema._
import util.Config

import scala.concurrent.Future

object Schema {
  // Fetchers
  val baseFetchers = Seq(models.user.SystemUserSchema.systemUserByRoleFetcher, models.audit.AuditSchema.auditByUserIdFetcher)

  val modelFetchers = {
    /* Start model fetchers */
    /* Projectile export section [boilerplay] */
    Seq(
      models.audit.AuditRecordSchema.auditRecordByAuditIdFetcher,
      models.audit.AuditRecordSchema.auditRecordByPrimaryKeyFetcher,
      models.audit.AuditSchema.auditByPrimaryKeyFetcher,
      models.ddl.FlywaySchemaHistorySchema.flywaySchemaHistoryByPrimaryKeyFetcher,
      models.note.NoteSchema.noteByAuthorFetcher,
      models.note.NoteSchema.noteByPrimaryKeyFetcher,
      models.settings.SettingSchema.settingByPrimaryKeyFetcher,
      models.sync.SyncProgressSchema.syncProgressByPrimaryKeyFetcher,
      models.task.ScheduledTaskRunSchema.scheduledTaskRunByPrimaryKeyFetcher,
      models.user.SystemUserSchema.systemUserByPrimaryKeyFetcher
    )
    /* End model fetchers */
  }

  val resolver = DeferredResolver.fetchers(baseFetchers ++ modelFetchers: _*)

  private[this] val customQueryFields = fields[GraphQLContext, Unit](
    Field(name = "status", fieldType = StringType, resolve = _ => Future.successful("OK")),
    Field(name = "version", fieldType = StringType, resolve = _ => Future.successful(Config.version))
  )

  // Query Types
  val baseQueryFields = customQueryFields ++
    models.supervisor.SocketDecriptionSchema.queryFields ++
    models.settings.SettingsSchema.queryFields ++
    models.sandbox.SandboxSchema.queryFields

  val modelQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    /* Start model query fields */
    /* Projectile export section [boilerplay] */
    models.audit.AuditSchema.queryFields ++
      models.audit.AuditRecordSchema.queryFields ++
      models.ddl.FlywaySchemaHistorySchema.queryFields ++
      models.note.NoteSchema.queryFields ++
      models.settings.SettingSchema.queryFields ++
      models.sync.SyncProgressSchema.queryFields ++
      models.task.ScheduledTaskRunSchema.queryFields ++
      models.user.SystemUserSchema.queryFields
    /* End model query fields */
  }

  val enumQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    /* Start enum query fields */
    /* Projectile export section [boilerplay] */
    models.settings.SettingKeySchema.queryFields
    /* End enum query fields */
  }

  val serviceQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    /* Start service methods */
    /* Projectile export section [boilerplay] */
    Nil
    /* End service methods */
  }

  val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = (baseQueryFields ++ modelQueryFields ++ enumQueryFields ++ serviceQueryFields).sortBy(_.name)
  )

  // Mutation Types
  val baseMutationFields = models.sandbox.SandboxSchema.mutationFields

  val modelMutationFields: Seq[Field[GraphQLContext, Unit]] = {
    /* Start model mutation fields */
    /* Projectile export section [boilerplay] */
    models.audit.AuditSchema.mutationFields ++
      models.audit.AuditRecordSchema.mutationFields ++
      models.ddl.FlywaySchemaHistorySchema.mutationFields ++
      models.note.NoteSchema.mutationFields ++
      models.settings.SettingSchema.mutationFields ++
      models.sync.SyncProgressSchema.mutationFields ++
      models.task.ScheduledTaskRunSchema.mutationFields ++
      models.user.SystemUserSchema.mutationFields
    /* End model mutation fields */
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
