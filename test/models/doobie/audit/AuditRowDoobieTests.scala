/* Custom File */
package models.doobie.audit

import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import com.kyleu.projectile.services.database.doobie.DoobieTestHelper.yolo._
import models.audit.AuditRow
import org.scalatest._

class AuditRowDoobieTests extends FlatSpec with Matchers {
  "Doobie queries for [AuditRow]" should "typecheck" in {
    /* Commented for CI
    AuditRowDoobie.countFragment.query[Long].check.unsafeRunSync
    AuditRowDoobie.selectFragment.query[AuditRow].check.unsafeRunSync
    (AuditRowDoobie.selectFragment ++ whereAnd(AuditRowDoobie.searchFragment("..."))).query[AuditRow].check.unsafeRunSync
    */
  }
}
