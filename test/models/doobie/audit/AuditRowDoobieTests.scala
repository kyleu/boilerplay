/* Generated File */
package models.doobie.audit

import models.audit.AuditRow
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class AuditRowDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [AuditRow]" should "typecheck" in {
    AuditRowDoobie.countFragment.query[Long].check.unsafeRunSync
    AuditRowDoobie.selectFragment.query[AuditRow].check.unsafeRunSync
    (AuditRowDoobie.selectFragment ++ whereAnd(AuditRowDoobie.searchFragment("..."))).query[AuditRow].check.unsafeRunSync
  }
}
