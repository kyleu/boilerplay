package models.audit

case class AuditStart(
  action: String,
  app: Option[String] = None,
  client: Option[String] = None,
  server: Option[String] = None,
  user: Option[Long] = None,
  company: Option[Long] = None,
  tags: Map[String, String] = Map.empty,
  models: Seq[AuditModelPk] = Seq.empty
)


