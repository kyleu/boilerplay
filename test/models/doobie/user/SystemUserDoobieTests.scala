/* Generated File */
package models.doobie.user

import models.user.SystemUser
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class SystemUserDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [SystemUser]" should "typecheck" in {
    SystemUserDoobie.countFragment.query[Long].check.unsafeRunSync
    SystemUserDoobie.selectFragment.query[SystemUser].check.unsafeRunSync
    (SystemUserDoobie.selectFragment ++ whereAnd(SystemUserDoobie.searchFragment("..."))).query[SystemUser].check.unsafeRunSync
  }
}
