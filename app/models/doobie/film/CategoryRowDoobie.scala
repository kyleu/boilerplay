/* Generated File */
package models.doobie.film

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.film.CategoryRow

object CategoryRowDoobie extends DoobieQueries[CategoryRow]("category") {
  override val countFragment = fr"""select count(*) from "category""""
  override val selectFragment = fr"""select "category_id", "name", "last_update" from "category""""

  override val columns = Seq("category_id", "name", "last_update")
  override val searchColumns = Seq("category_id", "name", "last_update")

  override def searchFragment(q: String) = {
    fr""""category_id"::text = $q or "name"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(categoryId: Int) = (selectFragment ++ whereAnd(fr"categoryId = $categoryId")).query[Option[CategoryRow]].unique
  def getByPrimaryKeySeq(categoryIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"categoryId", categoryIdSeq)).query[CategoryRow].to[Seq]
}
