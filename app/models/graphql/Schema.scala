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
  val baseFetchers = Seq(
    UserSchema.userByPrimaryKeyFetcher, UserSchema.userByRoleFetcher,
    AuditSchema.auditByPrimaryKeyFetcher, AuditSchema.auditByUserIdFetcher,
    AuditRecordSchema.auditRecordByPrimaryKeyFetcher, AuditRecordSchema.auditRecordByAuditIdFetcher,
    NoteSchema.noteByPrimaryKeyFetcher, NoteSchema.noteByAuthorFetcher
  )

  val modelFetchers = {
    // Start model fetchers
    Nil
    // End model fetchers
  }

  val resolver = DeferredResolver.fetchers(baseFetchers ++ modelFetchers: _*)

  // Query Types
  val baseQueryFields = {
    UserSchema.queryFields ++ AuditSchema.queryFields ++ AuditRecordSchema.queryFields ++
      NoteSchema.queryFields ++ SettingsSchema.queryFields ++ SandboxSchema.queryFields
  }

  val modelQueryFields = {
    // Start model query fields
    Nil
    // End model query fields
  }

  val queryType = ObjectType(
    name = "Query",
    description = "The main query interface.",
    fields = (baseQueryFields ++ modelQueryFields).sortBy(_.name)
  )

  // Mutation Types
  val baseMutationFields = SandboxSchema.mutationFields ++ AuditSchema.mutationFields ++ AuditRecordSchema.mutationFields ++ NoteSchema.mutationFields

  val modelMutationFields = {
    // Start model mutation fields
    Nil
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
