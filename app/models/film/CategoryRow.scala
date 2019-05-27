/* Generated File */
package models.film

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object CategoryRow {
  implicit val jsonEncoder: Encoder[CategoryRow] = (r: CategoryRow) => io.circe.Json.obj(
    ("categoryId", r.categoryId.asJson),
    ("name", r.name.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[CategoryRow] = (c: io.circe.HCursor) => for {
    categoryId <- c.downField("categoryId").as[Int]
    name <- c.downField("name").as[String]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield CategoryRow(categoryId, name, lastUpdate)

  def empty(
    categoryId: Int = 0,
    name: String = "",
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    CategoryRow(categoryId, name, lastUpdate)
  }
}

final case class CategoryRow(
    categoryId: Int,
    name: String,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("categoryId", Some(categoryId.toString)),
    DataField("name", Some(name)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "categoryRow", pk = categoryId.toString, title = s"categoryId: $categoryId")
}
