/* Generated File */
package services.store

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import models.queries.store.InventoryRowQueries
import models.store.InventoryRow
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class InventoryRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[InventoryRow]("inventoryRow") {
  def getByPrimaryKey(creds: Credentials, inventoryId: Long)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(InventoryRowQueries.getByPrimaryKey(inventoryId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, inventoryId: Long)(implicit trace: TraceData) = getByPrimaryKey(creds, inventoryId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load inventoryRow with inventoryId [$inventoryId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, inventoryIdSeq: Seq[Long])(implicit trace: TraceData) = if (inventoryIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(InventoryRowQueries.getByPrimaryKeySeq(inventoryIdSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(InventoryRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(InventoryRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(InventoryRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(InventoryRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(InventoryRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByFilmId(creds: Credentials, filmId: Int)(implicit trace: TraceData) = traceF("count.by.filmId") { td =>
    db.queryF(InventoryRowQueries.CountByFilmId(filmId))(td)
  }
  def getByFilmId(creds: Credentials, filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.filmId") { td =>
    db.queryF(InventoryRowQueries.GetByFilmId(filmId, orderBys, limit, offset))(td)
  }
  def getByFilmIdSeq(creds: Credentials, filmIdSeq: Seq[Int])(implicit trace: TraceData) = if (filmIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.filmId.seq") { td =>
      db.queryF(InventoryRowQueries.GetByFilmIdSeq(filmIdSeq))(td)
    }
  }

  def countByInventoryId(creds: Credentials, inventoryId: Long)(implicit trace: TraceData) = traceF("count.by.inventoryId") { td =>
    db.queryF(InventoryRowQueries.CountByInventoryId(inventoryId))(td)
  }
  def getByInventoryId(creds: Credentials, inventoryId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.inventoryId") { td =>
    db.queryF(InventoryRowQueries.GetByInventoryId(inventoryId, orderBys, limit, offset))(td)
  }
  def getByInventoryIdSeq(creds: Credentials, inventoryIdSeq: Seq[Long])(implicit trace: TraceData) = if (inventoryIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.inventoryId.seq") { td =>
      db.queryF(InventoryRowQueries.GetByInventoryIdSeq(inventoryIdSeq))(td)
    }
  }

  def countByStoreId(creds: Credentials, storeId: Int)(implicit trace: TraceData) = traceF("count.by.storeId") { td =>
    db.queryF(InventoryRowQueries.CountByStoreId(storeId))(td)
  }
  def getByStoreId(creds: Credentials, storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.storeId") { td =>
    db.queryF(InventoryRowQueries.GetByStoreId(storeId, orderBys, limit, offset))(td)
  }
  def getByStoreIdSeq(creds: Credentials, storeIdSeq: Seq[Int])(implicit trace: TraceData) = if (storeIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.storeId.seq") { td =>
      db.queryF(InventoryRowQueries.GetByStoreIdSeq(storeIdSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: InventoryRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(InventoryRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.inventoryId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Inventory.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[InventoryRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(InventoryRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(InventoryRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "inventoryId").toLong)
    }
  }

  def remove(creds: Credentials, inventoryId: Long)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, inventoryId)(td).flatMap {
      case Some(current) =>
        db.executeF(InventoryRowQueries.removeByPrimaryKey(inventoryId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find InventoryRow matching [$inventoryId]")
    })
  }

  def update(creds: Credentials, inventoryId: Long, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, inventoryId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Inventory [$inventoryId]")
      case Some(_) => db.executeF(InventoryRowQueries.update(inventoryId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "inventoryId").flatMap(_.v).map(s => s.toLong).getOrElse(inventoryId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Inventory [$inventoryId]"
          case None => throw new IllegalStateException(s"Cannot find InventoryRow matching [$inventoryId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find InventoryRow matching [$inventoryId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[InventoryRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, InventoryRowQueries.fields)(td))
  }
}
