package models.graphql

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchema}
import models.graphql.sandbox.SandboxSchema
import sangria.execution.deferred.DeferredResolver
import sangria.schema._
import util.Config

import scala.concurrent.Future

object Schema extends GraphQLSchema {
  // Fetchers
  private[this] val modelFetchers = {
    Nil ++
      /* Start model fetchers */
      /* Projectile export section [boilerplay] */
      Seq(
        models.graphql.audit.AuditRecordRowSchema.auditRecordRowByAuditIdFetcher,
        models.graphql.audit.AuditRecordRowSchema.auditRecordRowByPrimaryKeyFetcher,
        models.graphql.audit.AuditRowSchema.auditRowByPrimaryKeyFetcher,
        models.graphql.auth.Oauth2InfoRowSchema.oauth2InfoRowByPrimaryKeyFetcher,
        models.graphql.auth.PasswordInfoRowSchema.passwordInfoRowByPrimaryKeyFetcher,
        models.graphql.auth.SystemUserRowSchema.systemUserRowByPrimaryKeyFetcher,
        models.graphql.ddl.FlywaySchemaHistoryRowSchema.flywaySchemaHistoryRowByPrimaryKeyFetcher,
        models.graphql.note.NoteRowSchema.noteRowByAuthorFetcher,
        models.graphql.note.NoteRowSchema.noteRowByPrimaryKeyFetcher,
        models.graphql.settings.SettingSchema.settingByPrimaryKeyFetcher,
        models.graphql.sync.SyncProgressRowSchema.syncProgressRowByPrimaryKeyFetcher,
        models.graphql.task.ScheduledTaskRunRowSchema.scheduledTaskRunRowByPrimaryKeyFetcher
      ) ++
        /* End model fetchers */
        Nil
  }

  override val resolver = DeferredResolver.fetchers(modelFetchers: _*)

  private[this] val customQueryFields = fields[GraphQLContext, Unit](
    Field(name = "status", fieldType = StringType, resolve = _ => Future.successful("OK")),
    Field(name = "version", fieldType = StringType, resolve = _ => Future.successful(Config.version))
  )

  // Query Types
  private[this] val baseQueryFields = customQueryFields ++ SandboxSchema.queryFields

  private[this] val modelQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start model query fields */
      /* Projectile export section [boilerplay] */
      models.graphql.audit.AuditRecordRowSchema.queryFields ++
      models.graphql.audit.AuditRowSchema.queryFields ++
      models.graphql.auth.Oauth2InfoRowSchema.queryFields ++
      models.graphql.auth.PasswordInfoRowSchema.queryFields ++
      models.graphql.auth.SystemUserRowSchema.queryFields ++
      models.graphql.ddl.FlywaySchemaHistoryRowSchema.queryFields ++
      models.graphql.note.NoteRowSchema.queryFields ++
      models.graphql.settings.SettingSchema.queryFields ++
      models.graphql.sync.SyncProgressRowSchema.queryFields ++
      models.graphql.task.ScheduledTaskRunRowSchema.queryFields ++
      /* End model query fields */
      Nil
  }

  private[this] val enumQueryFields: Seq[Field[GraphQLContext, Unit]] = {
    Nil ++
      /* Start enum query fields */
      /* Projectile export section [boilerplay] */
      models.graphql.settings.SettingKeyTypeSchema.queryFields ++
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
      models.graphql.audit.AuditRecordRowSchema.mutationFields ++
      models.graphql.audit.AuditRowSchema.mutationFields ++
      models.graphql.auth.Oauth2InfoRowSchema.mutationFields ++
      models.graphql.auth.PasswordInfoRowSchema.mutationFields ++
      models.graphql.auth.SystemUserRowSchema.mutationFields ++
      models.graphql.ddl.FlywaySchemaHistoryRowSchema.mutationFields ++
      models.graphql.note.NoteRowSchema.mutationFields ++
      models.graphql.settings.SettingSchema.mutationFields ++
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
