/* Generated File */
package services.address

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.ZonedDateTime
import models.address.CountryRow
import models.queries.address.CountryRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class CountryRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[CountryRow]("countryRow") {
  def getByPrimaryKey(creds: Credentials, countryId: Int)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(CountryRowQueries.getByPrimaryKey(countryId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, countryId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, countryId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load countryRow with countryId [$countryId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, countryIdSeq: Seq[Int])(implicit trace: TraceData) = if (countryIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(CountryRowQueries.getByPrimaryKeySeq(countryIdSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(CountryRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(CountryRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(CountryRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(CountryRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(CountryRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByCountry(creds: Credentials, country: String)(implicit trace: TraceData) = traceF("count.by.country") { td =>
    db.queryF(CountryRowQueries.CountByCountry(country))(td)
  }
  def getByCountry(creds: Credentials, country: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.country") { td =>
    db.queryF(CountryRowQueries.GetByCountry(country, orderBys, limit, offset))(td)
  }
  def getByCountrySeq(creds: Credentials, countrySeq: Seq[String])(implicit trace: TraceData) = if (countrySeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.country.seq") { td =>
      db.queryF(CountryRowQueries.GetByCountrySeq(countrySeq))(td)
    }
  }

  def countByCountryId(creds: Credentials, countryId: Int)(implicit trace: TraceData) = traceF("count.by.countryId") { td =>
    db.queryF(CountryRowQueries.CountByCountryId(countryId))(td)
  }
  def getByCountryId(creds: Credentials, countryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.countryId") { td =>
    db.queryF(CountryRowQueries.GetByCountryId(countryId, orderBys, limit, offset))(td)
  }
  def getByCountryIdSeq(creds: Credentials, countryIdSeq: Seq[Int])(implicit trace: TraceData) = if (countryIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.countryId.seq") { td =>
      db.queryF(CountryRowQueries.GetByCountryIdSeq(countryIdSeq))(td)
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = traceF("count.by.lastUpdate") { td =>
    db.queryF(CountryRowQueries.CountByLastUpdate(lastUpdate))(td)
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.lastUpdate") { td =>
    db.queryF(CountryRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td)
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = if (lastUpdateSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.lastUpdate.seq") { td =>
      db.queryF(CountryRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: CountryRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(CountryRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.countryId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Country.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[CountryRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(CountryRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(CountryRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "countryId").toInt)
    }
  }

  def remove(creds: Credentials, countryId: Int)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, countryId)(td).flatMap {
      case Some(current) =>
        db.executeF(CountryRowQueries.removeByPrimaryKey(countryId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find CountryRow matching [$countryId]")
    })
  }

  def update(creds: Credentials, countryId: Int, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, countryId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Country [$countryId]")
      case Some(_) => db.executeF(CountryRowQueries.update(countryId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "countryId").flatMap(_.v).map(s => s.toInt).getOrElse(countryId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Country [$countryId]"
          case None => throw new IllegalStateException(s"Cannot find CountryRow matching [$countryId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find CountryRow matching [$countryId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[CountryRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, CountryRowQueries.fields)(td))
  }
}
