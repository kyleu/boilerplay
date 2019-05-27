/* Generated File */
package models.table.film

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import models.film.MpaaRatingType
import slick.jdbc.JdbcType

object MpaaRatingTypeColumnType {
  implicit val mpaaRatingTypeColumnType: JdbcType[MpaaRatingType] = MappedColumnType.base[MpaaRatingType, String](_.value, MpaaRatingType.withValue)
}
