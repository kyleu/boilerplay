package models.audit

import java.time.LocalDateTime
import java.util.UUID

import models.result.data.{DataField, DataFieldModel, DataSummary}
import models.tag.Tag
import util.JsonSerializers._

object Audit {
  implicit val jsonEncoder: Encoder[Audit] = deriveEncoder
  implicit val jsonDecoder: Decoder[Audit] = deriveDecoder
}

final case class Audit(
    id: UUID = UUID.randomUUID,
    act: String = "?",
    app: String = util.Config.projectId,
    client: String = "?",
    server: String = "?",
    userId: UUID = UUID.randomUUID,
    tags: List[Tag] = Nil,
    msg: String = "n/a",
    started: LocalDateTime = util.DateUtils.now,
    completed: LocalDateTime = util.DateUtils.now
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("id", Some(id.toString)),
    DataField("act", Some(act)),
    DataField("app", Some(app)),
    DataField("client", Some(client)),
    DataField("server", Some(server)),
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
