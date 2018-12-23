/* Generated File */
package models.doobie.audit

import models.audit.AuditRecordRow
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class AuditRecordRowDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [AuditRecordRow]" should "typecheck" in {
    AuditRecordRowDoobie.countFragment.query[Long].check.unsafeRunSync
    AuditRecordRowDoobie.selectFragment.query[AuditRecordRow].check.unsafeRunSync
    (AuditRecordRowDoobie.selectFragment ++ whereAnd(AuditRecordRowDoobie.searchFragment("..."))).query[AuditRecordRow].check.unsafeRunSync
  }
}
