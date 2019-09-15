/* Generated File */
package services.payment

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.ModelServiceHelper
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.{Credentials, CsvUtils}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import models.payment.PaymentRow
import models.queries.payment.PaymentRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class PaymentRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[PaymentRow]("paymentRow", "payment" -> "PaymentRow") {
  def getByPrimaryKey(creds: Credentials, paymentId: Long)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(PaymentRowQueries.getByPrimaryKey(paymentId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, paymentId: Long)(implicit trace: TraceData) = getByPrimaryKey(creds, paymentId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load paymentRow with paymentId [$paymentId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, paymentIdSeq: Seq[Long])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (paymentIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(PaymentRowQueries.getByPrimaryKeySeq(paymentIdSeq))(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(PaymentRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(PaymentRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(PaymentRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(PaymentRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(PaymentRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAmount(creds: Credentials, amount: BigDecimal)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.amount")(td => db.queryF(PaymentRowQueries.CountByAmount(amount))(td))
  }
  def getByAmount(creds: Credentials, amount: BigDecimal, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.amount")(td => db.queryF(PaymentRowQueries.GetByAmount(amount, orderBys, limit, offset))(td))
  }
  def getByAmountSeq(creds: Credentials, amountSeq: Seq[BigDecimal])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (amountSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.amount.seq") { td =>
        db.queryF(PaymentRowQueries.GetByAmountSeq(amountSeq))(td)
      }
    }
  }

  def countByCustomerId(creds: Credentials, customerId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.customerId")(td => db.queryF(PaymentRowQueries.CountByCustomerId(customerId))(td))
  }
  def getByCustomerId(creds: Credentials, customerId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.customerId")(td => db.queryF(PaymentRowQueries.GetByCustomerId(customerId, orderBys, limit, offset))(td))
  }
  def getByCustomerIdSeq(creds: Credentials, customerIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (customerIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.customerId.seq") { td =>
        db.queryF(PaymentRowQueries.GetByCustomerIdSeq(customerIdSeq))(td)
      }
    }
  }

  def countByPaymentId(creds: Credentials, paymentId: Long)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.paymentId")(td => db.queryF(PaymentRowQueries.CountByPaymentId(paymentId))(td))
  }
  def getByPaymentId(creds: Credentials, paymentId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.paymentId")(td => db.queryF(PaymentRowQueries.GetByPaymentId(paymentId, orderBys, limit, offset))(td))
  }
  def getByPaymentIdSeq(creds: Credentials, paymentIdSeq: Seq[Long])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (paymentIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.paymentId.seq") { td =>
        db.queryF(PaymentRowQueries.GetByPaymentIdSeq(paymentIdSeq))(td)
      }
    }
  }

  def countByRentalId(creds: Credentials, rentalId: Long)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.rentalId")(td => db.queryF(PaymentRowQueries.CountByRentalId(rentalId))(td))
  }
  def getByRentalId(creds: Credentials, rentalId: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.rentalId")(td => db.queryF(PaymentRowQueries.GetByRentalId(rentalId, orderBys, limit, offset))(td))
  }
  def getByRentalIdSeq(creds: Credentials, rentalIdSeq: Seq[Long])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (rentalIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.rentalId.seq") { td =>
        db.queryF(PaymentRowQueries.GetByRentalIdSeq(rentalIdSeq))(td)
      }
    }
  }

  def countByStaffId(creds: Credentials, staffId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.staffId")(td => db.queryF(PaymentRowQueries.CountByStaffId(staffId))(td))
  }
  def getByStaffId(creds: Credentials, staffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.staffId")(td => db.queryF(PaymentRowQueries.GetByStaffId(staffId, orderBys, limit, offset))(td))
  }
  def getByStaffIdSeq(creds: Credentials, staffIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (staffIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.staffId.seq") { td =>
        db.queryF(PaymentRowQueries.GetByStaffIdSeq(staffIdSeq))(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: PaymentRow)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(PaymentRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.paymentId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Payment")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[PaymentRow])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(PaymentRowQueries.insertBatch(models))(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(PaymentRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "paymentId").toLong)
    })
  }

  def remove(creds: Credentials, paymentId: Long)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, paymentId)(td).flatMap {
      case Some(current) =>
        db.executeF(PaymentRowQueries.removeByPrimaryKey(paymentId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find PaymentRow matching [$paymentId]")
    })
  }

  def update(creds: Credentials, paymentId: Long, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, paymentId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Payment [$paymentId]")
      case Some(_) => db.executeF(PaymentRowQueries.update(paymentId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "paymentId").flatMap(_.v).map(s => s.toLong).getOrElse(paymentId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Payment [$paymentId]"
          case None => throw new IllegalStateException(s"Cannot find PaymentRow matching [$paymentId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find PaymentRow matching [$paymentId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Seq[Any]], fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    db.executeF(PaymentRowQueries.updateBulk(pks, fields))(trace).map { x =>
      s"Updated [${fields.size}] fields for [$x of ${pks.size}] Payments"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[PaymentRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, PaymentRowQueries.fields)(td))
  }
}
