package models.audit

import java.time.LocalDateTime
import java.util.UUID

object Audit {
  case class Record(
    id: UUID = UUID.randomUUID,
    auditId: UUID = UUID.randomUUID,
    t: String = "default",
    pk: Seq[String] = Seq.empty,
    changes: Seq[AuditField]
  )
}

case class Audit(
  id: UUID = UUID.randomUUID,
  act: String = "???",
  app: Option[String] = None,
  client: Option[String] = None,
  server: Option[String] = None,
  user: Option[UUID] = None,
  tags: Map[String, String] = Map.empty,
  msg: String = "n/a",
  records: Seq[Audit.Record] = Nil,
  started: LocalDateTime = util.DateUtils.now,
  completed: LocalDateTime = util.DateUtils.now
) {
  lazy val changeLog = {
    "TODO... " + msg
  }
}
