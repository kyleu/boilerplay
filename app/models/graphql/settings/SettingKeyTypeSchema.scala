/* Generated File */
package models.graphql.settings

import com.kyleu.projectile.graphql.{CommonSchema, GraphQLSchemaHelper}
import models.settings.SettingKeyType
import sangria.schema.{EnumType, ListType, fields}
import scala.concurrent.Future

object SettingKeyTypeSchema extends GraphQLSchemaHelper("settingKeyType") {
  implicit val settingKeyTypeEnumType: EnumType[SettingKeyType] = CommonSchema.deriveStringEnumeratumType(
    name = "SettingKeyType",
    values = SettingKeyType.values
  )

  val queryFields = fields(
    unitField(name = "settingKeyType", desc = None, t = ListType(settingKeyTypeEnumType), f = (_, _) => Future.successful(SettingKeyType.values))
  )
}
