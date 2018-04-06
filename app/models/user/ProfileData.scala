package models.user

import models.template.Theme

final case class ProfileData(
    username: String,
    theme: Theme
)
