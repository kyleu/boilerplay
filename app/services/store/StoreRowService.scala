/* Generated File */
package services.store

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.ZonedDateTime
import models.queries.store.StoreRowQueries
import models.store.StoreRow
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class StoreRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[StoreRow]("storeRow") {
  def getByPrimaryKey(creds: Credentials, storeId: Int)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(StoreRowQueries.getByPrimaryKey(storeId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, storeId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, storeId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load storeRow with storeId [$storeId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, storeIdSeq: Seq[Int])(implicit trace: TraceData) = if (storeIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(StoreRowQueries.getByPrimaryKeySeq(storeIdSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(StoreRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(StoreRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(StoreRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(StoreRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(StoreRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAddressId(creds: Credentials, addressId: Int)(implicit trace: TraceData) = traceF("count.by.addressId") { td =>
    db.queryF(StoreRowQueries.CountByAddressId(addressId))(td)
  }
  def getByAddressId(creds: Credentials, addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.addressId") { td =>
    db.queryF(StoreRowQueries.GetByAddressId(addressId, orderBys, limit, offset))(td)
  }
  def getByAddressIdSeq(creds: Credentials, addressIdSeq: Seq[Int])(implicit trace: TraceData) = if (addressIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.addressId.seq") { td =>
      db.queryF(StoreRowQueries.GetByAddressIdSeq(addressIdSeq))(td)
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = traceF("count.by.lastUpdate") { td =>
    db.queryF(StoreRowQueries.CountByLastUpdate(lastUpdate))(td)
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.lastUpdate") { td =>
    db.queryF(StoreRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td)
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = if (lastUpdateSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.lastUpdate.seq") { td =>
      db.queryF(StoreRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
    }
  }

  def countByManagerStaffId(creds: Credentials, managerStaffId: Int)(implicit trace: TraceData) = traceF("count.by.managerStaffId") { td =>
    db.queryF(StoreRowQueries.CountByManagerStaffId(managerStaffId))(td)
  }
  def getByManagerStaffId(creds: Credentials, managerStaffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.managerStaffId") { td =>
    db.queryF(StoreRowQueries.GetByManagerStaffId(managerStaffId, orderBys, limit, offset))(td)
  }
  def getByManagerStaffIdSeq(creds: Credentials, managerStaffIdSeq: Seq[Int])(implicit trace: TraceData) = if (managerStaffIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.managerStaffId.seq") { td =>
      db.queryF(StoreRowQueries.GetByManagerStaffIdSeq(managerStaffIdSeq))(td)
    }
  }

  def countByStoreId(creds: Credentials, storeId: Int)(implicit trace: TraceData) = traceF("count.by.storeId") { td =>
    db.queryF(StoreRowQueries.CountByStoreId(storeId))(td)
  }
  def getByStoreId(creds: Credentials, storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.storeId") { td =>
    db.queryF(StoreRowQueries.GetByStoreId(storeId, orderBys, limit, offset))(td)
  }
  def getByStoreIdSeq(creds: Credentials, storeIdSeq: Seq[Int])(implicit trace: TraceData) = if (storeIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.storeId.seq") { td =>
      db.queryF(StoreRowQueries.GetByStoreIdSeq(storeIdSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: StoreRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(StoreRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.storeId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Store.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[StoreRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(StoreRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(StoreRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "storeId").toInt)
    }
  }

  def remove(creds: Credentials, storeId: Int)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, storeId)(td).flatMap {
      case Some(current) =>
        db.executeF(StoreRowQueries.removeByPrimaryKey(storeId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find StoreRow matching [$storeId]")
    })
  }

  def update(creds: Credentials, storeId: Int, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, storeId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Store [$storeId]")
      case Some(_) => db.executeF(StoreRowQueries.update(storeId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "storeId").flatMap(_.v).map(s => s.toInt).getOrElse(storeId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Store [$storeId]"
          case None => throw new IllegalStateException(s"Cannot find StoreRow matching [$storeId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find StoreRow matching [$storeId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[StoreRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, StoreRowQueries.fields)(td))
  }
}
