package models.user

import models.template.Theme

object UserPreferences {
  val empty = UserPreferences()
}

case class UserPreferences(
  theme: Theme = Theme.BlueGrey
)
