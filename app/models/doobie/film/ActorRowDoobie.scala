/* Generated File */
package models.doobie.film

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.film.ActorRow

object ActorRowDoobie extends DoobieQueries[ActorRow]("actor") {
  override val countFragment = fr"""select count(*) from "actor""""
  override val selectFragment = fr"""select "actor_id", "first_name", "last_name", "last_update" from "actor""""

  override val columns = Seq("actor_id", "first_name", "last_name", "last_update")
  override val searchColumns = Seq("actor_id", "first_name", "last_name", "last_update")

  override def searchFragment(q: String) = {
    fr""""actor_id"::text = $q or "first_name"::text = $q or "last_name"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(actorId: Int) = (selectFragment ++ whereAnd(fr"actorId = $actorId")).query[Option[ActorRow]].unique
  def getByPrimaryKeySeq(actorIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"actorId", actorIdSeq)).query[ActorRow].to[Seq]
}
