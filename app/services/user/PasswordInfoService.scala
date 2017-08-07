package services.user

import models.queries.user.PasswordInfoQueries
import models.user.PasswordInfo
import services.database.Database

object PasswordInfoService {
  def getById(provider: String, key: String) = Database.query(PasswordInfoQueries.getById(provider, key))
  def getByIdSeq(idSeq: Seq[(String, String)]) = Database.query(PasswordInfoQueries.getByIdSeq(idSeq))

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(PasswordInfoQueries.getAll(orderBy, limit, offset))
  }

  def searchCount(q: String) = Database.query(PasswordInfoQueries.searchCount(q))
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(PasswordInfoQueries.search(q, orderBy, limit, offset))
  }

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Database.query(PasswordInfoQueries.searchExact(q, orderBy, limit, offset))

  def insert(model: PasswordInfo) = Database.execute(PasswordInfoQueries.insert(model))
}
