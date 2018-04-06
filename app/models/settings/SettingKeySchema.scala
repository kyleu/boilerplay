/* Generated File */
package models.settings

import models.graphql.{CommonSchema, GraphQLContext, SchemaHelper}
import sangria.schema.{EnumType, Field, ListType, fields}

object SettingKeySchema extends SchemaHelper("settingKey") {
  implicit val settingKeyEnumType: EnumType[SettingKey] = CommonSchema.deriveStringEnumeratumType(
    name = "SettingKey",
    description = "An enumeration of SettingKey values.",
    values = SettingKey.values.map(t => t -> t.value).toList
  )

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "settingKey",
    fieldType = ListType(settingKeyEnumType),
    resolve = c => traceB(c.ctx, "list")(_ => SettingKey.values)
  ))
}
