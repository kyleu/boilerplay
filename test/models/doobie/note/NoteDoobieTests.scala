/* Generated File */
package models.doobie.note

import models.doobie.DoobieTestHelper
import models.note.Note
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class NoteDoobieTest extends FlatSpec with Matchers {
  import DoobieTestHelper.yolo._

  "Doobie queries for [Note]" should "typecheck" in {
    NoteDoobie.countFragment.query[Long].check.unsafeRunSync
    NoteDoobie.selectFragment.query[Note].check.unsafeRunSync
    (NoteDoobie.selectFragment ++ whereAnd(NoteDoobie.searchFragment("..."))).query[Note].check.unsafeRunSync
  }
}
