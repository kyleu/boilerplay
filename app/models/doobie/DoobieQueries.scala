package models.doobie

import models.queries.EngineHelper
import services.database.DoobieQueryService.Imports._

abstract class DoobieQueries[T: Composite](tableName: String) {
  protected def countFragment: Fragment
  protected def selectFragment: Fragment
  protected def searchFragment(q: String): Fragment

  protected def quote(n: String) = EngineHelper.quote(n)

  protected def columns: Seq[String]
  protected def columnsString = columns.map(quote).mkString(", ")
  protected def searchColumns: Seq[String]

  def countAll = countFragment.query[Int].unique
  def getAll = selectFragment.query[T].to[Seq]

  def search(q: String) = (selectFragment ++ whereAnd(searchFragment(q))).query[T].to[Seq]
}
