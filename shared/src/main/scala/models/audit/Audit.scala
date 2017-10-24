package models.audit

import java.time.LocalDateTime
import java.util.UUID

import io.circe.{Decoder, Encoder}
import models.result.data.{DataField, DataFieldModel, DataSummary}
import io.circe.generic.semiauto._
import io.circe.java8.time._

object Audit {
  implicit val jsonEncoder: Encoder[Audit] = deriveEncoder
  implicit val jsonDecoder: Decoder[Audit] = deriveDecoder

  def empty(act: String = "default", userId: Option[UUID] = None) = Audit(act = act, userId = userId)
}

case class Audit(
    id: UUID = UUID.randomUUID,
    act: String = "???",
    app: String = util.Config.projectId,
    client: Option[String] = None,
    server: Option[String] = None,
    userId: Option[UUID] = None,
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
    DataField("userId", userId.map(_.toString)),
    DataField("tags", Some(tags.toString)),
    DataField("msg", Some(msg)),
    DataField("started", Some(started.toString)),
    DataField("completed", Some(completed.toString))
  )

  lazy val changeLog = s"Audit [$id] ($act/$app): $msg"
  lazy val duration = (util.DateUtils.toMillis(completed) - util.DateUtils.toMillis(started)).toInt

  def toSummary = DataSummary(model = "audit", pk = Seq(id.toString), title = s"$act / $app ($id)")
}
