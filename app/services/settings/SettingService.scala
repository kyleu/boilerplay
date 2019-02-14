/* Generated File */
package services.settings

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.ApplicationDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import models.queries.settings.SettingQueries
import models.settings.{Setting, SettingKeyType}
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits._

@javax.inject.Singleton
class SettingService @javax.inject.Inject() (override val tracing: TracingService) extends ModelServiceHelper[Setting]("setting") {
  def getByPrimaryKey(creds: Credentials, k: SettingKeyType)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => ApplicationDatabase.queryF(SettingQueries.getByPrimaryKey(k))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, k: SettingKeyType)(implicit trace: TraceData) = getByPrimaryKey(creds, k).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load setting with k [$k]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, kSeq: Seq[SettingKeyType])(implicit trace: TraceData) = if (kSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => ApplicationDatabase.queryF(SettingQueries.getByPrimaryKeySeq(kSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => ApplicationDatabase.queryF(SettingQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => ApplicationDatabase.queryF(SettingQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => ApplicationDatabase.queryF(SettingQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => ApplicationDatabase.queryF(SettingQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => ApplicationDatabase.queryF(SettingQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByK(creds: Credentials, k: SettingKeyType)(implicit trace: TraceData) = traceF("count.by.k") { td =>
    ApplicationDatabase.queryF(SettingQueries.CountByK(k))(td)
  }
  def getByK(creds: Credentials, k: SettingKeyType, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.k") { td =>
    ApplicationDatabase.queryF(SettingQueries.GetByK(k, orderBys, limit, offset))(td)
  }
  def getByKSeq(creds: Credentials, kSeq: Seq[SettingKeyType])(implicit trace: TraceData) = if (kSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.k.seq") { td =>
      ApplicationDatabase.queryF(SettingQueries.GetByKSeq(kSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: Setting)(implicit trace: TraceData) = traceF("insert") { td =>
    ApplicationDatabase.executeF(SettingQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.k)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Setting.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[Setting])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => ApplicationDatabase.executeF(SettingQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    ApplicationDatabase.executeF(SettingQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, SettingKeyType.withValue(fieldVal(fields, "k")))
    }
  }

  def remove(creds: Credentials, k: SettingKeyType)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, k)(td).flatMap {
      case Some(current) =>
        ApplicationDatabase.executeF(SettingQueries.removeByPrimaryKey(k))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find Setting matching [$k]")
    })
  }

  def update(creds: Credentials, k: SettingKeyType, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, k)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Setting [$k]")
      case Some(_) => ApplicationDatabase.executeF(SettingQueries.update(k, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, k)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Setting [$k]"
          case None => throw new IllegalStateException(s"Cannot find Setting matching [$k]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find Setting matching [$k]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[Setting])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, SettingQueries.fields)(td))
  }
}
