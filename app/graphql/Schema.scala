package graphql

import sangria.execution.deferred.DeferredResolver
import sangria.schema._
import util.Config

import scala.concurrent.Future

object Schema {
  // Fetchers
  val baseFetchers = Seq(models.user.SystemUserSchema.systemUserByRoleFetcher)

  val modelFetchers = {
    /* Start model fetchers */
    /* Projectile export section [boilerplay] */
    Seq(
      models.audit.AuditRecordRowSchema.auditRecordRowByAuditIdFetcher,
      models.audit.AuditRecordRowSchema.auditRecordRowByPrimaryKeyFetcher,
      models.audit.AuditRowSchema.auditRowByPrimaryKeyFetcher,
      models.ddl.FlywaySchemaHistoryRowSchema.flywaySchemaHistoryRowByPrimaryKeyFetcher,
      models.note.NoteRowSchema.noteRowByAuthorFetcher,
      models.note.NoteRowSchema.noteRowByPrimaryKeyFetcher,
      models.settings.SettingSchema.settingByPrimaryKeyFetcher,
      models.sync.SyncProgressRowSchema.syncProgressRowByPrimaryKeyFetcher,
      models.task.ScheduledTaskRunRowSchema.scheduledTaskRunRowByPrimaryKeyFetcher,
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
    models.sandbox.SandboxSchema.queryFields

  val modelQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    /* Start model query fields */
    /* Projectile export section [boilerplay] */
    models.audit.AuditRecordRowSchema.queryFields ++
      models.audit.AuditRowSchema.queryFields ++
      models.ddl.FlywaySchemaHistoryRowSchema.queryFields ++
      models.note.NoteRowSchema.queryFields ++
      models.settings.SettingSchema.queryFields ++
      models.sync.SyncProgressRowSchema.queryFields ++
      models.task.ScheduledTaskRunRowSchema.queryFields ++
      models.user.SystemUserSchema.queryFields
    /* End model query fields */
  }

  val enumQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    /* Start enum query fields */
    /* Projectile export section [boilerplay] */
    models.settings.SettingKeyTypeSchema.queryFields
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
    models.audit.AuditRecordRowSchema.mutationFields ++
      models.audit.AuditRowSchema.mutationFields ++
      models.ddl.FlywaySchemaHistoryRowSchema.mutationFields ++
      models.note.NoteRowSchema.mutationFields ++
      models.settings.SettingSchema.mutationFields ++
      models.sync.SyncProgressRowSchema.mutationFields ++
      models.task.ScheduledTaskRunRowSchema.mutationFields ++
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
