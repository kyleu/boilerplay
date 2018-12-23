/* Generated File */
package models.doobie.sync

import models.sync.SyncProgressRow
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class SyncProgressRowDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [SyncProgressRow]" should "typecheck" in {
    SyncProgressRowDoobie.countFragment.query[Long].check.unsafeRunSync
    SyncProgressRowDoobie.selectFragment.query[SyncProgressRow].check.unsafeRunSync
    (SyncProgressRowDoobie.selectFragment ++ whereAnd(SyncProgressRowDoobie.searchFragment("..."))).query[SyncProgressRow].check.unsafeRunSync
  }
}
