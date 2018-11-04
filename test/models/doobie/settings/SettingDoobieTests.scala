/* Generated File */
package models.doobie.settings

import models.doobie.settings.SettingKeyDoobie.settingKeyMeta
import models.settings.Setting
import org.scalatest._
import services.database.doobie.DoobieQueryService.Imports._
import services.database.doobie.DoobieTestHelper.yolo._

class SettingDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [Setting]" should "typecheck" in {
    SettingDoobie.countFragment.query[Long].check.unsafeRunSync
    SettingDoobie.selectFragment.query[Setting].check.unsafeRunSync
    (SettingDoobie.selectFragment ++ whereAnd(SettingDoobie.searchFragment("..."))).query[Setting].check.unsafeRunSync
  }
}
