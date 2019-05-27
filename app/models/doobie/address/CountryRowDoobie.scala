/* Generated File */
package models.doobie.address

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.address.CountryRow

object CountryRowDoobie extends DoobieQueries[CountryRow]("country") {
  override val countFragment = fr"""select count(*) from "country""""
  override val selectFragment = fr"""select "country_id", "country", "last_update" from "country""""

  override val columns = Seq("country_id", "country", "last_update")
  override val searchColumns = Seq("country_id", "country", "last_update")

  override def searchFragment(q: String) = {
    fr""""country_id"::text = $q or "country"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(countryId: Int) = (selectFragment ++ whereAnd(fr"countryId = $countryId")).query[Option[CountryRow]].unique
  def getByPrimaryKeySeq(countryIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"countryId", countryIdSeq)).query[CountryRow].to[Seq]
}
