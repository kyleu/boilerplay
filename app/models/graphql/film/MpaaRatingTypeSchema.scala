/* Generated File */
package models.graphql.film

import com.kyleu.projectile.graphql.{CommonSchema, GraphQLSchemaHelper}
import models.film.MpaaRatingType
import sangria.schema.{EnumType, ListType, fields}
import scala.concurrent.{ExecutionContext, Future}

object MpaaRatingTypeSchema extends GraphQLSchemaHelper("mpaaRatingType")(ExecutionContext.global) {
  implicit val mpaaRatingTypeEnumType: EnumType[MpaaRatingType] = CommonSchema.deriveStringEnumeratumType(
    name = "MpaaRatingType",
    values = MpaaRatingType.values
  )

  val queryFields = fields(
    unitField(name = "mpaaRatingType", desc = None, t = ListType(mpaaRatingTypeEnumType), f = (_, _) => Future.successful(MpaaRatingType.values))
  )
}
