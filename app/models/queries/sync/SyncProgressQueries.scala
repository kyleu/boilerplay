/* Generated File */
package models.queries.sync

import java.time.LocalDateTime
import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.queries.BaseQueries
import models.result.ResultFieldHelper
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import models.sync.SyncProgress

object SyncProgressQueries extends BaseQueries[SyncProgress]("syncProgress", "sync_progress") {
  override val fields = Seq(
    DatabaseField(title = "Key", prop = "key", col = "key", typ = StringType),
    DatabaseField(title = "Status", prop = "status", col = "status", typ = StringType),
    DatabaseField(title = "Message", prop = "message", col = "message", typ = StringType),
    DatabaseField(title = "Last Time", prop = "lastTime", col = "last_time", typ = TimestampType)
  )
  override protected val pkColumns = Seq("key")
  override protected val searchColumns = Seq("key", "status", "message", "last_time")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(key: String) = new GetByPrimaryKey(Seq(key))
  def getByPrimaryKeySeq(keySeq: Seq[String]) = new ColSeqQuery(column = "key", values = keySeq)

  final case class CountByKey(key: String) extends ColCount(column = "key", values = Seq(key))
  final case class GetByKey(key: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("key") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(key)
  )
  final case class GetByKeySeq(keySeq: Seq[String]) extends ColSeqQuery(column = "key", values = keySeq)

  final case class CountByLastTime(lastTime: LocalDateTime) extends ColCount(column = "last_time", values = Seq(lastTime))
  final case class GetByLastTime(lastTime: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_time") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastTime)
  )
  final case class GetByLastTimeSeq(lastTimeSeq: Seq[LocalDateTime]) extends ColSeqQuery(column = "last_time", values = lastTimeSeq)

  final case class CountByMessage(message: String) extends ColCount(column = "message", values = Seq(message))
  final case class GetByMessage(message: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("message") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(message)
  )
  final case class GetByMessageSeq(messageSeq: Seq[String]) extends ColSeqQuery(column = "message", values = messageSeq)

  final case class CountByStatus(status: String) extends ColCount(column = "status", values = Seq(status))
  final case class GetByStatus(status: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("status") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(status)
  )
  final case class GetByStatusSeq(statusSeq: Seq[String]) extends ColSeqQuery(column = "status", values = statusSeq)

  def insert(model: SyncProgress) = new Insert(model)
  def insertBatch(models: Seq[SyncProgress]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def removeByPrimaryKey(key: String) = new RemoveByPrimaryKey(Seq[Any](key))

  def update(key: String, fields: Seq[DataField]) = new UpdateFields(Seq[Any](key), fields)

  override def fromRow(row: Row) = SyncProgress(
    key = StringType(row, "key"),
    status = StringType(row, "status"),
    message = StringType(row, "message"),
    lastTime = TimestampType(row, "last_time")
  )
}
