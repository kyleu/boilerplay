/* Generated File */
package models.doobie.film

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.film.FilmActorRow

object FilmActorRowDoobie extends DoobieQueries[FilmActorRow]("film_actor") {
  override val countFragment = fr"""select count(*) from "film_actor""""
  override val selectFragment = fr"""select "actor_id", "film_id", "last_update" from "film_actor""""

  override val columns = Seq("actor_id", "film_id", "last_update")
  override val searchColumns = Seq("actor_id", "film_id", "last_update")

  override def searchFragment(q: String) = {
    fr""""actor_id"::text = $q or "film_id"::text = $q or "last_update"::text = $q"""
  }

  // def getByPrimaryKey(actorId: Int, filmId: Int) = ???

  def countByFilmId(filmId: Int) = (countFragment ++ whereAnd(fr"filmId = $filmId")).query[Int].unique
  def getByFilmId(filmId: Int) = (selectFragment ++ whereAnd(fr"filmId = $filmId")).query[FilmActorRow].to[Seq]
  def getByFilmIdSeq(filmIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"filmId", filmIdSeq))).query[FilmActorRow].to[Seq]

  def countByActorId(actorId: Int) = (countFragment ++ whereAnd(fr"actorId = $actorId")).query[Int].unique
  def getByActorId(actorId: Int) = (selectFragment ++ whereAnd(fr"actorId = $actorId")).query[FilmActorRow].to[Seq]
  def getByActorIdSeq(actorIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"actorId", actorIdSeq))).query[FilmActorRow].to[Seq]
}
