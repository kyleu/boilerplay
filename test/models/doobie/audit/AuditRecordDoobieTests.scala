/* Generated File */
package models.doobie.audit

import models.audit.AuditRecord
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class AuditRecordDoobieTests extends FlatSpec with Matchers {
  import models.doobie.DoobieTestHelper.yolo._

  "Doobie queries for [AuditRecord]" should "typecheck" in {
    AuditRecordDoobie.countFragment.query[Long].check.unsafeRunSync
    AuditRecordDoobie.selectFragment.query[AuditRecord].check.unsafeRunSync
    (AuditRecordDoobie.selectFragment ++ whereAnd(AuditRecordDoobie.searchFragment("..."))).query[AuditRecord].check.unsafeRunSync
  }
}
