package services.ddl

import models.ddl.Ddl
import models.queries.ddl.DdlQueries
import services.database.Database

object DdlService {
  def getById(id: Int) = Database.query(DdlQueries.getById(id))
  def getByIdSeq(idSeq: Seq[Int]) = Database.query(DdlQueries.getByIdSeq(idSeq))

  def getAll(orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(DdlQueries.getAll(orderBy, limit, offset))
  }

  def searchCount(q: String) = Database.query(DdlQueries.searchCount(q))
  def search(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = {
    Database.query(DdlQueries.search(q, orderBy, limit, offset))
  }

  def searchExact(q: String, orderBy: Option[String] = None, limit: Option[Int] = None, offset: Option[Int] = None) = Database.query(DdlQueries.searchExact(q, orderBy, limit, offset))

  def insert(model: Ddl) = Database.execute(DdlQueries.insert(model))
  def remove(id: Int) = Database.execute(DdlQueries.removeById(id))
}
