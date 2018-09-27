/* Generated File */
package models.doobie.sync

import models.sync.SyncProgress
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class SyncProgressDoobieTests extends FlatSpec with Matchers {
  import models.doobie.DoobieTestHelper.yolo._

  "Doobie queries for [SyncProgress]" should "typecheck" in {
    SyncProgressDoobie.countFragment.query[Long].check.unsafeRunSync
    SyncProgressDoobie.selectFragment.query[SyncProgress].check.unsafeRunSync
    (SyncProgressDoobie.selectFragment ++ whereAnd(SyncProgressDoobie.searchFragment("..."))).query[SyncProgress].check.unsafeRunSync
  }
}
