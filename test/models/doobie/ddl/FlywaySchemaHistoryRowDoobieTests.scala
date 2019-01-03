/* Custom File */
package models.doobie.ddl

import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import com.kyleu.projectile.services.database.doobie.DoobieTestHelper.yolo._
import models.ddl.FlywaySchemaHistoryRow
import org.scalatest._

class FlywaySchemaHistoryRowDoobieTests extends FlatSpec with Matchers {
  "Doobie queries for [FlywaySchemaHistoryRow]" should "typecheck" in {
    /* Commented for CI
    FlywaySchemaHistoryRowDoobie.countFragment.query[Long].check.unsafeRunSync
    FlywaySchemaHistoryRowDoobie.selectFragment.query[FlywaySchemaHistoryRow].check.unsafeRunSync
    (FlywaySchemaHistoryRowDoobie.selectFragment ++ whereAnd(FlywaySchemaHistoryRowDoobie.searchFragment("..."))).query[FlywaySchemaHistoryRow].check.unsafeRunSync
    */
  }
}
