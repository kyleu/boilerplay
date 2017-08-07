package models.ddl

import java.time.LocalDateTime

case class Ddl(
  id: Int,
  name: String,
  sql: String,
  applied: LocalDateTime
)
