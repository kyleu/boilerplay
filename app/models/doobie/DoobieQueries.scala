package models.doobie

import models.queries.EngineHelper
import services.database.DoobieQueryService.Imports._

abstract class DoobieQueries[T: Composite](tableName: String) {
  def countFragment: Fragment
  def selectFragment: Fragment
  def searchFragment(q: String): Fragment

  protected def quote(n: String) = EngineHelper.quote(n)

  def columns: Seq[String]
  def columnsString = columns.map(quote).mkString(", ")
  def searchColumns: Seq[String]

  def countAll = countFragment.query[Long].unique
  def getAll = selectFragment.query[T].to[Seq]

  def search(q: String) = (selectFragment ++ whereAnd(searchFragment(q))).query[T].to[Seq]
}
