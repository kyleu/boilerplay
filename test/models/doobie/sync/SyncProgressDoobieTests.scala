/* Generated File */
package models.doobie.sync

import models.doobie.DoobieTestHelper
import models.sync.SyncProgress
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class SyncProgressDoobieTest extends FlatSpec with Matchers {
  import DoobieTestHelper.yolo._

  "Doobie queries for [SyncProgress]" should "typecheck" in {
    SyncProgressDoobie.countFragment.query[Long].check.unsafeRunSync
    SyncProgressDoobie.selectFragment.query[SyncProgress].check.unsafeRunSync
    (SyncProgressDoobie.selectFragment ++ whereAnd(SyncProgressDoobie.searchFragment("..."))).query[SyncProgress].check.unsafeRunSync
  }
}
