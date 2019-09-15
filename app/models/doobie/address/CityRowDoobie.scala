/* Generated File */
package models.doobie.address

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.address.CityRow

object CityRowDoobie extends DoobieQueries[CityRow]("city") {
  override val countFragment = fr"""select count(*) from "city""""
  override val selectFragment = fr"""select "city_id", "city", "country_id", "last_update" from "city""""

  override val columns = Seq("city_id", "city", "country_id", "last_update")
  override val searchColumns = Seq("city_id", "city", "country_id", "last_update")

  override def searchFragment(q: String) = {
    fr""""city_id"::text = $q or "city"::text = $q or "country_id"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(cityId: Int) = (selectFragment ++ whereAnd(fr"cityId = $cityId")).query[Option[CityRow]].unique
  def getByPrimaryKeySeq(cityIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"cityId", cityIdSeq)).query[CityRow].to[Seq]

  def countByCountryId(countryId: Int) = (countFragment ++ whereAnd(fr"countryId = $countryId")).query[Int].unique
  def getByCountryId(countryId: Int) = (selectFragment ++ whereAnd(fr"countryId = $countryId")).query[CityRow].to[Seq]
  def getByCountryIdSeq(countryIdSeq: NonEmptyList[Int]) = (selectFragment ++ whereAnd(in(fr"countryId", countryIdSeq))).query[CityRow].to[Seq]
}
