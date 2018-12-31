/* Generated File */
package models.doobie.settings

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.doobie.settings.SettingKeyTypeDoobie.settingKeyTypeMeta
import models.settings.{Setting, SettingKeyType}

object SettingDoobie extends DoobieQueries[Setting]("setting_values") {
  override val countFragment = fr"""select count(*) from "setting_values""""
  override val selectFragment = fr"""select "k", "v" from "setting_values""""

  override val columns = Seq("k", "v")
  override val searchColumns = Seq("k")

  override def searchFragment(q: String) = {
    fr""""k"::text = $q or "v"::text = $q"""
  }

  def getByPrimaryKey(k: SettingKeyType) = (selectFragment ++ whereAnd(fr"k = $k")).query[Option[Setting]].unique
  def getByPrimaryKeySeq(kSeq: NonEmptyList[SettingKeyType]) = (selectFragment ++ in(fr"k", kSeq)).query[Setting].to[Seq]
}
