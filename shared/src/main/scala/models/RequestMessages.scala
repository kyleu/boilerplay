package models

import java.time.LocalDateTime
import java.util.UUID

import models.audit.AuditModelPk

sealed trait RequestMessage

case class MalformedRequest(reason: String, content: String) extends RequestMessage

case object GetVersion extends RequestMessage

case class Ping(timestamp: LocalDateTime) extends RequestMessage

case class AuditStart(
  action: String,
  app: Option[String] = None,
  client: Option[String] = None,
  server: Option[String] = None,
  user: Option[Int] = None,
  company: Option[Int] = None,
  tags: Map[String, String] = Map.empty,
  models: Seq[AuditModelPk] = Seq.empty
) extends RequestMessage

case class AuditComplete(id: UUID, msg: String, tags: Map[String, String] = Map.empty, inserted: Seq[AuditModelPk] = Seq.empty) extends RequestMessage
