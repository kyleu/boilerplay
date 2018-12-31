/* Generated File */
package models.ddl

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.LocalDateTime

object FlywaySchemaHistoryRow {
  implicit val jsonEncoder: Encoder[FlywaySchemaHistoryRow] = deriveEncoder
  implicit val jsonDecoder: Decoder[FlywaySchemaHistoryRow] = deriveDecoder

  def empty(installedRank: Long = 0L, version: Option[String] = None, description: String = "", typ: String = "", script: String = "", checksum: Option[Long] = None, installedBy: String = "", installedOn: LocalDateTime = DateUtils.now, executionTime: Long = 0L, success: Boolean = false) = {
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

  def toSummary = DataSummary(model = "flywaySchemaHistoryRow", pk = Seq(installedRank.toString), title = s"$version / $description / $typ / $installedOn / $success ($installedRank)")
}
