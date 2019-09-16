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
import models.address.AddressRow
import models.queries.address.AddressRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class AddressRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[AddressRow]("addressRow", "address" -> "AddressRow") {
  def getByPrimaryKey(creds: Credentials, addressId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(AddressRowQueries.getByPrimaryKey(addressId), conn)(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, addressId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = getByPrimaryKey(creds, addressId, conn).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load addressRow with addressId [$addressId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, addressIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (addressIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(AddressRowQueries.getByPrimaryKeySeq(addressIdSeq), conn)(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(AddressRowQueries.countAll(filters), conn)(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(AddressRowQueries.getAll(filters, orderBys, limit, offset), conn)(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(AddressRowQueries.searchCount(q, filters), conn)(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(AddressRowQueries.search(q, filters, orderBys, limit, offset), conn)(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(AddressRowQueries.searchExact(q, orderBys, limit, offset), conn)(td))
  }

  def countByAddress(creds: Credentials, address: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.address")(td => db.queryF(AddressRowQueries.CountByAddress(address), conn)(td))
  }
  def getByAddress(creds: Credentials, address: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.address")(td => db.queryF(AddressRowQueries.GetByAddress(address, orderBys, limit, offset), conn)(td))
  }
  def getByAddressSeq(creds: Credentials, addressSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (addressSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.address.seq") { td =>
        db.queryF(AddressRowQueries.GetByAddressSeq(addressSeq), conn)(td)
      }
    }
  }

  def countByAddress2(creds: Credentials, address2: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.address2")(td => db.queryF(AddressRowQueries.CountByAddress2(address2), conn)(td))
  }
  def getByAddress2(creds: Credentials, address2: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.address2")(td => db.queryF(AddressRowQueries.GetByAddress2(address2, orderBys, limit, offset), conn)(td))
  }
  def getByAddress2Seq(creds: Credentials, address2Seq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (address2Seq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.address2.seq") { td =>
        db.queryF(AddressRowQueries.GetByAddress2Seq(address2Seq), conn)(td)
      }
    }
  }

  def countByAddressId(creds: Credentials, addressId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.addressId")(td => db.queryF(AddressRowQueries.CountByAddressId(addressId), conn)(td))
  }
  def getByAddressId(creds: Credentials, addressId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.addressId")(td => db.queryF(AddressRowQueries.GetByAddressId(addressId, orderBys, limit, offset), conn)(td))
  }
  def getByAddressIdSeq(creds: Credentials, addressIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (addressIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.addressId.seq") { td =>
        db.queryF(AddressRowQueries.GetByAddressIdSeq(addressIdSeq), conn)(td)
      }
    }
  }

  def countByCityId(creds: Credentials, cityId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.cityId")(td => db.queryF(AddressRowQueries.CountByCityId(cityId), conn)(td))
  }
  def getByCityId(creds: Credentials, cityId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.cityId")(td => db.queryF(AddressRowQueries.GetByCityId(cityId, orderBys, limit, offset), conn)(td))
  }
  def getByCityIdSeq(creds: Credentials, cityIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (cityIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.cityId.seq") { td =>
        db.queryF(AddressRowQueries.GetByCityIdSeq(cityIdSeq), conn)(td)
      }
    }
  }

  def countByDistrict(creds: Credentials, district: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.district")(td => db.queryF(AddressRowQueries.CountByDistrict(district), conn)(td))
  }
  def getByDistrict(creds: Credentials, district: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.district")(td => db.queryF(AddressRowQueries.GetByDistrict(district, orderBys, limit, offset), conn)(td))
  }
  def getByDistrictSeq(creds: Credentials, districtSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (districtSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.district.seq") { td =>
        db.queryF(AddressRowQueries.GetByDistrictSeq(districtSeq), conn)(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(AddressRowQueries.CountByLastUpdate(lastUpdate), conn)(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(AddressRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset), conn)(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(AddressRowQueries.GetByLastUpdateSeq(lastUpdateSeq), conn)(td)
      }
    }
  }

  def countByPhone(creds: Credentials, phone: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.phone")(td => db.queryF(AddressRowQueries.CountByPhone(phone), conn)(td))
  }
  def getByPhone(creds: Credentials, phone: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.phone")(td => db.queryF(AddressRowQueries.GetByPhone(phone, orderBys, limit, offset), conn)(td))
  }
  def getByPhoneSeq(creds: Credentials, phoneSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (phoneSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.phone.seq") { td =>
        db.queryF(AddressRowQueries.GetByPhoneSeq(phoneSeq), conn)(td)
      }
    }
  }

  def countByPostalCode(creds: Credentials, postalCode: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.postalCode")(td => db.queryF(AddressRowQueries.CountByPostalCode(postalCode), conn)(td))
  }
  def getByPostalCode(creds: Credentials, postalCode: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.postalCode")(td => db.queryF(AddressRowQueries.GetByPostalCode(postalCode, orderBys, limit, offset), conn)(td))
  }
  def getByPostalCodeSeq(creds: Credentials, postalCodeSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (postalCodeSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.postalCode.seq") { td =>
        db.queryF(AddressRowQueries.GetByPostalCodeSeq(postalCodeSeq), conn)(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: AddressRow, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(AddressRowQueries.insert(model), conn)(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.addressId, conn)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Address")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[AddressRow], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(AddressRowQueries.insertBatch(models), conn)(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(AddressRowQueries.create(fields), conn)(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "addressId").toInt, conn)
    })
  }

  def remove(creds: Credentials, addressId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, addressId, conn)(td).flatMap {
      case Some(current) =>
        db.executeF(AddressRowQueries.removeByPrimaryKey(addressId), conn)(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find AddressRow matching [$addressId]")
    })
  }

  def update(creds: Credentials, addressId: Int, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, addressId, conn)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Address [$addressId]")
      case Some(_) => db.executeF(AddressRowQueries.update(addressId, fields), conn)(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "addressId").flatMap(_.v).map(s => s.toInt).getOrElse(addressId), conn)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Address [$addressId]"
          case None => throw new IllegalStateException(s"Cannot find AddressRow matching [$addressId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find AddressRow matching [$addressId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Seq[Any]], fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    db.executeF(AddressRowQueries.updateBulk(pks, fields), conn)(trace).map { x =>
      s"Updated [${fields.size}] fields for [$x of ${pks.size}] Addresses"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[AddressRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, AddressRowQueries.fields)(td))
  }
}
