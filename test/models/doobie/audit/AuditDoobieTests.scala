/* Generated File */
package models.doobie.audit

import models.audit.Audit
import models.doobie.DoobieTestHelper
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class AuditDoobieTest extends FlatSpec with Matchers {
  import DoobieTestHelper.yolo._

  "Doobie queries for [Audit]" should "typecheck" in {
    AuditDoobie.countFragment.query[Long].check.unsafeRunSync
    AuditDoobie.selectFragment.query[Audit].check.unsafeRunSync
    (AuditDoobie.selectFragment ++ whereAnd(AuditDoobie.searchFragment("..."))).query[Audit].check.unsafeRunSync
  }
}
