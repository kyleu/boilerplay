/* Generated File */
package services.film

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.ModelServiceHelper
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.{Credentials, CsvUtils}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.ZonedDateTime
import models.film.CategoryRow
import models.queries.film.CategoryRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class CategoryRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[CategoryRow]("categoryRow", "film" -> "CategoryRow") {
  def getByPrimaryKey(creds: Credentials, categoryId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(CategoryRowQueries.getByPrimaryKey(categoryId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, categoryId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, categoryId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load categoryRow with categoryId [$categoryId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, categoryIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (categoryIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(CategoryRowQueries.getByPrimaryKeySeq(categoryIdSeq))(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(CategoryRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(CategoryRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(CategoryRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(CategoryRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(CategoryRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByCategoryId(creds: Credentials, categoryId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.categoryId")(td => db.queryF(CategoryRowQueries.CountByCategoryId(categoryId))(td))
  }
  def getByCategoryId(creds: Credentials, categoryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.categoryId")(td => db.queryF(CategoryRowQueries.GetByCategoryId(categoryId, orderBys, limit, offset))(td))
  }
  def getByCategoryIdSeq(creds: Credentials, categoryIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (categoryIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.categoryId.seq") { td =>
        db.queryF(CategoryRowQueries.GetByCategoryIdSeq(categoryIdSeq))(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(CategoryRowQueries.CountByLastUpdate(lastUpdate))(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(CategoryRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(CategoryRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
      }
    }
  }

  def countByName(creds: Credentials, name: String)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.name")(td => db.queryF(CategoryRowQueries.CountByName(name))(td))
  }
  def getByName(creds: Credentials, name: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.name")(td => db.queryF(CategoryRowQueries.GetByName(name, orderBys, limit, offset))(td))
  }
  def getByNameSeq(creds: Credentials, nameSeq: Seq[String])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (nameSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.name.seq") { td =>
        db.queryF(CategoryRowQueries.GetByNameSeq(nameSeq))(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: CategoryRow)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(CategoryRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.categoryId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Category")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[CategoryRow])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(CategoryRowQueries.insertBatch(models))(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(CategoryRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "categoryId").toInt)
    })
  }

  def remove(creds: Credentials, categoryId: Int)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, categoryId)(td).flatMap {
      case Some(current) =>
        db.executeF(CategoryRowQueries.removeByPrimaryKey(categoryId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find CategoryRow matching [$categoryId]")
    })
  }

  def update(creds: Credentials, categoryId: Int, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, categoryId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Category [$categoryId]")
      case Some(_) => db.executeF(CategoryRowQueries.update(categoryId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "categoryId").flatMap(_.v).map(s => s.toInt).getOrElse(categoryId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Category [$categoryId]"
          case None => throw new IllegalStateException(s"Cannot find CategoryRow matching [$categoryId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find CategoryRow matching [$categoryId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[CategoryRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, CategoryRowQueries.fields)(td))
  }
}
