package models.database.queries.history

import java.util.UUID

import models.database.queries.BaseQueries
import models.database.{ Query, Row, Statement }
import models.history.RequestLog
import org.joda.time.{ LocalDate, LocalDateTime }

object RequestLogQueries extends BaseQueries[RequestLog] {
  override protected val tableName = "requests"
  override protected val columns = Seq(
    "id", "user_id", "auth_provider", "auth_key", "remote_address",
    "method", "host", "secure", "path", "query_string",
    "lang", "cookie", "referrer", "user_agent", "started", "duration", "status"
  )
  override protected val searchColumns = Seq("id::text", "user_id::text", "method", "host", "path", "referrer", "user_agent")

  val insert = Insert
  def searchCount(q: String, groupBy: Option[String] = None) = new SearchCount(q, groupBy)
  val search = Search

  case class GetRequestsByUser(id: UUID) extends Query[List[RequestLog]] {
    override val sql = getSql(Some("user_id = ?"), orderBy = Some("started desc"))
    override val values = Seq(id)
    override def reduce(rows: Iterator[Row]) = rows.map(fromRow).toList
  }

  def getRequestCountForUser(userId: UUID) = new Count(s"select count(*) as c from $tableName where user_id = ?", Seq(userId))

  case class RemoveRequestsByUser(userId: UUID) extends Statement {
    override val sql = s"delete from $tableName where user_id = ?"
    override val values = Seq(userId)
  }

  case object GetUserCounts extends Query[Seq[(UUID, Option[String], Int)]] {
    override val sql = """
      select r.user_id as id, u.username as un, count(r.id) as c
      from requests r join users u on r.user_id = u.id
      group by r.user_id, u.username
      order by c desc;
    """
    override def reduce(rows: Iterator[Row]) = rows.map { row =>
      (row.as[UUID]("id"), row.asOpt[String]("un"), row.as[Long]("c").toInt)
    }.toSeq
  }

  case class GetCounts(col: String) extends Query[Seq[(String, Int)]] {
    override val sql = s"select r.$col as col, count(r.id) as c from requests r group by r.$col order by c desc;"
    override def reduce(rows: Iterator[Row]) = rows.map { row =>
      row.asOpt[String]("col").getOrElse("null") -> row.asOpt[Long]("c").getOrElse(0L).toInt
    }.toSeq
  }

  case object GetEarliestDay extends Query[LocalDate] {
    override val sql = s"select min(started::date) as d from $tableName"
    override def reduce(rows: Iterator[Row]) = rows.next().as[LocalDate]("d")
  }

  override protected def fromRow(row: Row) = {
    val id = row.as[UUID]("id")
    val userId = row.as[UUID]("user_id")
    val authProvider = row.as[String]("auth_provider")
    val authKey = row.as[String]("auth_key")
    val remoteAddress = row.as[String]("remote_address")

    val method = row.as[String]("method")
    val host = row.as[String]("host")
    val secure = row.as[Boolean]("secure")
    val path = row.as[String]("path")
    val queryString = row.as[String]("query_string")

    val lang = row.asOpt[String]("lang")
    val cookie = row.asOpt[String]("cookie")
    val referrer = row.asOpt[String]("referrer")
    val userAgent = row.asOpt[String]("user_agent")
    val started = row.as[LocalDateTime]("started")
    val duration = row.as[Int]("duration")
    val status = row.as[Int]("status")

    RequestLog(
      id, userId, authProvider, authKey, remoteAddress,
      method, host, secure, path, queryString,
      lang, cookie, referrer, userAgent, started, duration, status
    )
  }

  override protected def toDataSeq(r: RequestLog) = Seq[Any](
    r.id, r.userId, r.authProvider, r.authKey, r.remoteAddress,
    r.method, r.host, r.secure, r.path, r.queryString,
    r.lang, r.cookie, r.referrer, r.userAgent, r.started, r.duration, r.status
  )
}
