/* Generated File */
package models.table.address

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.address.CountryRow

object CountryRowTable {
  val query = TableQuery[CountryRowTable]

  def getByPrimaryKey(countryId: Int) = query.filter(_.countryId === countryId).result.headOption
  def getByPrimaryKeySeq(countryIdSeq: Seq[Int]) = query.filter(_.countryId.inSet(countryIdSeq)).result
}

class CountryRowTable(tag: slick.lifted.Tag) extends Table[CountryRow](tag, "country") {
  val countryId = column[Int]("country_id", O.PrimaryKey, O.AutoInc)
  val country = column[String]("country")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (countryId, country, lastUpdate) <> (
    (CountryRow.apply _).tupled,
    CountryRow.unapply
  )
}

