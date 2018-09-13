/* Generated File */
package services.ddl

import java.time.LocalDateTime
import models.auth.Credentials
import models.ddl.FlywaySchemaHistory
import models.queries.ddl.FlywaySchemaHistoryQueries
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy
import scala.concurrent.Future
import services.ModelServiceHelper
import services.database.ApplicationDatabase
import util.FutureUtils.serviceContext
import util.tracing.{TraceData, TracingService}

@javax.inject.Singleton
class FlywaySchemaHistoryService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[FlywaySchemaHistory]("flywaySchemaHistory") {
  def getByPrimaryKey(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.getByPrimaryKey(installedRank))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = getByPrimaryKey(creds, installedRank).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load flywaySchemaHistory with installedRank [$installedRank]."))
  }
  def getByPrimaryKeySeq(creds: Credentials, installedRankSeq: Seq[Long])(implicit trace: TraceData) = {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.getByPrimaryKeySeq(installedRankSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByExecutionTime(creds: Credentials, executionTime: Long)(implicit trace: TraceData) = traceF("count.by.executionTime") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.CountByExecutionTime(executionTime))(td)
  }
  def getByExecutionTime(creds: Credentials, executionTime: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.executionTime") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByExecutionTime(executionTime, orderBys, limit, offset))(td)
  }
  def getByExecutionTimeSeq(creds: Credentials, executionTimeSeq: Seq[Long])(implicit trace: TraceData) = traceF("get.by.executionTime.seq") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByExecutionTimeSeq(executionTimeSeq))(td)
  }

  def countByInstalledOn(creds: Credentials, installedOn: LocalDateTime)(implicit trace: TraceData) = traceF("count.by.installedOn") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.CountByInstalledOn(installedOn))(td)
  }
  def getByInstalledOn(creds: Credentials, installedOn: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.installedOn") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByInstalledOn(installedOn, orderBys, limit, offset))(td)
  }
  def getByInstalledOnSeq(creds: Credentials, installedOnSeq: Seq[LocalDateTime])(implicit trace: TraceData) = traceF("get.by.installedOn.seq") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByInstalledOnSeq(installedOnSeq))(td)
  }

  def countByInstalledRank(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = traceF("count.by.installedRank") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.CountByInstalledRank(installedRank))(td)
  }
  def getByInstalledRank(creds: Credentials, installedRank: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.installedRank") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByInstalledRank(installedRank, orderBys, limit, offset))(td)
  }
  def getByInstalledRankSeq(creds: Credentials, installedRankSeq: Seq[Long])(implicit trace: TraceData) = traceF("get.by.installedRank.seq") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByInstalledRankSeq(installedRankSeq))(td)
  }

  def countByScript(creds: Credentials, script: String)(implicit trace: TraceData) = traceF("count.by.script") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.CountByScript(script))(td)
  }
  def getByScript(creds: Credentials, script: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.script") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByScript(script, orderBys, limit, offset))(td)
  }
  def getByScriptSeq(creds: Credentials, scriptSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.script.seq") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByScriptSeq(scriptSeq))(td)
  }

  def countBySuccess(creds: Credentials, success: Boolean)(implicit trace: TraceData) = traceF("count.by.success") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.CountBySuccess(success))(td)
  }
  def getBySuccess(creds: Credentials, success: Boolean, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.success") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetBySuccess(success, orderBys, limit, offset))(td)
  }
  def getBySuccessSeq(creds: Credentials, successSeq: Seq[Boolean])(implicit trace: TraceData) = traceF("get.by.success.seq") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetBySuccessSeq(successSeq))(td)
  }

  def countByTyp(creds: Credentials, typ: String)(implicit trace: TraceData) = traceF("count.by.typ") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.CountByTyp(typ))(td)
  }
  def getByTyp(creds: Credentials, typ: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.typ") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByTyp(typ, orderBys, limit, offset))(td)
  }
  def getByTypSeq(creds: Credentials, typSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.typ.seq") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByTypSeq(typSeq))(td)
  }

  def countByVersion(creds: Credentials, version: String)(implicit trace: TraceData) = traceF("count.by.version") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.CountByVersion(version))(td)
  }
  def getByVersion(creds: Credentials, version: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.version") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByVersion(version, orderBys, limit, offset))(td)
  }
  def getByVersionSeq(creds: Credentials, versionSeq: Seq[String])(implicit trace: TraceData) = traceF("get.by.version.seq") { td =>
    ApplicationDatabase.queryF(FlywaySchemaHistoryQueries.GetByVersionSeq(versionSeq))(td)
  }

  // Mutations
  def insert(creds: Credentials, model: FlywaySchemaHistory)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(FlywaySchemaHistoryQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.installedRank)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Flyway Schema History.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[FlywaySchemaHistory])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(FlywaySchemaHistoryQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(FlywaySchemaHistoryQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "installedRank").toLong)
    }
  }

  def remove(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, installedRank)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(FlywaySchemaHistoryQueries.removeByPrimaryKey(installedRank))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find FlywaySchemaHistory matching [$installedRank].")
    })
  }

  def update(creds: Credentials, installedRank: Long, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, installedRank)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Flyway Schema History [$installedRank].")
      case Some(_) => ApplicationDatabase.executeF(FlywaySchemaHistoryQueries.update(installedRank, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, installedRank)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Flyway Schema History [$installedRank]."
          case None => throw new IllegalStateException(s"Cannot find FlywaySchemaHistory matching [$installedRank].")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find FlywaySchemaHistory matching [$installedRank].")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[FlywaySchemaHistory])(implicit trace: TraceData) = {
    traceB("export.csv")(td => util.CsvUtils.csvFor(Some(key), totalCount, rows, FlywaySchemaHistoryQueries.fields)(td))
  }
}
