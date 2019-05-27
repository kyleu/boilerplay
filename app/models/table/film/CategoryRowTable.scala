/* Generated File */
package models.table.film

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._
import java.time.ZonedDateTime
import models.film.CategoryRow

object CategoryRowTable {
  val query = TableQuery[CategoryRowTable]

  def getByPrimaryKey(categoryId: Int) = query.filter(_.categoryId === categoryId).result.headOption
  def getByPrimaryKeySeq(categoryIdSeq: Seq[Int]) = query.filter(_.categoryId.inSet(categoryIdSeq)).result
}

class CategoryRowTable(tag: slick.lifted.Tag) extends Table[CategoryRow](tag, "category") {
  val categoryId = column[Int]("category_id", O.PrimaryKey, O.AutoInc)
  val name = column[String]("name")
  val lastUpdate = column[ZonedDateTime]("last_update")

  override val * = (categoryId, name, lastUpdate) <> (
    (CategoryRow.apply _).tupled,
    CategoryRow.unapply
  )
}

