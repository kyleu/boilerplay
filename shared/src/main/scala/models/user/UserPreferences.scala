package models.user

import models.template.Theme

object UserPreferences {
  val empty = UserPreferences()
}

case class UserPreferences(
  language: Language = Language.English,
  theme: Theme = Theme.BlueGrey
)
