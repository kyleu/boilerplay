/* Generated File */
package models.doobie.note

import models.note.NoteRow
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class NoteRowDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [NoteRow]" should "typecheck" in {
    NoteRowDoobie.countFragment.query[Long].check.unsafeRunSync
    NoteRowDoobie.selectFragment.query[NoteRow].check.unsafeRunSync
    (NoteRowDoobie.selectFragment ++ whereAnd(NoteRowDoobie.searchFragment("..."))).query[NoteRow].check.unsafeRunSync
  }
}
