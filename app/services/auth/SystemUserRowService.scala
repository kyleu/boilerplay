/* Generated File */
package services.auth

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.ApplicationDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.util.UUID
import models.auth.SystemUserRow
import models.queries.auth.SystemUserRowQueries
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

@javax.inject.Singleton
class SystemUserRowService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[SystemUserRow]("systemUserRow") {
  def getByPrimaryKey(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(SystemUserRowQueries.getByPrimaryKey(id))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, id: UUID)(implicit trace: TraceData) = getByPrimaryKey(creds, id).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load systemUserRow with id [$id]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = if (idSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(SystemUserRowQueries.getByPrimaryKeySeq(idSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(SystemUserRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(SystemUserRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(SystemUserRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(SystemUserRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(SystemUserRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countById(creds: Credentials, id: UUID)(implicit trace: TraceData) = traceF("count.by.id") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.CountById(id))(td)
  }
  def getById(creds: Credentials, id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.id") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.GetById(id, orderBys, limit, offset))(td)
  }
  def getByIdSeq(creds: Credentials, idSeq: Seq[UUID])(implicit trace: TraceData) = if (idSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.id.seq") { td =>
      ApplicationDatabase.queryF(SystemUserRowQueries.GetByIdSeq(idSeq))(td)
    }
  }

  def countByKey(creds: Credentials, key: String)(implicit trace: TraceData) = traceF("count.by.key") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.CountByKey(key))(td)
  }
  def getByKey(creds: Credentials, key: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.key") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.GetByKey(key, orderBys, limit, offset))(td)
  }
  def getByKeySeq(creds: Credentials, keySeq: Seq[String])(implicit trace: TraceData) = if (keySeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.key.seq") { td =>
      ApplicationDatabase.queryF(SystemUserRowQueries.GetByKeySeq(keySeq))(td)
    }
  }

  def countByProvider(creds: Credentials, provider: String)(implicit trace: TraceData) = traceF("count.by.provider") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.CountByProvider(provider))(td)
  }
  def getByProvider(creds: Credentials, provider: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.provider") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.GetByProvider(provider, orderBys, limit, offset))(td)
  }
  def getByProviderSeq(creds: Credentials, providerSeq: Seq[String])(implicit trace: TraceData) = if (providerSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.provider.seq") { td =>
      ApplicationDatabase.queryF(SystemUserRowQueries.GetByProviderSeq(providerSeq))(td)
    }
  }

  def countByUsername(creds: Credentials, username: String)(implicit trace: TraceData) = traceF("count.by.username") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.CountByUsername(username))(td)
  }
  def getByUsername(creds: Credentials, username: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.username") { td =>
    ApplicationDatabase.queryF(SystemUserRowQueries.GetByUsername(username, orderBys, limit, offset))(td)
  }
  def getByUsernameSeq(creds: Credentials, usernameSeq: Seq[String])(implicit trace: TraceData) = if (usernameSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.username.seq") { td =>
      ApplicationDatabase.queryF(SystemUserRowQueries.GetByUsernameSeq(usernameSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: SystemUserRow)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(SystemUserRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.id)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted System User.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[SystemUserRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(SystemUserRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(SystemUserRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, UUID.fromString(fieldVal(fields, "id")))
    }
  }

  def remove(creds: Credentials, id: UUID)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(SystemUserRowQueries.removeByPrimaryKey(id))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find SystemUserRow matching [$id]")
    })
  }

  def update(creds: Credentials, id: UUID, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, id)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for System User [$id]")
      case Some(_) => ApplicationDatabase.executeF(SystemUserRowQueries.update(id, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, id)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of System User [$id]"
          case None => throw new IllegalStateException(s"Cannot find SystemUserRow matching [$id]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find SystemUserRow matching [$id]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[SystemUserRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, SystemUserRowQueries.fields)(td))
  }
}
