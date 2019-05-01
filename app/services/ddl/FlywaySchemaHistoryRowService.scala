/* Generated File */
package services.ddl

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.LocalDateTime
import models.ddl.FlywaySchemaHistoryRow
import models.queries.ddl.FlywaySchemaHistoryRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class FlywaySchemaHistoryRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[FlywaySchemaHistoryRow]("flywaySchemaHistoryRow") {
  def getByPrimaryKey(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(FlywaySchemaHistoryRowQueries.getByPrimaryKey(installedRank))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = getByPrimaryKey(creds, installedRank).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load flywaySchemaHistoryRow with installedRank [$installedRank]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, installedRankSeq: Seq[Long])(implicit trace: TraceData) = if (installedRankSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(FlywaySchemaHistoryRowQueries.getByPrimaryKeySeq(installedRankSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(FlywaySchemaHistoryRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(FlywaySchemaHistoryRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(FlywaySchemaHistoryRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(FlywaySchemaHistoryRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(FlywaySchemaHistoryRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByDescription(creds: Credentials, description: String)(implicit trace: TraceData) = traceF("count.by.description") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.CountByDescription(description))(td)
  }
  def getByDescription(creds: Credentials, description: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.description") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.GetByDescription(description, orderBys, limit, offset))(td)
  }
  def getByDescriptionSeq(creds: Credentials, descriptionSeq: Seq[String])(implicit trace: TraceData) = if (descriptionSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.description.seq") { td =>
      db.queryF(FlywaySchemaHistoryRowQueries.GetByDescriptionSeq(descriptionSeq))(td)
    }
  }

  def countByInstalledOn(creds: Credentials, installedOn: LocalDateTime)(implicit trace: TraceData) = traceF("count.by.installedOn") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.CountByInstalledOn(installedOn))(td)
  }
  def getByInstalledOn(creds: Credentials, installedOn: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.installedOn") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.GetByInstalledOn(installedOn, orderBys, limit, offset))(td)
  }
  def getByInstalledOnSeq(creds: Credentials, installedOnSeq: Seq[LocalDateTime])(implicit trace: TraceData) = if (installedOnSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.installedOn.seq") { td =>
      db.queryF(FlywaySchemaHistoryRowQueries.GetByInstalledOnSeq(installedOnSeq))(td)
    }
  }

  def countByInstalledRank(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = traceF("count.by.installedRank") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.CountByInstalledRank(installedRank))(td)
  }
  def getByInstalledRank(creds: Credentials, installedRank: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.installedRank") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.GetByInstalledRank(installedRank, orderBys, limit, offset))(td)
  }
  def getByInstalledRankSeq(creds: Credentials, installedRankSeq: Seq[Long])(implicit trace: TraceData) = if (installedRankSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.installedRank.seq") { td =>
      db.queryF(FlywaySchemaHistoryRowQueries.GetByInstalledRankSeq(installedRankSeq))(td)
    }
  }

  def countBySuccess(creds: Credentials, success: Boolean)(implicit trace: TraceData) = traceF("count.by.success") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.CountBySuccess(success))(td)
  }
  def getBySuccess(creds: Credentials, success: Boolean, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.success") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.GetBySuccess(success, orderBys, limit, offset))(td)
  }
  def getBySuccessSeq(creds: Credentials, successSeq: Seq[Boolean])(implicit trace: TraceData) = if (successSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.success.seq") { td =>
      db.queryF(FlywaySchemaHistoryRowQueries.GetBySuccessSeq(successSeq))(td)
    }
  }

  def countByTyp(creds: Credentials, typ: String)(implicit trace: TraceData) = traceF("count.by.typ") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.CountByTyp(typ))(td)
  }
  def getByTyp(creds: Credentials, typ: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.typ") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.GetByTyp(typ, orderBys, limit, offset))(td)
  }
  def getByTypSeq(creds: Credentials, typSeq: Seq[String])(implicit trace: TraceData) = if (typSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.typ.seq") { td =>
      db.queryF(FlywaySchemaHistoryRowQueries.GetByTypSeq(typSeq))(td)
    }
  }

  def countByVersion(creds: Credentials, version: String)(implicit trace: TraceData) = traceF("count.by.version") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.CountByVersion(version))(td)
  }
  def getByVersion(creds: Credentials, version: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.version") { td =>
    db.queryF(FlywaySchemaHistoryRowQueries.GetByVersion(version, orderBys, limit, offset))(td)
  }
  def getByVersionSeq(creds: Credentials, versionSeq: Seq[String])(implicit trace: TraceData) = if (versionSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.version.seq") { td =>
      db.queryF(FlywaySchemaHistoryRowQueries.GetByVersionSeq(versionSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: FlywaySchemaHistoryRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(FlywaySchemaHistoryRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.installedRank)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Flyway Schema History.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[FlywaySchemaHistoryRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(FlywaySchemaHistoryRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(FlywaySchemaHistoryRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "installedRank").toLong)
    }
  }

  def remove(creds: Credentials, installedRank: Long)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, installedRank)(td).flatMap {
      case Some(current) =>
        db.executeF(FlywaySchemaHistoryRowQueries.removeByPrimaryKey(installedRank))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find FlywaySchemaHistoryRow matching [$installedRank]")
    })
  }

  def update(creds: Credentials, installedRank: Long, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, installedRank)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Flyway Schema History [$installedRank]")
      case Some(_) => db.executeF(FlywaySchemaHistoryRowQueries.update(installedRank, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, installedRank)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Flyway Schema History [$installedRank]"
          case None => throw new IllegalStateException(s"Cannot find FlywaySchemaHistoryRow matching [$installedRank]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find FlywaySchemaHistoryRow matching [$installedRank]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[FlywaySchemaHistoryRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, FlywaySchemaHistoryRowQueries.fields)(td))
  }
}
