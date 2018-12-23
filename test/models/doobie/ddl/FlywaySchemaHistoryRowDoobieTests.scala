/* Generated File */
package models.doobie.ddl

import models.ddl.FlywaySchemaHistoryRow
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class FlywaySchemaHistoryRowDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [FlywaySchemaHistoryRow]" should "typecheck" in {
    FlywaySchemaHistoryRowDoobie.countFragment.query[Long].check.unsafeRunSync
    FlywaySchemaHistoryRowDoobie.selectFragment.query[FlywaySchemaHistoryRow].check.unsafeRunSync
    (FlywaySchemaHistoryRowDoobie.selectFragment ++ whereAnd(FlywaySchemaHistoryRowDoobie.searchFragment("..."))).query[FlywaySchemaHistoryRow].check.unsafeRunSync
  }
}
