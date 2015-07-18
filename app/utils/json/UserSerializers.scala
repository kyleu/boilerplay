package utils.json

import models.user.{ Role, User, UserPreferences }
import org.joda.time.LocalDateTime
import play.api.libs.json._

object UserSerializers {
  implicit val localDateTimeWrites = new Writes[LocalDateTime] {
    override def writes(ldt: LocalDateTime) = JsString(ldt.toString)
  }

  implicit val roleWrites = Json.writes[Role]

  implicit val userPreferencesReads = Json.reads[UserPreferences]
  implicit val userPreferencesWrites = Json.writes[UserPreferences]

  implicit val userWrites = Json.writes[User]
}
