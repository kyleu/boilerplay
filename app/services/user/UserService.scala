package services.user

import java.util.UUID

import models.queries.user.UserQueries
import models.user.User
import services.database.Database

object UserService {
  def getById(id: UUID) = Database.query(UserQueries.getById(id))
  def getByIdSeq(idSeq: Seq[UUID]) = Database.query(UserQueries.getByIdSeq(idSeq))

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(UserQueries.getAll(orderBy, limit, offset))
  }

  def searchCount(q: String) = Database.query(UserQueries.searchCount(q))
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(UserQueries.search(q, orderBy, limit, offset))
  }

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Database.query(UserQueries.searchExact(q, orderBy, limit, offset))

  def insert(model: User) = Database.execute(UserQueries.insert(model))
  def remove(id: UUID) = Database.execute(UserQueries.removeById(id))
}
