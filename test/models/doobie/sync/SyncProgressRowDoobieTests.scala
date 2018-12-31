/* Generated File */
package models.doobie.sync

import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import com.kyleu.projectile.services.database.doobie.DoobieTestHelper.yolo._
import models.sync.SyncProgressRow
import org.scalatest._

class SyncProgressRowDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [SyncProgressRow]" should "typecheck" in {
    SyncProgressRowDoobie.countFragment.query[Long].check.unsafeRunSync
    SyncProgressRowDoobie.selectFragment.query[SyncProgressRow].check.unsafeRunSync
    (SyncProgressRowDoobie.selectFragment ++ whereAnd(SyncProgressRowDoobie.searchFragment("..."))).query[SyncProgressRow].check.unsafeRunSync
  }
}
