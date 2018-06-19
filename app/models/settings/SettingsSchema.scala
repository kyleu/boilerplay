package models.settings

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import sangria.execution.deferred.HasId
import sangria.macros.derive._
import sangria.schema._
import models.settings.SettingKeySchema.settingKeyEnumType

import scala.concurrent.Future

object SettingsSchema extends GraphQLSchemaHelper("settings") {
  val settingPrimaryKeyId = HasId[Setting, SettingKey](_.key)

  implicit lazy val settingType: ObjectType[GraphQLContext, Setting] = deriveObjectType()

  val queryFields = fields(
    unitField(
      name = "setting",
      desc = None,
      t = ListType(settingType),
      f = (c, td) => Future.successful(c.ctx.app.coreServices.settings.getAll)
    )
  )
}
