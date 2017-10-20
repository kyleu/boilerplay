package models.audit

import java.time.LocalDateTime
import java.util.UUID

import models.result.data.{DataField, DataFieldModel}

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
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("act", Some(act.toString)),
    DataField("app", Some(app.toString)),
    DataField("client", client.map(_.toString)),
    DataField("server", server.map(_.toString)),
    DataField("user", user.map(_.toString)),
    DataField("tags", Some(tags.toString)),
    DataField("started", Some(started.toString)),
    DataField("completed", Some(completed.toString))
  )

  lazy val changeLog = {
    "TODO... " + msg
  }
}
