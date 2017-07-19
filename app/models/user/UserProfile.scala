package models.user

import java.util.UUID

import models.template.Theme
import java.time.LocalDateTime

case class UserProfile(
  id: UUID,
  username: String,
  email: String,
  role: Role,
  theme: Theme,
  created: LocalDateTime
)
