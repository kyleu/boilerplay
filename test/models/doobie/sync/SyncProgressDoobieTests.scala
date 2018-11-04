/* Generated File */
package models.doobie.sync

import models.sync.SyncProgress
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class SyncProgressDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [SyncProgress]" should "typecheck" in {
    SyncProgressDoobie.countFragment.query[Long].check.unsafeRunSync
    SyncProgressDoobie.selectFragment.query[SyncProgress].check.unsafeRunSync
    (SyncProgressDoobie.selectFragment ++ whereAnd(SyncProgressDoobie.searchFragment("..."))).query[SyncProgress].check.unsafeRunSync
  }
}
