/* Generated File */
package models.settings

import graphql.{CommonSchema, GraphQLContext, GraphQLSchemaHelper}
import sangria.schema.{EnumType, ListType, fields}
import scala.concurrent.Future

object SettingKeySchema extends GraphQLSchemaHelper("settingKey") {
  implicit val settingKeyEnumType: EnumType[SettingKey] = CommonSchema.deriveStringEnumeratumType(
    name = "SettingKey",
    values = SettingKey.values
  )

  val queryFields = fields(
    unitField(name = "settingKey", desc = None, t = ListType(settingKeyEnumType), f = (c, td) => Future.successful(SettingKey.values))
  )
}
