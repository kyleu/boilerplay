/* Generated File */
package models.doobie.customer

import cats.data.NonEmptyList
import com.kyleu.projectile.services.database.doobie.DoobieQueries
import com.kyleu.projectile.services.database.doobie.DoobieQueryService.Imports._
import models.customer.LanguageRow

object LanguageRowDoobie extends DoobieQueries[LanguageRow]("language") {
  override val countFragment = fr"""select count(*) from "language""""
  override val selectFragment = fr"""select "language_id", "name", "last_update" from "language""""

  override val columns = Seq("language_id", "name", "last_update")
  override val searchColumns = Seq("language_id", "name", "last_update")

  override def searchFragment(q: String) = {
    fr""""language_id"::text = $q or "name"::text = $q or "last_update"::text = $q"""
  }

  def getByPrimaryKey(languageId: Int) = (selectFragment ++ whereAnd(fr"languageId = $languageId")).query[Option[LanguageRow]].unique
  def getByPrimaryKeySeq(languageIdSeq: NonEmptyList[Int]) = (selectFragment ++ in(fr"languageId", languageIdSeq)).query[LanguageRow].to[Seq]
}
