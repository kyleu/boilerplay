/* Generated File */
package models.doobie.settings

import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import com.kyleu.projectile.services.database.doobie.DoobieTestHelper.yolo._
import models.doobie.settings.SettingKeyTypeDoobie.settingKeyTypeMeta
import models.settings.Setting
import org.scalatest._

class SettingDoobieTests extends FlatSpec with Matchers {

  "Doobie queries for [Setting]" should "typecheck" in {
    SettingDoobie.countFragment.query[Long].check.unsafeRunSync
    SettingDoobie.selectFragment.query[Setting].check.unsafeRunSync
    (SettingDoobie.selectFragment ++ whereAnd(SettingDoobie.searchFragment("..."))).query[Setting].check.unsafeRunSync
  }
}
