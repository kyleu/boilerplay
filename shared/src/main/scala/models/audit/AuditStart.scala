package models.audit

import java.util.UUID

case class AuditStart(
  action: String,
  app: Option[String] = None,
  client: Option[String] = None,
  server: Option[String] = None,
  user: Option[UUID] = None,
  tags: Map[String, String] = Map.empty,
  models: Seq[AuditModelPk] = Seq.empty
)


