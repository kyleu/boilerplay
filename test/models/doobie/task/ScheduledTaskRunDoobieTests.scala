/* Generated File */
package models.doobie.task

import models.task.ScheduledTaskRun
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class ScheduledTaskRunDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [ScheduledTaskRun]" should "typecheck" in {
    ScheduledTaskRunDoobie.countFragment.query[Long].check.unsafeRunSync
    ScheduledTaskRunDoobie.selectFragment.query[ScheduledTaskRun].check.unsafeRunSync
    (ScheduledTaskRunDoobie.selectFragment ++ whereAnd(ScheduledTaskRunDoobie.searchFragment("..."))).query[ScheduledTaskRun].check.unsafeRunSync
  }
}
