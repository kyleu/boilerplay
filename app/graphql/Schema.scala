package graphql

import sangria.execution.deferred.DeferredResolver
import sangria.schema._
import util.Config

import scala.concurrent.Future

object Schema {
  // Fetchers
  val baseFetchers = Seq(models.user.SystemUserSchema.systemUserByRoleFetcher, models.audit.AuditSchema.auditByUserIdFetcher)

  val modelFetchers = {
    // Start model fetchers
    Seq(
      models.audit.AuditRecordSchema.auditRecordByAuditIdFetcher,
      models.audit.AuditRecordSchema.auditRecordByPrimaryKeyFetcher,
      models.audit.AuditSchema.auditByPrimaryKeyFetcher,
      models.note.NoteSchema.noteByAuthorFetcher,
      models.note.NoteSchema.noteByPrimaryKeyFetcher,
      models.sync.SyncProgressSchema.syncProgressByPrimaryKeyFetcher,
      models.task.ScheduledTaskRunSchema.scheduledTaskRunByPrimaryKeyFetcher,
      models.user.SystemUserSchema.systemUserByPrimaryKeyFetcher
    )
    // End model fetchers
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

  val modelQueryFields = {
    // Start model query fields
    models.audit.AuditRecordSchema.queryFields ++
      models.audit.AuditSchema.queryFields ++
      models.note.NoteSchema.queryFields ++
      models.sync.SyncProgressSchema.queryFields ++
      models.task.ScheduledTaskRunSchema.queryFields ++
      models.user.SystemUserSchema.queryFields
    // End model query fields
  }

  val enumQueryFields = {
    // Start enum query fields
    models.settings.SettingKeySchema.queryFields
    // End enum query fields
  }

  val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = (baseQueryFields ++ modelQueryFields ++ enumQueryFields).sortBy(_.name)
  )

  // Mutation Types
  val baseMutationFields = models.sandbox.SandboxSchema.mutationFields

  val modelMutationFields = {
    // Start model mutation fields
    models.audit.AuditRecordSchema.mutationFields ++
      models.audit.AuditSchema.mutationFields ++
      models.note.NoteSchema.mutationFields ++
      models.sync.SyncProgressSchema.mutationFields ++
      models.task.ScheduledTaskRunSchema.mutationFields ++
      models.user.SystemUserSchema.mutationFields
    // End model mutation fields
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
