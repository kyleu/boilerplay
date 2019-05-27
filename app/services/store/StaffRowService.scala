/* Generated File */
package services.store

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import models.queries.store.StaffRowQueries
import models.store.StaffRow
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class StaffRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[StaffRow]("staffRow") {
  def getByPrimaryKey(creds: Credentials, staffId: Int)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(StaffRowQueries.getByPrimaryKey(staffId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, staffId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, staffId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load staffRow with staffId [$staffId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, staffIdSeq: Seq[Int])(implicit trace: TraceData) = if (staffIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(StaffRowQueries.getByPrimaryKeySeq(staffIdSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(StaffRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(StaffRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(StaffRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(StaffRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(StaffRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByAddressId(creds: Credentials, addressId: Int)(implicit trace: TraceData) = traceF("count.by.addressId") { td =>
    db.queryF(StaffRowQueries.CountByAddressId(addressId))(td)
  }
  def getByAddressId(creds: Credentials, addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.addressId") { td =>
    db.queryF(StaffRowQueries.GetByAddressId(addressId, orderBys, limit, offset))(td)
  }
  def getByAddressIdSeq(creds: Credentials, addressIdSeq: Seq[Int])(implicit trace: TraceData) = if (addressIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.addressId.seq") { td =>
      db.queryF(StaffRowQueries.GetByAddressIdSeq(addressIdSeq))(td)
    }
  }

  def countByEmail(creds: Credentials, email: String)(implicit trace: TraceData) = traceF("count.by.email") { td =>
    db.queryF(StaffRowQueries.CountByEmail(email))(td)
  }
  def getByEmail(creds: Credentials, email: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.email") { td =>
    db.queryF(StaffRowQueries.GetByEmail(email, orderBys, limit, offset))(td)
  }
  def getByEmailSeq(creds: Credentials, emailSeq: Seq[String])(implicit trace: TraceData) = if (emailSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.email.seq") { td =>
      db.queryF(StaffRowQueries.GetByEmailSeq(emailSeq))(td)
    }
  }

  def countByFirstName(creds: Credentials, firstName: String)(implicit trace: TraceData) = traceF("count.by.firstName") { td =>
    db.queryF(StaffRowQueries.CountByFirstName(firstName))(td)
  }
  def getByFirstName(creds: Credentials, firstName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.firstName") { td =>
    db.queryF(StaffRowQueries.GetByFirstName(firstName, orderBys, limit, offset))(td)
  }
  def getByFirstNameSeq(creds: Credentials, firstNameSeq: Seq[String])(implicit trace: TraceData) = if (firstNameSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.firstName.seq") { td =>
      db.queryF(StaffRowQueries.GetByFirstNameSeq(firstNameSeq))(td)
    }
  }

  def countByLastName(creds: Credentials, lastName: String)(implicit trace: TraceData) = traceF("count.by.lastName") { td =>
    db.queryF(StaffRowQueries.CountByLastName(lastName))(td)
  }
  def getByLastName(creds: Credentials, lastName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.lastName") { td =>
    db.queryF(StaffRowQueries.GetByLastName(lastName, orderBys, limit, offset))(td)
  }
  def getByLastNameSeq(creds: Credentials, lastNameSeq: Seq[String])(implicit trace: TraceData) = if (lastNameSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.lastName.seq") { td =>
      db.queryF(StaffRowQueries.GetByLastNameSeq(lastNameSeq))(td)
    }
  }

  def countByStaffId(creds: Credentials, staffId: Int)(implicit trace: TraceData) = traceF("count.by.staffId") { td =>
    db.queryF(StaffRowQueries.CountByStaffId(staffId))(td)
  }
  def getByStaffId(creds: Credentials, staffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.staffId") { td =>
    db.queryF(StaffRowQueries.GetByStaffId(staffId, orderBys, limit, offset))(td)
  }
  def getByStaffIdSeq(creds: Credentials, staffIdSeq: Seq[Int])(implicit trace: TraceData) = if (staffIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.staffId.seq") { td =>
      db.queryF(StaffRowQueries.GetByStaffIdSeq(staffIdSeq))(td)
    }
  }

  def countByStoreId(creds: Credentials, storeId: Int)(implicit trace: TraceData) = traceF("count.by.storeId") { td =>
    db.queryF(StaffRowQueries.CountByStoreId(storeId))(td)
  }
  def getByStoreId(creds: Credentials, storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.storeId") { td =>
    db.queryF(StaffRowQueries.GetByStoreId(storeId, orderBys, limit, offset))(td)
  }
  def getByStoreIdSeq(creds: Credentials, storeIdSeq: Seq[Int])(implicit trace: TraceData) = if (storeIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.storeId.seq") { td =>
      db.queryF(StaffRowQueries.GetByStoreIdSeq(storeIdSeq))(td)
    }
  }

  def countByUsername(creds: Credentials, username: String)(implicit trace: TraceData) = traceF("count.by.username") { td =>
    db.queryF(StaffRowQueries.CountByUsername(username))(td)
  }
  def getByUsername(creds: Credentials, username: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.username") { td =>
    db.queryF(StaffRowQueries.GetByUsername(username, orderBys, limit, offset))(td)
  }
  def getByUsernameSeq(creds: Credentials, usernameSeq: Seq[String])(implicit trace: TraceData) = if (usernameSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.username.seq") { td =>
      db.queryF(StaffRowQueries.GetByUsernameSeq(usernameSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: StaffRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(StaffRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.staffId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Staff.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[StaffRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(StaffRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(StaffRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "staffId").toInt)
    }
  }

  def remove(creds: Credentials, staffId: Int)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, staffId)(td).flatMap {
      case Some(current) =>
        db.executeF(StaffRowQueries.removeByPrimaryKey(staffId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find StaffRow matching [$staffId]")
    })
  }

  def update(creds: Credentials, staffId: Int, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, staffId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Staff [$staffId]")
      case Some(_) => db.executeF(StaffRowQueries.update(staffId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "staffId").flatMap(_.v).map(s => s.toInt).getOrElse(staffId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Staff [$staffId]"
          case None => throw new IllegalStateException(s"Cannot find StaffRow matching [$staffId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find StaffRow matching [$staffId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[StaffRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, StaffRowQueries.fields)(td))
  }
}
