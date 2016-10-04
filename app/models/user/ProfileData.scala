package models.user

import models.template.Theme

case class ProfileData(
  username: String,
  language: Language,
  theme: Theme
)
