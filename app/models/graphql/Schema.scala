package models.graphql

import models.audit.{AuditRecordSchema, AuditSchema}
import models.note.NoteSchema
import models.sandbox.SandboxSchema
import models.settings.SettingsSchema
import models.user.UserSchema
import sangria.execution.deferred.DeferredResolver
import sangria.schema._

object Schema {
  // Fetchers
  val baseFetchers = Seq(UserSchema.userByRoleFetcher, AuditSchema.auditByUserIdFetcher)

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
  val baseQueryFields = SettingsSchema.queryFields ++ SandboxSchema.queryFields

  val modelQueryFields = {
    // Start model query fields

    models.audit.AuditRecordSchema.queryFields ++
      models.audit.AuditSchema.queryFields ++
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
  val baseMutationFields = SandboxSchema.mutationFields

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
