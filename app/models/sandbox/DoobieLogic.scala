package models.sandbox

import java.util.UUID

import models.queries.note.NoteDoobieQueries
import services.database.ApplicationDatabase

import scala.concurrent.Future

object DoobieLogic {
  case class Foo(x: UUID, y: String)

  def test() = {
    val r = ApplicationDatabase.doobie.runSync(NoteDoobieQueries.countAll)
    Future.successful("Doobie: " + r)
  }
}
