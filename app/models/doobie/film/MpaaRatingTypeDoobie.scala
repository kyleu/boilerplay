/* Generated File */
package models.doobie.film

import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.film.MpaaRatingType

object MpaaRatingTypeDoobie {
  implicit val mpaaRatingTypeMeta: Meta[MpaaRatingType] = pgEnumStringOpt("MpaaRatingType", MpaaRatingType.withValueOpt, _.value)
}
