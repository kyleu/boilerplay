/* Generated File */
package models.table.film

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.film.FilmActorRow
import scala.language.higherKinds

object FilmActorRowTable {
  val query = TableQuery[FilmActorRowTable]

  def getByPrimaryKey(actorId: Int, filmId: Int) = query.filter(o => o.actorId === actorId && o.filmId === filmId).result.headOption

  def getByFilmId(filmId: Int) = query.filter(_.filmId === filmId).result
  def getByFilmIdSeq(filmIdSeq: Seq[Int]) = query.filter(_.filmId.inSet(filmIdSeq)).result

  def getByActorId(actorId: Int) = query.filter(_.actorId === actorId).result
  def getByActorIdSeq(actorIdSeq: Seq[Int]) = query.filter(_.actorId.inSet(actorIdSeq)).result

  implicit class FilmActorRowTableExtensions[C[_]](q: Query[FilmActorRowTable, FilmActorRow, C]) {
    def withActorRow = q.join(ActorRowTable.query).on(_.actorId === _.actorId)
    def withActorRowOpt = q.joinLeft(ActorRowTable.query).on(_.actorId === _.actorId)
    def withFilmRow = q.join(FilmRowTable.query).on(_.filmId === _.filmId)
    def withFilmRowOpt = q.joinLeft(FilmRowTable.query).on(_.filmId === _.filmId)
  }
}

class FilmActorRowTable(tag: slick.lifted.Tag) extends Table[FilmActorRow](tag, "film_actor") {
  val actorId = column[Int]("actor_id")
  val filmId = column[Int]("film_id")
  val lastUpdate = column[ZonedDateTime]("last_update")

  val modelPrimaryKey = primaryKey("pk_film_actor", (actorId, filmId))

  override val * = (actorId, filmId, lastUpdate) <> (
    (FilmActorRow.apply _).tupled,
    FilmActorRow.unapply
  )
}

