package models.audit

import java.time.LocalDateTime
import java.util.UUID

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.models.tag.Tag
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._

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
    started: LocalDateTime = DateUtils.now,
    completed: LocalDateTime = DateUtils.now
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
  lazy val duration = (DateUtils.toMillis(completed) - DateUtils.toMillis(started)).toInt

  def toSummary = DataSummary(model = "audit", pk = id.toString, title = s"$act / $app ($id)")
}
