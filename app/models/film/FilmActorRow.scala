/* Generated File */
package models.film

import com.kyleu.projectile.models.result.data.{DataField, DataFieldModel, DataSummary}
import com.kyleu.projectile.util.DateUtils
import com.kyleu.projectile.util.JsonSerializers._
import java.time.ZonedDateTime

object FilmActorRow {
  implicit val jsonEncoder: Encoder[FilmActorRow] = (r: FilmActorRow) => io.circe.Json.obj(
    ("actorId", r.actorId.asJson),
    ("filmId", r.filmId.asJson),
    ("lastUpdate", r.lastUpdate.asJson)
  )

  implicit val jsonDecoder: Decoder[FilmActorRow] = (c: io.circe.HCursor) => for {
    actorId <- c.downField("actorId").as[Int]
    filmId <- c.downField("filmId").as[Int]
    lastUpdate <- c.downField("lastUpdate").as[ZonedDateTime]
  } yield FilmActorRow(actorId, filmId, lastUpdate)

  def empty(
    actorId: Int = 0,
    filmId: Int = 0,
    lastUpdate: ZonedDateTime = DateUtils.nowZoned
  ) = {
    FilmActorRow(actorId, filmId, lastUpdate)
  }
}

final case class FilmActorRow(
    actorId: Int,
    filmId: Int,
    lastUpdate: ZonedDateTime
) extends DataFieldModel {
  override def toDataFields = Seq(
    DataField("actorId", Some(actorId.toString)),
    DataField("filmId", Some(filmId.toString)),
    DataField("lastUpdate", Some(lastUpdate.toString))
  )

  def toSummary = DataSummary(model = "filmActorRow", pk = actorId.toString + "/" + filmId.toString, title = s"actorId: $actorId, filmId: $filmId, lastUpdate: $lastUpdate")
}
