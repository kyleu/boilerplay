/* Generated File */
package models.settings

import models.graphql.{CommonSchema, SchemaHelper}
import sangria.schema.{EnumType, ListType, fields}
import scala.concurrent.Future

object SettingKeySchema extends SchemaHelper("settingKey") {
  implicit val settingKeyEnumType: EnumType[SettingKey] = CommonSchema.deriveStringEnumeratumType(
    name = "SettingKey",
    description = "An enumeration of SettingKey values.",
    values = SettingKey.values.map(t => t -> t.value).toList
  )

  val queryFields = fields(
    unitField(name = "settingKey", desc = None, t = ListType(settingKeyEnumType), f = (c, td) => Future.successful(SettingKey.values))
  )
}
