package models.settings

import models.graphql.{GraphQLContext, SchemaHelper}
import sangria.execution.deferred.HasId
import sangria.macros.derive._
import sangria.schema._
import models.settings.SettingKeySchema.settingKeyEnumType

object SettingsSchema extends SchemaHelper("settings") {
  val settingPrimaryKeyId = HasId[Setting, SettingKey](_.key)

  implicit lazy val settingType: ObjectType[GraphQLContext, Setting] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "setting",
    description = Some("The system setting values for this application."),
    fieldType = ListType(settingType),
    resolve = c => traceB(c.ctx, "search")(_ => c.ctx.app.coreServices.settings.getAll)
  ))
}
