package models.user

import java.time.LocalDateTime

case class PasswordInfo(
  provider: String,
  key: String,
  hasher: String,
  password: String,
  salt: Option[String],
  created: LocalDateTime
)
