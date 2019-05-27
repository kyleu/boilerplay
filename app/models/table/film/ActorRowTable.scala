/* Generated File */
package models.table.film

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.film.ActorRow

object ActorRowTable {
  val query = TableQuery[ActorRowTable]

  def getByPrimaryKey(actorId: Int) = query.filter(_.actorId === actorId).result.headOption
  def getByPrimaryKeySeq(actorIdSeq: Seq[Int]) = query.filter(_.actorId.inSet(actorIdSeq)).result
}

class ActorRowTable(tag: slick.lifted.Tag) extends Table[ActorRow](tag, "actor") {
  val actorId = column[Int]("actor_id", O.PrimaryKey, O.AutoInc)
  val firstName = column[String]("first_name")
  val lastName = column[String]("last_name")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (actorId, firstName, lastName, lastUpdate) <> (
    (ActorRow.apply _).tupled,
    ActorRow.unapply
  )
}

