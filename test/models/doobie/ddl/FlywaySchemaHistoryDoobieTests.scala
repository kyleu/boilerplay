/* Generated File */
package models.doobie.ddl

import models.ddl.FlywaySchemaHistory
import models.doobie.DoobieTestHelper
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class FlywaySchemaHistoryDoobieTest extends FlatSpec with Matchers {
  import DoobieTestHelper.yolo._

  "Doobie queries for [FlywaySchemaHistory]" should "typecheck" in {
    FlywaySchemaHistoryDoobie.countFragment.query[Long].check.unsafeRunSync
    FlywaySchemaHistoryDoobie.selectFragment.query[FlywaySchemaHistory].check.unsafeRunSync
    (FlywaySchemaHistoryDoobie.selectFragment ++ whereAnd(FlywaySchemaHistoryDoobie.searchFragment("..."))).query[FlywaySchemaHistory].check.unsafeRunSync
  }
}
