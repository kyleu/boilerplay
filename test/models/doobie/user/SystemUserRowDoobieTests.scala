/* Generated File */
package models.doobie.user

import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import com.kyleu.projectile.services.database.doobie.DoobieTestHelper.yolo._
import models.user.SystemUserRow
import org.scalatest._

class SystemUserRowDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [SystemUserRow]" should "typecheck" in {
    SystemUserRowDoobie.countFragment.query[Long].check.unsafeRunSync
    SystemUserRowDoobie.selectFragment.query[SystemUserRow].check.unsafeRunSync
    (SystemUserRowDoobie.selectFragment ++ whereAnd(SystemUserRowDoobie.searchFragment("..."))).query[SystemUserRow].check.unsafeRunSync
  }
}
