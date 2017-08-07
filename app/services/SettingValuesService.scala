package services

import models.SettingValues
import models.queries.SettingValuesQueries
import services.database.Database

object SettingValuesService {
  def getById(k: String) = Database.query(SettingValuesQueries.getById(k))
  def getByIdSeq(kSeq: Seq[String]) = Database.query(SettingValuesQueries.getByIdSeq(kSeq))

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(SettingValuesQueries.getAll(orderBy, limit, offset))
  }

  def searchCount(q: String) = Database.query(SettingValuesQueries.searchCount(q))
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(SettingValuesQueries.search(q, orderBy, limit, offset))
  }

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Database.query(SettingValuesQueries.searchExact(q, orderBy, limit, offset))

  def insert(model: SettingValues) = Database.execute(SettingValuesQueries.insert(model))
  def remove(k: String) = Database.execute(SettingValuesQueries.removeById(k))
}
