package services.audit

import java.util.UUID

import models.audit.ClientTraceResult
import models.database.queries.audit.ClientTraceQueries
import org.joda.time.LocalDateTime
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.JsObject
import services.database.Database

object ClientTraceService {
  def getTrace(id: UUID) = Database.query(ClientTraceQueries.getById(Seq(id)))

  def persistTrace(userId: UUID, data: JsObject) = {
    val ctr = ClientTraceResult(
      id = UUID.randomUUID,
      player = userId,
      data = data,
      created = new LocalDateTime()
    )
    Database.execute(ClientTraceQueries.insert(ctr))
  }

  def searchTraces(q: String, orderBy: String, page: Int) = for {
    count <- Database.query(ClientTraceQueries.searchCount(q))
    list <- Database.query(ClientTraceQueries.search(q, getOrderClause(orderBy), Some(page)))
  } yield count -> list

  def remove(id: UUID) = Database.execute(ClientTraceQueries.removeById(Seq(id))).map(_ == 1)

  private[this] def getOrderClause(sortBy: String) = sortBy match {
    case "created" => "created desc"
    case x => x
  }
}
