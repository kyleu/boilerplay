package models.user

import java.time.LocalDateTime
import java.util.UUID

case class User(
  id: UUID,
  username: String,
  prefs: String,
  email: String,
  role: String,
  created: LocalDateTime
) extends com.mohiva.play.silhouette.api.Identity
