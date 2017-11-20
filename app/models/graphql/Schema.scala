package models.graphql

import sangria.execution.deferred.DeferredResolver
import sangria.schema._

object Schema {
  // Fetchers
  val baseFetchers = Seq(models.user.UserSchema.userByRoleFetcher, models.audit.AuditSchema.auditByUserIdFetcher)

  val modelFetchers = {
    // Start model fetchers

    Seq(
      models.audit.AuditRecordSchema.auditRecordByAuditIdFetcher,
      models.audit.AuditRecordSchema.auditRecordByPrimaryKeyFetcher,
      models.audit.AuditSchema.auditByPrimaryKeyFetcher,
      models.note.NoteSchema.noteByAuthorFetcher,
      models.note.NoteSchema.noteByPrimaryKeyFetcher,
      models.user.UserSchema.userByPrimaryKeyFetcher
    )

    // End model fetchers
  }

  val resolver = DeferredResolver.fetchers(baseFetchers ++ modelFetchers: _*)

  // Query Types
  val baseQueryFields = models.supervisor.SocketDecriptionSchema.queryFields ++
    models.settings.SettingsSchema.queryFields ++
    models.sandbox.SandboxSchema.queryFields

  val modelQueryFields = {
    // Start model query fields
    models.audit.AuditSchema.queryFields ++
      models.audit.AuditRecordSchema.queryFields ++
      models.note.NoteSchema.queryFields ++
      models.user.UserSchema.queryFields

    // End model query fields
  }

  val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = (baseQueryFields ++ modelQueryFields).sortBy(_.name)
  )

  // Mutation Types
  val baseMutationFields = models.sandbox.SandboxSchema.mutationFields

  val modelMutationFields = {
    // Start model mutation fields

    models.audit.AuditRecordSchema.mutationFields ++
      models.audit.AuditSchema.mutationFields ++
      models.note.NoteSchema.mutationFields ++
      models.user.UserSchema.mutationFields

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
