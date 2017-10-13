package models.audit

import java.util.UUID

case class AuditComplete(id: UUID, msg: String, tags: Map[String, String] = Map.empty, inserted: Seq[AuditModelPk] = Seq.empty)
