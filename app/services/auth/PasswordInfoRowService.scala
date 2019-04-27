/* Generated File */
package services.auth

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import models.auth.PasswordInfoRow
import models.queries.auth.PasswordInfoRowQueries
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@javax.inject.Singleton
class PasswordInfoRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService) extends ModelServiceHelper[PasswordInfoRow]("passwordInfoRow") {
  def getByPrimaryKey(creds: Credentials, provider: String, key: String)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(PasswordInfoRowQueries.getByPrimaryKey(provider, key))(td))
  }
  def getByPrimaryKeySeq(creds: Credentials, pkSeq: Seq[(String, String)])(implicit trace: TraceData) = if (pkSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(PasswordInfoRowQueries.getByPrimaryKeySeq(pkSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(PasswordInfoRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(PasswordInfoRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(PasswordInfoRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(PasswordInfoRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(PasswordInfoRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByKey(creds: Credentials, key: String)(implicit trace: TraceData) = traceF("count.by.key") { td =>
    db.queryF(PasswordInfoRowQueries.CountByKey(key))(td)
  }
  def getByKey(creds: Credentials, key: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.key") { td =>
    db.queryF(PasswordInfoRowQueries.GetByKey(key, orderBys, limit, offset))(td)
  }
  def getByKeySeq(creds: Credentials, keySeq: Seq[String])(implicit trace: TraceData) = if (keySeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.key.seq") { td =>
      db.queryF(PasswordInfoRowQueries.GetByKeySeq(keySeq))(td)
    }
  }

  def countByProvider(creds: Credentials, provider: String)(implicit trace: TraceData) = traceF("count.by.provider") { td =>
    db.queryF(PasswordInfoRowQueries.CountByProvider(provider))(td)
  }
  def getByProvider(creds: Credentials, provider: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.provider") { td =>
    db.queryF(PasswordInfoRowQueries.GetByProvider(provider, orderBys, limit, offset))(td)
  }
  def getByProviderSeq(creds: Credentials, providerSeq: Seq[String])(implicit trace: TraceData) = if (providerSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.provider.seq") { td =>
      db.queryF(PasswordInfoRowQueries.GetByProviderSeq(providerSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: PasswordInfoRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(PasswordInfoRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.provider, model.key)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Password Info.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[PasswordInfoRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(PasswordInfoRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(PasswordInfoRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "provider"), fieldVal(fields, "key"))
    }
  }

  def remove(creds: Credentials, provider: String, key: String)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, provider, key)(td).flatMap {
      case Some(current) =>
        db.executeF(PasswordInfoRowQueries.removeByPrimaryKey(provider, key))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find PasswordInfoRow matching [$provider, $key]")
    })
  }

  def update(creds: Credentials, provider: String, key: String, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, provider, key)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Password Info [$provider, $key]")
      case Some(_) => db.executeF(PasswordInfoRowQueries.update(provider, key, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, provider, key)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Password Info [$provider, $key]"
          case None => throw new IllegalStateException(s"Cannot find PasswordInfoRow matching [$provider, $key]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find PasswordInfoRow matching [$provider, $key]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[PasswordInfoRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, PasswordInfoRowQueries.fields)(td))
  }
}
