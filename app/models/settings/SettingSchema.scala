package models.settings

import models.graphql.{CommonSchema, GraphQLContext}
import sangria.execution.deferred.HasId
import sangria.macros.derive._
import sangria.schema._
import services.settings.SettingsService

object SettingSchema {
  implicit val settingKeyEnum = CommonSchema.deriveEnumeratumType(
    name = "SettingKey",
    description = "The possible system settings for this application.",
    values = SettingKey.values.map(t => t -> t.description).toList
  )

  implicit val settingId = HasId[Setting, SettingKey](_.key)

  implicit lazy val settingType: ObjectType[GraphQLContext, Setting] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "setting",
    description = Some("The system setting values for this application."),
    fieldType = ListType(settingType),
    resolve = _ => SettingsService.getAll
  ))
}
