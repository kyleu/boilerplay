/* Generated File */
package models.table.address

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.address.CityRow
import scala.language.higherKinds

object CityRowTable {
  val query = TableQuery[CityRowTable]

  def getByPrimaryKey(cityId: Int) = query.filter(_.cityId === cityId).result.headOption
  def getByPrimaryKeySeq(cityIdSeq: Seq[Int]) = query.filter(_.cityId.inSet(cityIdSeq)).result

  def getByCountryId(countryId: Int) = query.filter(_.countryId === countryId).result
  def getByCountryIdSeq(countryIdSeq: Seq[Int]) = query.filter(_.countryId.inSet(countryIdSeq)).result

  implicit class CityRowTableExtensions[C[_]](q: Query[CityRowTable, CityRow, C]) {
    def withCountryRow = q.join(CountryRowTable.query).on(_.countryId === _.countryId)
    def withCountryRowOpt = q.joinLeft(CountryRowTable.query).on(_.countryId === _.countryId)
  }
}

class CityRowTable(tag: slick.lifted.Tag) extends Table[CityRow](tag, "city") {
  val cityId = column[Int]("city_id", O.PrimaryKey, O.AutoInc)
  val city = column[String]("city")
  val countryId = column[Int]("country_id")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (cityId, city, countryId, lastUpdate) <> (
    (CityRow.apply _).tupled,
    CityRow.unapply
  )
}

