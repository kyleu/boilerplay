/* Generated File */
package models.doobie.task

import models.doobie.DoobieTestHelper
import models.task.ScheduledTaskRun
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class ScheduledTaskRunDoobieTest extends FlatSpec with Matchers {
  import DoobieTestHelper.yolo._

  "Doobie queries for [ScheduledTaskRun]" should "typecheck" in {
    ScheduledTaskRunDoobie.countFragment.query[Long].check.unsafeRunSync
    ScheduledTaskRunDoobie.selectFragment.query[ScheduledTaskRun].check.unsafeRunSync
    (ScheduledTaskRunDoobie.selectFragment ++ whereAnd(ScheduledTaskRunDoobie.searchFragment("..."))).query[ScheduledTaskRun].check.unsafeRunSync
  }
}
