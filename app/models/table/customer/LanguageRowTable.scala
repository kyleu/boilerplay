/* Generated File */
package models.table.customer

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.customer.LanguageRow

object LanguageRowTable {
  val query = TableQuery[LanguageRowTable]

  def getByPrimaryKey(languageId: Int) = query.filter(_.languageId === languageId).result.headOption
  def getByPrimaryKeySeq(languageIdSeq: Seq[Int]) = query.filter(_.languageId.inSet(languageIdSeq)).result
}

class LanguageRowTable(tag: slick.lifted.Tag) extends Table[LanguageRow](tag, "language") {
  val languageId = column[Int]("language_id", O.PrimaryKey, O.AutoInc)
  val name = column[String]("name")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (languageId, name, lastUpdate) <> (
    (LanguageRow.apply _).tupled,
    LanguageRow.unapply
  )
}

