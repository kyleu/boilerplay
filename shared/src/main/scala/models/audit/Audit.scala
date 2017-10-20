package models.audit

import java.time.LocalDateTime
import java.util.UUID

case class Audit(
  id: UUID = UUID.randomUUID,
  act: String = "???",
  app: Option[String] = Some(util.Config.projectId),
  client: Option[String] = None,
  server: Option[String] = None,
  user: Option[UUID] = None,
  tags: Map[String, String] = Map.empty,
  msg: String = "n/a",
  records: Seq[AuditRecord] = Nil,
  started: LocalDateTime = util.DateUtils.now,
  completed: LocalDateTime = util.DateUtils.now
) {
  lazy val changeLog = {
    "TODO... " + msg
  }
}
