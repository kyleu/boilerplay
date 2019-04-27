/* Generated File */
package models.ddl

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime

object FlywaySchemaHistoryRow {
  implicit val jsonEncoder: Encoder[FlywaySchemaHistoryRow] = (r: FlywaySchemaHistoryRow) => io.circe.Json.obj(
    ("installedRank", r.installedRank.asJson),
    ("version", r.version.asJson),
    ("description", r.description.asJson),
    ("typ", r.typ.asJson),
    ("script", r.script.asJson),
    ("checksum", r.checksum.asJson),
    ("installedBy", r.installedBy.asJson),
    ("installedOn", r.installedOn.asJson),
    ("executionTime", r.executionTime.asJson),
    ("success", r.success.asJson)
  )

  implicit val jsonDecoder: Decoder[FlywaySchemaHistoryRow] = (c: io.circe.HCursor) => for {
    installedRank <- c.downField("installedRank").as[Long]
    version <- c.downField("version").as[Option[String]]
    description <- c.downField("description").as[String]
    typ <- c.downField("typ").as[String]
    script <- c.downField("script").as[String]
    checksum <- c.downField("checksum").as[Option[Long]]
    installedBy <- c.downField("installedBy").as[String]
    installedOn <- c.downField("installedOn").as[LocalDateTime]
    executionTime <- c.downField("executionTime").as[Long]
    success <- c.downField("success").as[Boolean]
  } yield FlywaySchemaHistoryRow(installedRank, version, description, typ, script, checksum, installedBy, installedOn, executionTime, success)

  def empty(
    installedRank: Long = 0L,
    version: Option[String] = None,
    description: String = "",
    typ: String = "",
    script: String = "",
    checksum: Option[Long] = None,
    installedBy: String = "",
    installedOn: LocalDateTime = DateUtils.now,
    executionTime: Long = 0L,
    success: Boolean = false
  ) = {
    FlywaySchemaHistoryRow(installedRank, version, description, typ, script, checksum, installedBy, installedOn, executionTime, success)
  }
}

final case class FlywaySchemaHistoryRow(
    installedRank: Long,
    version: Option[String],
    description: String,
    typ: String,
    script: String,
    checksum: Option[Long],
    installedBy: String,
    installedOn: LocalDateTime,
    executionTime: Long,
    success: Boolean
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("installedRank", Some(installedRank.toString)),
    DataField("version", version),
    DataField("description", Some(description)),
    DataField("typ", Some(typ)),
    DataField("script", Some(script)),
    DataField("checksum", checksum.map(_.toString)),
    DataField("installedBy", Some(installedBy)),
    DataField("installedOn", Some(installedOn.toString)),
    DataField("executionTime", Some(executionTime.toString)),
    DataField("success", Some(success.toString))
  )

  def toSummary = DataSummary(model = "flywaySchemaHistoryRow", pk = installedRank.toString, title = s"version: ${version.map(_.toString).getOrElse("∅")}, description: $description, typ: $typ, installedOn: $installedOn, success: $success")
}
