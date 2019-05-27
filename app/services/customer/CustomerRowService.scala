/* Generated File */
package services.customer

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import models.customer.CustomerRow
import models.queries.customer.CustomerRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class CustomerRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[CustomerRow]("customerRow") {
  def getByPrimaryKey(creds: Credentials, customerId: Int)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(CustomerRowQueries.getByPrimaryKey(customerId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, customerId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, customerId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load customerRow with customerId [$customerId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, customerIdSeq: Seq[Int])(implicit trace: TraceData) = if (customerIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(CustomerRowQueries.getByPrimaryKeySeq(customerIdSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(CustomerRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(CustomerRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(CustomerRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(CustomerRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(CustomerRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAddressId(creds: Credentials, addressId: Int)(implicit trace: TraceData) = traceF("count.by.addressId") { td =>
    db.queryF(CustomerRowQueries.CountByAddressId(addressId))(td)
  }
  def getByAddressId(creds: Credentials, addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.addressId") { td =>
    db.queryF(CustomerRowQueries.GetByAddressId(addressId, orderBys, limit, offset))(td)
  }
  def getByAddressIdSeq(creds: Credentials, addressIdSeq: Seq[Int])(implicit trace: TraceData) = if (addressIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.addressId.seq") { td =>
      db.queryF(CustomerRowQueries.GetByAddressIdSeq(addressIdSeq))(td)
    }
  }

  def countByCustomerId(creds: Credentials, customerId: Int)(implicit trace: TraceData) = traceF("count.by.customerId") { td =>
    db.queryF(CustomerRowQueries.CountByCustomerId(customerId))(td)
  }
  def getByCustomerId(creds: Credentials, customerId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.customerId") { td =>
    db.queryF(CustomerRowQueries.GetByCustomerId(customerId, orderBys, limit, offset))(td)
  }
  def getByCustomerIdSeq(creds: Credentials, customerIdSeq: Seq[Int])(implicit trace: TraceData) = if (customerIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.customerId.seq") { td =>
      db.queryF(CustomerRowQueries.GetByCustomerIdSeq(customerIdSeq))(td)
    }
  }

  def countByEmail(creds: Credentials, email: String)(implicit trace: TraceData) = traceF("count.by.email") { td =>
    db.queryF(CustomerRowQueries.CountByEmail(email))(td)
  }
  def getByEmail(creds: Credentials, email: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.email") { td =>
    db.queryF(CustomerRowQueries.GetByEmail(email, orderBys, limit, offset))(td)
  }
  def getByEmailSeq(creds: Credentials, emailSeq: Seq[String])(implicit trace: TraceData) = if (emailSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.email.seq") { td =>
      db.queryF(CustomerRowQueries.GetByEmailSeq(emailSeq))(td)
    }
  }

  def countByFirstName(creds: Credentials, firstName: String)(implicit trace: TraceData) = traceF("count.by.firstName") { td =>
    db.queryF(CustomerRowQueries.CountByFirstName(firstName))(td)
  }
  def getByFirstName(creds: Credentials, firstName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.firstName") { td =>
    db.queryF(CustomerRowQueries.GetByFirstName(firstName, orderBys, limit, offset))(td)
  }
  def getByFirstNameSeq(creds: Credentials, firstNameSeq: Seq[String])(implicit trace: TraceData) = if (firstNameSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.firstName.seq") { td =>
      db.queryF(CustomerRowQueries.GetByFirstNameSeq(firstNameSeq))(td)
    }
  }

  def countByLastName(creds: Credentials, lastName: String)(implicit trace: TraceData) = traceF("count.by.lastName") { td =>
    db.queryF(CustomerRowQueries.CountByLastName(lastName))(td)
  }
  def getByLastName(creds: Credentials, lastName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.lastName") { td =>
    db.queryF(CustomerRowQueries.GetByLastName(lastName, orderBys, limit, offset))(td)
  }
  def getByLastNameSeq(creds: Credentials, lastNameSeq: Seq[String])(implicit trace: TraceData) = if (lastNameSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.lastName.seq") { td =>
      db.queryF(CustomerRowQueries.GetByLastNameSeq(lastNameSeq))(td)
    }
  }

  def countByStoreId(creds: Credentials, storeId: Int)(implicit trace: TraceData) = traceF("count.by.storeId") { td =>
    db.queryF(CustomerRowQueries.CountByStoreId(storeId))(td)
  }
  def getByStoreId(creds: Credentials, storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.storeId") { td =>
    db.queryF(CustomerRowQueries.GetByStoreId(storeId, orderBys, limit, offset))(td)
  }
  def getByStoreIdSeq(creds: Credentials, storeIdSeq: Seq[Int])(implicit trace: TraceData) = if (storeIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.storeId.seq") { td =>
      db.queryF(CustomerRowQueries.GetByStoreIdSeq(storeIdSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: CustomerRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(CustomerRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.customerId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Customer.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[CustomerRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(CustomerRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(CustomerRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "customerId").toInt)
    }
  }

  def remove(creds: Credentials, customerId: Int)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, customerId)(td).flatMap {
      case Some(current) =>
        db.executeF(CustomerRowQueries.removeByPrimaryKey(customerId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find CustomerRow matching [$customerId]")
    })
  }

  def update(creds: Credentials, customerId: Int, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, customerId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Customer [$customerId]")
      case Some(_) => db.executeF(CustomerRowQueries.update(customerId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "customerId").flatMap(_.v).map(s => s.toInt).getOrElse(customerId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Customer [$customerId]"
          case None => throw new IllegalStateException(s"Cannot find CustomerRow matching [$customerId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find CustomerRow matching [$customerId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[CustomerRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, CustomerRowQueries.fields)(td))
  }
}
