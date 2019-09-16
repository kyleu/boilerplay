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
import models.queries.store.StaffRowQueries
import models.store.StaffRow
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class StaffRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[StaffRow]("staffRow", "store" -> "StaffRow") {
  def getByPrimaryKey(creds: Credentials, staffId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(StaffRowQueries.getByPrimaryKey(staffId), conn)(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, staffId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = getByPrimaryKey(creds, staffId, conn).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load staffRow with staffId [$staffId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, staffIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (staffIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(StaffRowQueries.getByPrimaryKeySeq(staffIdSeq), conn)(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(StaffRowQueries.countAll(filters), conn)(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(StaffRowQueries.getAll(filters, orderBys, limit, offset), conn)(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(StaffRowQueries.searchCount(q, filters), conn)(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(StaffRowQueries.search(q, filters, orderBys, limit, offset), conn)(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(StaffRowQueries.searchExact(q, orderBys, limit, offset), conn)(td))
  }

  def countByAddressId(creds: Credentials, addressId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.addressId")(td => db.queryF(StaffRowQueries.CountByAddressId(addressId), conn)(td))
  }
  def getByAddressId(creds: Credentials, addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.addressId")(td => db.queryF(StaffRowQueries.GetByAddressId(addressId, orderBys, limit, offset), conn)(td))
  }
  def getByAddressIdSeq(creds: Credentials, addressIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (addressIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.addressId.seq") { td =>
        db.queryF(StaffRowQueries.GetByAddressIdSeq(addressIdSeq), conn)(td)
      }
    }
  }

  def countByEmail(creds: Credentials, email: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.email")(td => db.queryF(StaffRowQueries.CountByEmail(email), conn)(td))
  }
  def getByEmail(creds: Credentials, email: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.email")(td => db.queryF(StaffRowQueries.GetByEmail(email, orderBys, limit, offset), conn)(td))
  }
  def getByEmailSeq(creds: Credentials, emailSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (emailSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.email.seq") { td =>
        db.queryF(StaffRowQueries.GetByEmailSeq(emailSeq), conn)(td)
      }
    }
  }

  def countByFirstName(creds: Credentials, firstName: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.firstName")(td => db.queryF(StaffRowQueries.CountByFirstName(firstName), conn)(td))
  }
  def getByFirstName(creds: Credentials, firstName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.firstName")(td => db.queryF(StaffRowQueries.GetByFirstName(firstName, orderBys, limit, offset), conn)(td))
  }
  def getByFirstNameSeq(creds: Credentials, firstNameSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (firstNameSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.firstName.seq") { td =>
        db.queryF(StaffRowQueries.GetByFirstNameSeq(firstNameSeq), conn)(td)
      }
    }
  }

  def countByLastName(creds: Credentials, lastName: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastName")(td => db.queryF(StaffRowQueries.CountByLastName(lastName), conn)(td))
  }
  def getByLastName(creds: Credentials, lastName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastName")(td => db.queryF(StaffRowQueries.GetByLastName(lastName, orderBys, limit, offset), conn)(td))
  }
  def getByLastNameSeq(creds: Credentials, lastNameSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastNameSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastName.seq") { td =>
        db.queryF(StaffRowQueries.GetByLastNameSeq(lastNameSeq), conn)(td)
      }
    }
  }

  def countByStaffId(creds: Credentials, staffId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.staffId")(td => db.queryF(StaffRowQueries.CountByStaffId(staffId), conn)(td))
  }
  def getByStaffId(creds: Credentials, staffId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.staffId")(td => db.queryF(StaffRowQueries.GetByStaffId(staffId, orderBys, limit, offset), conn)(td))
  }
  def getByStaffIdSeq(creds: Credentials, staffIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (staffIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.staffId.seq") { td =>
        db.queryF(StaffRowQueries.GetByStaffIdSeq(staffIdSeq), conn)(td)
      }
    }
  }

  def countByStoreId(creds: Credentials, storeId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.storeId")(td => db.queryF(StaffRowQueries.CountByStoreId(storeId), conn)(td))
  }
  def getByStoreId(creds: Credentials, storeId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.storeId")(td => db.queryF(StaffRowQueries.GetByStoreId(storeId, orderBys, limit, offset), conn)(td))
  }
  def getByStoreIdSeq(creds: Credentials, storeIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (storeIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.storeId.seq") { td =>
        db.queryF(StaffRowQueries.GetByStoreIdSeq(storeIdSeq), conn)(td)
      }
    }
  }

  def countByUsername(creds: Credentials, username: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.username")(td => db.queryF(StaffRowQueries.CountByUsername(username), conn)(td))
  }
  def getByUsername(creds: Credentials, username: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.username")(td => db.queryF(StaffRowQueries.GetByUsername(username, orderBys, limit, offset), conn)(td))
  }
  def getByUsernameSeq(creds: Credentials, usernameSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (usernameSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.username.seq") { td =>
        db.queryF(StaffRowQueries.GetByUsernameSeq(usernameSeq), conn)(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: StaffRow, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(StaffRowQueries.insert(model), conn)(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.staffId, conn)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Staff")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[StaffRow], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(StaffRowQueries.insertBatch(models), conn)(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(StaffRowQueries.create(fields), conn)(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "staffId").toInt, conn)
    })
  }

  def remove(creds: Credentials, staffId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, staffId, conn)(td).flatMap {
      case Some(current) =>
        db.executeF(StaffRowQueries.removeByPrimaryKey(staffId), conn)(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find StaffRow matching [$staffId]")
    })
  }

  def update(creds: Credentials, staffId: Int, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, staffId, conn)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Staff [$staffId]")
      case Some(_) => db.executeF(StaffRowQueries.update(staffId, fields), conn)(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "staffId").flatMap(_.v).map(s => s.toInt).getOrElse(staffId), conn)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Staff [$staffId]"
          case None => throw new IllegalStateException(s"Cannot find StaffRow matching [$staffId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find StaffRow matching [$staffId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Seq[Any]], fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    db.executeF(StaffRowQueries.updateBulk(pks, fields), conn)(trace).map { x =>
      s"Updated [${fields.size}] fields for [$x of ${pks.size}] Staff"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[StaffRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, StaffRowQueries.fields)(td))
  }
}
