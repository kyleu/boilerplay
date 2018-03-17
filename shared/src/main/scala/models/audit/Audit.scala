package models.audit

import java.time.LocalDateTime
import java.util.UUID

import models.result.data.{DataField, DataFieldModel, DataSummary}
import models.tag.Tag
import util.JsonSerializers.Circe._

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

object Audit {
  implicit val jsonEncoder: Encoder[Audit] = deriveEncoder
  implicit val jsonDecoder: Decoder[Audit] = deriveDecoder
}

@JSExportTopLevel(util.Config.projectId + ".Audit")
case class Audit(
    @JSExport id: UUID = UUID.randomUUID,
    @JSExport act: String = "?",
    @JSExport app: String = util.Config.projectId,
    @JSExport client: String = "?",
    @JSExport server: String = "?",
    @JSExport userId: UUID = UUID.randomUUID,
    @JSExport tags: Seq[Tag] = Nil,
    @JSExport msg: String = "n/a",
    @JSExport records: Seq[AuditRecord] = Nil,
    @JSExport started: LocalDateTime = util.DateUtils.now,
    @JSExport completed: LocalDateTime = util.DateUtils.now
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
