package services.history

import java.util.UUID

import com.github.mauricio.async.db.Connection
import models.database.queries.history.RequestLogQueries
import models.history.RequestLog
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database

object RequestHistoryService {
  def insert(log: RequestLog) = Database.execute(RequestLogQueries.insert(log)).map(i => log)

  def searchRequests(q: String, orderBy: String, page: Int) = for {
    count <- Database.query(RequestLogQueries.searchCount(q))
    list <- Database.query(RequestLogQueries.search(q, orderBy, Some(page)))
  } yield count -> list

  def getCountByUser(id: UUID) = Database.query(RequestLogQueries.getRequestCountForUser(id))

  def removeRequestsByUser(userId: UUID, conn: Option[Connection]) = Database.execute(RequestLogQueries.RemoveRequestsByUser(userId), conn)
}
