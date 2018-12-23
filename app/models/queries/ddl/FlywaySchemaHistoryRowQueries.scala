/* Generated File */
package models.queries.ddl

import java.time.LocalDateTime
import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.ddl.FlywaySchemaHistoryRow
import models.queries.{BaseQueries, ResultFieldHelper}
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy

object FlywaySchemaHistoryRowQueries extends BaseQueries[FlywaySchemaHistoryRow]("flywaySchemaHistoryRow", "flyway_schema_history") {
  override val fields = Seq(
    DatabaseField(title = "Installed Rank", prop = "installedRank", col = "installed_rank", typ = LongType),
    DatabaseField(title = "Version", prop = "version", col = "version", typ = StringType),
    DatabaseField(title = "Description", prop = "description", col = "description", typ = StringType),
    DatabaseField(title = "Type", prop = "typ", col = "type", typ = StringType),
    DatabaseField(title = "Script", prop = "script", col = "script", typ = StringType),
    DatabaseField(title = "Checksum", prop = "checksum", col = "checksum", typ = LongType),
    DatabaseField(title = "Installed By", prop = "installedBy", col = "installed_by", typ = StringType),
    DatabaseField(title = "Installed On", prop = "installedOn", col = "installed_on", typ = TimestampType),
    DatabaseField(title = "Execution Time", prop = "executionTime", col = "execution_time", typ = LongType),
    DatabaseField(title = "Success", prop = "success", col = "success", typ = BooleanType)
  )
  override protected val pkColumns = Seq("installed_rank")
  override protected val searchColumns = Seq("installed_rank", "version", "description", "type", "installed_on", "success")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(installedRank: Long) = new GetByPrimaryKey(Seq(installedRank))
  def getByPrimaryKeySeq(installedRankSeq: Seq[Long]) = new ColSeqQuery(column = "installed_rank", values = installedRankSeq)

  final case class CountByDescription(description: String) extends ColCount(column = "description", values = Seq(description))
  final case class GetByDescription(description: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("description") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(description)
  )
  final case class GetByDescriptionSeq(descriptionSeq: Seq[String]) extends ColSeqQuery(column = "description", values = descriptionSeq)

  final case class CountByInstalledOn(installedOn: LocalDateTime) extends ColCount(column = "installed_on", values = Seq(installedOn))
  final case class GetByInstalledOn(installedOn: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("installed_on") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(installedOn)
  )
  final case class GetByInstalledOnSeq(installedOnSeq: Seq[LocalDateTime]) extends ColSeqQuery(column = "installed_on", values = installedOnSeq)

  final case class CountByInstalledRank(installedRank: Long) extends ColCount(column = "installed_rank", values = Seq(installedRank))
  final case class GetByInstalledRank(installedRank: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("installed_rank") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(installedRank)
  )
  final case class GetByInstalledRankSeq(installedRankSeq: Seq[Long]) extends ColSeqQuery(column = "installed_rank", values = installedRankSeq)

  final case class CountBySuccess(success: Boolean) extends ColCount(column = "success", values = Seq(success))
  final case class GetBySuccess(success: Boolean, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("success") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(success)
  )
  final case class GetBySuccessSeq(successSeq: Seq[Boolean]) extends ColSeqQuery(column = "success", values = successSeq)

  final case class CountByTyp(typ: String) extends ColCount(column = "type", values = Seq(typ))
  final case class GetByTyp(typ: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("type") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(typ)
  )
  final case class GetByTypSeq(typSeq: Seq[String]) extends ColSeqQuery(column = "type", values = typSeq)

  final case class CountByVersion(version: String) extends ColCount(column = "version", values = Seq(version))
  final case class GetByVersion(version: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("version") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(version)
  )
  final case class GetByVersionSeq(versionSeq: Seq[String]) extends ColSeqQuery(column = "version", values = versionSeq)

  def insert(model: FlywaySchemaHistoryRow) = new Insert(model)
  def insertBatch(models: Seq[FlywaySchemaHistoryRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new CreateFields(dataFields)

  def removeByPrimaryKey(installedRank: Long) = new RemoveByPrimaryKey(Seq[Any](installedRank))

  def update(installedRank: Long, fields: Seq[DataField]) = new UpdateFields(Seq[Any](installedRank), fields)

  override def fromRow(row: Row) = FlywaySchemaHistoryRow(
    installedRank = LongType(row, "installed_rank"),
    version = StringType.opt(row, "version"),
    description = StringType(row, "description"),
    typ = StringType(row, "type"),
    script = StringType(row, "script"),
    checksum = LongType.opt(row, "checksum"),
    installedBy = StringType(row, "installed_by"),
    installedOn = TimestampType(row, "installed_on"),
    executionTime = LongType(row, "execution_time"),
    success = BooleanType(row, "success")
  )
}
