/* Generated File */
package models.doobie.user

import models.doobie.DoobieTestHelper
import models.user.SystemUser
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class SystemUserDoobieTest extends FlatSpec with Matchers {
  import DoobieTestHelper.yolo._

  "Doobie queries for [SystemUser]" should "typecheck" in {
    SystemUserDoobie.countFragment.query[Long].check.unsafeRunSync
    SystemUserDoobie.selectFragment.query[SystemUser].check.unsafeRunSync
    (SystemUserDoobie.selectFragment ++ whereAnd(SystemUserDoobie.searchFragment("..."))).query[SystemUser].check.unsafeRunSync
  }
}
