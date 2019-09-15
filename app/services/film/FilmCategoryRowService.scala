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
import models.film.FilmCategoryRow
import models.queries.film.FilmCategoryRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class FilmCategoryRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[FilmCategoryRow]("filmCategoryRow", "film" -> "FilmCategoryRow") {
  def getByPrimaryKey(creds: Credentials, filmId: Int, categoryId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(FilmCategoryRowQueries.getByPrimaryKey(filmId, categoryId))(td))
  }
  def getByPrimaryKeySeq(creds: Credentials, pkSeq: Seq[(Int, Int)])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (pkSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(FilmCategoryRowQueries.getByPrimaryKeySeq(pkSeq))(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(FilmCategoryRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(FilmCategoryRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(FilmCategoryRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(FilmCategoryRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(FilmCategoryRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByCategoryId(creds: Credentials, categoryId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.categoryId")(td => db.queryF(FilmCategoryRowQueries.CountByCategoryId(categoryId))(td))
  }
  def getByCategoryId(creds: Credentials, categoryId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.categoryId")(td => db.queryF(FilmCategoryRowQueries.GetByCategoryId(categoryId, orderBys, limit, offset))(td))
  }
  def getByCategoryIdSeq(creds: Credentials, categoryIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (categoryIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.categoryId.seq") { td =>
        db.queryF(FilmCategoryRowQueries.GetByCategoryIdSeq(categoryIdSeq))(td)
      }
    }
  }

  def countByFilmId(creds: Credentials, filmId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.filmId")(td => db.queryF(FilmCategoryRowQueries.CountByFilmId(filmId))(td))
  }
  def getByFilmId(creds: Credentials, filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.filmId")(td => db.queryF(FilmCategoryRowQueries.GetByFilmId(filmId, orderBys, limit, offset))(td))
  }
  def getByFilmIdSeq(creds: Credentials, filmIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (filmIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.filmId.seq") { td =>
        db.queryF(FilmCategoryRowQueries.GetByFilmIdSeq(filmIdSeq))(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(FilmCategoryRowQueries.CountByLastUpdate(lastUpdate))(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(FilmCategoryRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(FilmCategoryRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: FilmCategoryRow)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(FilmCategoryRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.filmId, model.categoryId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Film Category")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[FilmCategoryRow])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(FilmCategoryRowQueries.insertBatch(models))(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(FilmCategoryRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "filmId").toInt, fieldVal(fields, "categoryId").toInt)
    })
  }

  def remove(creds: Credentials, filmId: Int, categoryId: Int)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, filmId, categoryId)(td).flatMap {
      case Some(current) =>
        db.executeF(FilmCategoryRowQueries.removeByPrimaryKey(filmId, categoryId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find FilmCategoryRow matching [$filmId, $categoryId]")
    })
  }

  def update(creds: Credentials, filmId: Int, categoryId: Int, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, filmId, categoryId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Film Category [$filmId, $categoryId]")
      case Some(_) => db.executeF(FilmCategoryRowQueries.update(filmId, categoryId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "filmId").flatMap(_.v).map(s => s.toInt).getOrElse(filmId), fields.find(_.k == "categoryId").flatMap(_.v).map(s => s.toInt).getOrElse(categoryId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Film Category [$filmId, $categoryId]"
          case None => throw new IllegalStateException(s"Cannot find FilmCategoryRow matching [$filmId, $categoryId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find FilmCategoryRow matching [$filmId, $categoryId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Seq[Any]], fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    db.executeF(FilmCategoryRowQueries.updateBulk(pks, fields))(trace).map { x =>
      s"Updated [${fields.size}] fields for [$x of ${pks.size}] Film Categories"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[FilmCategoryRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, FilmCategoryRowQueries.fields)(td))
  }
}
