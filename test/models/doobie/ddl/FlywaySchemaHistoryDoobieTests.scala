/* Generated File */
package models.doobie.ddl

import models.ddl.FlywaySchemaHistory
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class FlywaySchemaHistoryDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [FlywaySchemaHistory]" should "typecheck" in {
    FlywaySchemaHistoryDoobie.countFragment.query[Long].check.unsafeRunSync
    FlywaySchemaHistoryDoobie.selectFragment.query[FlywaySchemaHistory].check.unsafeRunSync
    (FlywaySchemaHistoryDoobie.selectFragment ++ whereAnd(FlywaySchemaHistoryDoobie.searchFragment("..."))).query[FlywaySchemaHistory].check.unsafeRunSync
  }
}
