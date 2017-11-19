package models.audit

import java.net.InetAddress
import java.time.LocalDateTime
import java.util.UUID

import io.circe.{Decoder, Encoder}
import models.result.data.{DataField, DataFieldModel, DataSummary}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.java8.time._
import models.tag.Tag

object Audit {
  implicit val jsonEncoder: Encoder[Audit] = deriveEncoder
  implicit val jsonDecoder: Decoder[Audit] = deriveDecoder

  lazy val serverName = InetAddress.getLocalHost.getHostName
}

case class Audit(
    id: UUID = UUID.randomUUID,
    act: String,
    app: String = util.Config.projectId,
    client: String,
    server: String = Audit.serverName,
    userId: UUID,
    tags: Seq[Tag] = Nil,
    msg: String = "n/a",
    records: Seq[AuditRecord] = Nil,
    started: LocalDateTime = util.DateUtils.now,
    completed: LocalDateTime = util.DateUtils.now
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("act", Some(act.toString)),
    DataField("app", Some(app.toString)),
    DataField("client", Some(client.toString)),
    DataField("server", Some(server.toString)),
    DataField("userId", Some(userId.toString)),
    DataField("tags", Some(tags.toString)),
    DataField("msg", Some(msg)),
    DataField("started", Some(started.toString)),
    DataField("completed", Some(completed.toString))
  )

  lazy val changeLog = s"Audit [$id] ($act/$app): $msg"
  lazy val duration = (util.DateUtils.toMillis(completed) - util.DateUtils.toMillis(started)).toInt

  def toSummary = DataSummary(model = "audit", pk = Seq(id.toString), title = s"$act / $app ($id)")
}
