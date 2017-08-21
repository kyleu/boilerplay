package models.settings

import models.graphql.{CommonSchema, GraphQLContext, SchemaHelper}
import sangria.execution.deferred.HasId
import sangria.macros.derive._
import sangria.schema._

object SettingsSchema extends SchemaHelper("settings") {
  implicit val settingKeyEnum = CommonSchema.deriveEnumeratumType(
    name = "SettingKeyEnum",
    description = "The possible system settings for this application.",
    values = SettingKey.values.map(t => t -> t.description).toList
  )

  val settingPrimaryKeyId = HasId[Setting, SettingKey](_.key)

  implicit lazy val settingType: ObjectType[GraphQLContext, Setting] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "setting",
    description = Some("The system setting values for this application."),
    fieldType = ListType(settingType),
    resolve = c => trace(c.ctx, "search")(td => c.ctx.services.settingsService.getAll)
  ))
}
