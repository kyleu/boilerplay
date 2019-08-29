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
import models.customer.LanguageRow
import models.queries.customer.LanguageRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class LanguageRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[LanguageRow]("languageRow", "customer" -> "LanguageRow") {
  def getByPrimaryKey(creds: Credentials, languageId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(LanguageRowQueries.getByPrimaryKey(languageId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, languageId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, languageId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load languageRow with languageId [$languageId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, languageIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (languageIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(LanguageRowQueries.getByPrimaryKeySeq(languageIdSeq))(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(LanguageRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(LanguageRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(LanguageRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(LanguageRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(LanguageRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByLanguageId(creds: Credentials, languageId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.languageId")(td => db.queryF(LanguageRowQueries.CountByLanguageId(languageId))(td))
  }
  def getByLanguageId(creds: Credentials, languageId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.languageId")(td => db.queryF(LanguageRowQueries.GetByLanguageId(languageId, orderBys, limit, offset))(td))
  }
  def getByLanguageIdSeq(creds: Credentials, languageIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (languageIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.languageId.seq") { td =>
        db.queryF(LanguageRowQueries.GetByLanguageIdSeq(languageIdSeq))(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(LanguageRowQueries.CountByLastUpdate(lastUpdate))(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(LanguageRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(LanguageRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
      }
    }
  }

  def countByName(creds: Credentials, name: String)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.name")(td => db.queryF(LanguageRowQueries.CountByName(name))(td))
  }
  def getByName(creds: Credentials, name: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.name")(td => db.queryF(LanguageRowQueries.GetByName(name, orderBys, limit, offset))(td))
  }
  def getByNameSeq(creds: Credentials, nameSeq: Seq[String])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (nameSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.name.seq") { td =>
        db.queryF(LanguageRowQueries.GetByNameSeq(nameSeq))(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: LanguageRow)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(LanguageRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.languageId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Language.")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[LanguageRow])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => db.executeF(LanguageRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(LanguageRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "languageId").toInt)
    })
  }

  def remove(creds: Credentials, languageId: Int)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, languageId)(td).flatMap {
      case Some(current) =>
        db.executeF(LanguageRowQueries.removeByPrimaryKey(languageId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find LanguageRow matching [$languageId]")
    })
  }

  def update(creds: Credentials, languageId: Int, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, languageId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Language [$languageId]")
      case Some(_) => db.executeF(LanguageRowQueries.update(languageId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "languageId").flatMap(_.v).map(s => s.toInt).getOrElse(languageId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Language [$languageId]"
          case None => throw new IllegalStateException(s"Cannot find LanguageRow matching [$languageId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find LanguageRow matching [$languageId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[LanguageRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, LanguageRowQueries.fields)(td))
  }
}
