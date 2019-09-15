/* Generated File */
package services.customer

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.ModelServiceHelper
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.{Credentials, CsvUtils}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.ZonedDateTime
import models.customer.RentalRow
import models.queries.customer.RentalRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class RentalRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[RentalRow]("rentalRow", "customer" -> "RentalRow") {
  def getByPrimaryKey(creds: Credentials, rentalId: Long)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(RentalRowQueries.getByPrimaryKey(rentalId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, rentalId: Long)(implicit trace: TraceData) = getByPrimaryKey(creds, rentalId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load rentalRow with rentalId [$rentalId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, rentalIdSeq: Seq[Long])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (rentalIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(RentalRowQueries.getByPrimaryKeySeq(rentalIdSeq))(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(RentalRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(RentalRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(RentalRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(RentalRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(RentalRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByCustomerId(creds: Credentials, customerId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.customerId")(td => db.queryF(RentalRowQueries.CountByCustomerId(customerId))(td))
  }
  def getByCustomerId(creds: Credentials, customerId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.customerId")(td => db.queryF(RentalRowQueries.GetByCustomerId(customerId, orderBys, limit, offset))(td))
  }
  def getByCustomerIdSeq(creds: Credentials, customerIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (customerIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.customerId.seq") { td =>
        db.queryF(RentalRowQueries.GetByCustomerIdSeq(customerIdSeq))(td)
      }
    }
  }

  def countByInventoryId(creds: Credentials, inventoryId: Long)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.inventoryId")(td => db.queryF(RentalRowQueries.CountByInventoryId(inventoryId))(td))
  }
  def getByInventoryId(creds: Credentials, inventoryId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.inventoryId")(td => db.queryF(RentalRowQueries.GetByInventoryId(inventoryId, orderBys, limit, offset))(td))
  }
  def getByInventoryIdSeq(creds: Credentials, inventoryIdSeq: Seq[Long])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (inventoryIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.inventoryId.seq") { td =>
        db.queryF(RentalRowQueries.GetByInventoryIdSeq(inventoryIdSeq))(td)
      }
    }
  }

  def countByRentalDate(creds: Credentials, rentalDate: ZonedDateTime)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.rentalDate")(td => db.queryF(RentalRowQueries.CountByRentalDate(rentalDate))(td))
  }
  def getByRentalDate(creds: Credentials, rentalDate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.rentalDate")(td => db.queryF(RentalRowQueries.GetByRentalDate(rentalDate, orderBys, limit, offset))(td))
  }
  def getByRentalDateSeq(creds: Credentials, rentalDateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (rentalDateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.rentalDate.seq") { td =>
        db.queryF(RentalRowQueries.GetByRentalDateSeq(rentalDateSeq))(td)
      }
    }
  }

  def countByRentalId(creds: Credentials, rentalId: Long)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.rentalId")(td => db.queryF(RentalRowQueries.CountByRentalId(rentalId))(td))
  }
  def getByRentalId(creds: Credentials, rentalId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.rentalId")(td => db.queryF(RentalRowQueries.GetByRentalId(rentalId, orderBys, limit, offset))(td))
  }
  def getByRentalIdSeq(creds: Credentials, rentalIdSeq: Seq[Long])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (rentalIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.rentalId.seq") { td =>
        db.queryF(RentalRowQueries.GetByRentalIdSeq(rentalIdSeq))(td)
      }
    }
  }

  def countByReturnDate(creds: Credentials, returnDate: ZonedDateTime)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.returnDate")(td => db.queryF(RentalRowQueries.CountByReturnDate(returnDate))(td))
  }
  def getByReturnDate(creds: Credentials, returnDate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.returnDate")(td => db.queryF(RentalRowQueries.GetByReturnDate(returnDate, orderBys, limit, offset))(td))
  }
  def getByReturnDateSeq(creds: Credentials, returnDateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (returnDateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.returnDate.seq") { td =>
        db.queryF(RentalRowQueries.GetByReturnDateSeq(returnDateSeq))(td)
      }
    }
  }

  def countByStaffId(creds: Credentials, staffId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.staffId")(td => db.queryF(RentalRowQueries.CountByStaffId(staffId))(td))
  }
  def getByStaffId(creds: Credentials, staffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.staffId")(td => db.queryF(RentalRowQueries.GetByStaffId(staffId, orderBys, limit, offset))(td))
  }
  def getByStaffIdSeq(creds: Credentials, staffIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (staffIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.staffId.seq") { td =>
        db.queryF(RentalRowQueries.GetByStaffIdSeq(staffIdSeq))(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: RentalRow)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(RentalRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.rentalId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Rental")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[RentalRow])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(RentalRowQueries.insertBatch(models))(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(RentalRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "rentalId").toLong)
    })
  }

  def remove(creds: Credentials, rentalId: Long)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, rentalId)(td).flatMap {
      case Some(current) =>
        db.executeF(RentalRowQueries.removeByPrimaryKey(rentalId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find RentalRow matching [$rentalId]")
    })
  }

  def update(creds: Credentials, rentalId: Long, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, rentalId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Rental [$rentalId]")
      case Some(_) => db.executeF(RentalRowQueries.update(rentalId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "rentalId").flatMap(_.v).map(s => s.toLong).getOrElse(rentalId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Rental [$rentalId]"
          case None => throw new IllegalStateException(s"Cannot find RentalRow matching [$rentalId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find RentalRow matching [$rentalId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Seq[Any]], fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    db.executeF(RentalRowQueries.updateBulk(pks, fields))(trace).map { x =>
      s"Updated [${fields.size}] fields for [$x of ${pks.size}] Rentals"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[RentalRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, RentalRowQueries.fields)(td))
  }
}
