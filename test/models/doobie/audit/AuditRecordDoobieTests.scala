/* Generated File */
package models.doobie.audit

import models.audit.AuditRecord
import models.doobie.DoobieTestHelper
import org.scalatest._
import services.database.DoobieQueryService.Imports._

class AuditRecordDoobieTest extends FlatSpec with Matchers {
  import DoobieTestHelper.yolo._

  "Doobie queries for [AuditRecord]" should "typecheck" in {
    AuditRecordDoobie.countFragment.query[Long].check.unsafeRunSync
    AuditRecordDoobie.selectFragment.query[AuditRecord].check.unsafeRunSync
    (AuditRecordDoobie.selectFragment ++ whereAnd(AuditRecordDoobie.searchFragment("..."))).query[AuditRecord].check.unsafeRunSync
  }
}
