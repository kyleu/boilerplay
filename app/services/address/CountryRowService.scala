/* Generated File */
package services.address

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.ModelServiceHelper
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.{Credentials, CsvUtils}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.sql.Connection
import java.time.ZonedDateTime
import models.address.CountryRow
import models.queries.address.CountryRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class CountryRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[CountryRow]("countryRow", "address" -> "CountryRow") {
  def getByPrimaryKey(creds: Credentials, countryId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(CountryRowQueries.getByPrimaryKey(countryId), conn)(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, countryId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = getByPrimaryKey(creds, countryId, conn).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load countryRow with countryId [$countryId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, countryIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (countryIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(CountryRowQueries.getByPrimaryKeySeq(countryIdSeq), conn)(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(CountryRowQueries.countAll(filters), conn)(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(CountryRowQueries.getAll(filters, orderBys, limit, offset), conn)(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(CountryRowQueries.searchCount(q, filters), conn)(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(CountryRowQueries.search(q, filters, orderBys, limit, offset), conn)(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(CountryRowQueries.searchExact(q, orderBys, limit, offset), conn)(td))
  }

  def countByCountry(creds: Credentials, country: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.country")(td => db.queryF(CountryRowQueries.CountByCountry(country), conn)(td))
  }
  def getByCountry(creds: Credentials, country: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.country")(td => db.queryF(CountryRowQueries.GetByCountry(country, orderBys, limit, offset), conn)(td))
  }
  def getByCountrySeq(creds: Credentials, countrySeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (countrySeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.country.seq") { td =>
        db.queryF(CountryRowQueries.GetByCountrySeq(countrySeq), conn)(td)
      }
    }
  }

  def countByCountryId(creds: Credentials, countryId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.countryId")(td => db.queryF(CountryRowQueries.CountByCountryId(countryId), conn)(td))
  }
  def getByCountryId(creds: Credentials, countryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.countryId")(td => db.queryF(CountryRowQueries.GetByCountryId(countryId, orderBys, limit, offset), conn)(td))
  }
  def getByCountryIdSeq(creds: Credentials, countryIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (countryIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.countryId.seq") { td =>
        db.queryF(CountryRowQueries.GetByCountryIdSeq(countryIdSeq), conn)(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(CountryRowQueries.CountByLastUpdate(lastUpdate), conn)(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(CountryRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset), conn)(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(CountryRowQueries.GetByLastUpdateSeq(lastUpdateSeq), conn)(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: CountryRow, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(CountryRowQueries.insert(model), conn)(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.countryId, conn)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Country")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[CountryRow], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(CountryRowQueries.insertBatch(models), conn)(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(CountryRowQueries.create(fields), conn)(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "countryId").toInt, conn)
    })
  }

  def remove(creds: Credentials, countryId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, countryId, conn)(td).flatMap {
      case Some(current) =>
        db.executeF(CountryRowQueries.removeByPrimaryKey(countryId), conn)(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find CountryRow matching [$countryId]")
    })
  }

  def update(creds: Credentials, countryId: Int, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, countryId, conn)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Country [$countryId]")
      case Some(_) => db.executeF(CountryRowQueries.update(countryId, fields), conn)(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "countryId").flatMap(_.v).map(s => s.toInt).getOrElse(countryId), conn)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Country [$countryId]"
          case None => throw new IllegalStateException(s"Cannot find CountryRow matching [$countryId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find CountryRow matching [$countryId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Int], fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    Future.sequence(pks.map(pk => update(creds, pk, fields, conn))).map { x =>
      s"Updated [${fields.size}] fields for [${x.size} of ${pks.size}] CountryRow"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[CountryRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, CountryRowQueries.fields)(td))
  }
}
