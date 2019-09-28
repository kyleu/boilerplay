/* Generated File */
package services.store

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.ModelServiceHelper
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.{Credentials, CsvUtils}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.sql.Connection
import java.time.ZonedDateTime
import models.queries.store.StoreRowQueries
import models.store.StoreRow
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class StoreRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[StoreRow]("storeRow", "store" -> "StoreRow") {
  def getByPrimaryKey(creds: Credentials, storeId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(StoreRowQueries.getByPrimaryKey(storeId), conn)(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, storeId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = getByPrimaryKey(creds, storeId, conn).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load storeRow with storeId [$storeId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, storeIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (storeIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(StoreRowQueries.getByPrimaryKeySeq(storeIdSeq), conn)(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(StoreRowQueries.countAll(filters), conn)(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(StoreRowQueries.getAll(filters, orderBys, limit, offset), conn)(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(StoreRowQueries.searchCount(q, filters), conn)(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(StoreRowQueries.search(q, filters, orderBys, limit, offset), conn)(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(StoreRowQueries.searchExact(q, orderBys, limit, offset), conn)(td))
  }

  def countByAddressId(creds: Credentials, addressId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.addressId")(td => db.queryF(StoreRowQueries.CountByAddressId(addressId), conn)(td))
  }
  def getByAddressId(creds: Credentials, addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.addressId")(td => db.queryF(StoreRowQueries.GetByAddressId(addressId, orderBys, limit, offset), conn)(td))
  }
  def getByAddressIdSeq(creds: Credentials, addressIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (addressIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.addressId.seq") { td =>
        db.queryF(StoreRowQueries.GetByAddressIdSeq(addressIdSeq), conn)(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(StoreRowQueries.CountByLastUpdate(lastUpdate), conn)(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(StoreRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset), conn)(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(StoreRowQueries.GetByLastUpdateSeq(lastUpdateSeq), conn)(td)
      }
    }
  }

  def countByManagerStaffId(creds: Credentials, managerStaffId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.managerStaffId")(td => db.queryF(StoreRowQueries.CountByManagerStaffId(managerStaffId), conn)(td))
  }
  def getByManagerStaffId(creds: Credentials, managerStaffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.managerStaffId")(td => db.queryF(StoreRowQueries.GetByManagerStaffId(managerStaffId, orderBys, limit, offset), conn)(td))
  }
  def getByManagerStaffIdSeq(creds: Credentials, managerStaffIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (managerStaffIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.managerStaffId.seq") { td =>
        db.queryF(StoreRowQueries.GetByManagerStaffIdSeq(managerStaffIdSeq), conn)(td)
      }
    }
  }

  def countByStoreId(creds: Credentials, storeId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.storeId")(td => db.queryF(StoreRowQueries.CountByStoreId(storeId), conn)(td))
  }
  def getByStoreId(creds: Credentials, storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.storeId")(td => db.queryF(StoreRowQueries.GetByStoreId(storeId, orderBys, limit, offset), conn)(td))
  }
  def getByStoreIdSeq(creds: Credentials, storeIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (storeIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.storeId.seq") { td =>
        db.queryF(StoreRowQueries.GetByStoreIdSeq(storeIdSeq), conn)(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: StoreRow, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(StoreRowQueries.insert(model), conn)(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.storeId, conn)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Store")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[StoreRow], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(StoreRowQueries.insertBatch(models), conn)(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(StoreRowQueries.create(fields), conn)(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "storeId").toInt, conn)
    })
  }

  def remove(creds: Credentials, storeId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, storeId, conn)(td).flatMap {
      case Some(current) =>
        db.executeF(StoreRowQueries.removeByPrimaryKey(storeId), conn)(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find StoreRow matching [$storeId]")
    })
  }

  def update(creds: Credentials, storeId: Int, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, storeId, conn)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Store [$storeId]")
      case Some(_) => db.executeF(StoreRowQueries.update(storeId, fields), conn)(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "storeId").flatMap(_.v).map(s => s.toInt).getOrElse(storeId), conn)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Store [$storeId]"
          case None => throw new IllegalStateException(s"Cannot find StoreRow matching [$storeId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find StoreRow matching [$storeId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Int], fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    Future.sequence(pks.map(pk => update(creds, pk, fields, conn))).map { x =>
      s"Updated [${fields.size}] fields for [${x.size} of ${pks.size}] StoreRow"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[StoreRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, StoreRowQueries.fields)(td))
  }
}
