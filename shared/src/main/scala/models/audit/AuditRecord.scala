package models.audit

import java.util.UUID

case class AuditRecord(
  id: UUID = UUID.randomUUID,
  auditId: UUID = UUID.randomUUID,
  t: String = "default",
  pk: Seq[String] = Seq.empty,
  changes: Seq[AuditField]
)