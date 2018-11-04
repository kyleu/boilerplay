/* Generated File */
package models.doobie.audit

import models.audit.Audit
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class AuditDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [Audit]" should "typecheck" in {
    AuditDoobie.countFragment.query[Long].check.unsafeRunSync
    AuditDoobie.selectFragment.query[Audit].check.unsafeRunSync
    (AuditDoobie.selectFragment ++ whereAnd(AuditDoobie.searchFragment("..."))).query[Audit].check.unsafeRunSync
  }
}
