/* Generated File */
package models.doobie.user

import models.user.SystemUser
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class SystemUserDoobieTests extends FlatSpec with Matchers {
  import models.doobie.DoobieTestHelper.yolo._

  "Doobie queries for [SystemUser]" should "typecheck" in {
    SystemUserDoobie.countFragment.query[Long].check.unsafeRunSync
    SystemUserDoobie.selectFragment.query[SystemUser].check.unsafeRunSync
    (SystemUserDoobie.selectFragment ++ whereAnd(SystemUserDoobie.searchFragment("..."))).query[SystemUser].check.unsafeRunSync
  }
}
