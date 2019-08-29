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
import models.film.{FilmRow, MpaaRatingType}
import models.queries.film.FilmRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class FilmRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[FilmRow]("filmRow", "film" -> "FilmRow") {
  def getByPrimaryKey(creds: Credentials, filmId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(FilmRowQueries.getByPrimaryKey(filmId))(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, filmId: Int)(implicit trace: TraceData) = getByPrimaryKey(creds, filmId).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load filmRow with filmId [$filmId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, filmIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (filmIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(FilmRowQueries.getByPrimaryKeySeq(filmIdSeq))(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(FilmRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(FilmRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(FilmRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(FilmRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(FilmRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByFilmId(creds: Credentials, filmId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.filmId")(td => db.queryF(FilmRowQueries.CountByFilmId(filmId))(td))
  }
  def getByFilmId(creds: Credentials, filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.filmId")(td => db.queryF(FilmRowQueries.GetByFilmId(filmId, orderBys, limit, offset))(td))
  }
  def getByFilmIdSeq(creds: Credentials, filmIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (filmIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.filmId.seq") { td =>
        db.queryF(FilmRowQueries.GetByFilmIdSeq(filmIdSeq))(td)
      }
    }
  }

  def countByLanguageId(creds: Credentials, languageId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.languageId")(td => db.queryF(FilmRowQueries.CountByLanguageId(languageId))(td))
  }
  def getByLanguageId(creds: Credentials, languageId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.languageId")(td => db.queryF(FilmRowQueries.GetByLanguageId(languageId, orderBys, limit, offset))(td))
  }
  def getByLanguageIdSeq(creds: Credentials, languageIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (languageIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.languageId.seq") { td =>
        db.queryF(FilmRowQueries.GetByLanguageIdSeq(languageIdSeq))(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(FilmRowQueries.CountByLastUpdate(lastUpdate))(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(FilmRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(FilmRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
      }
    }
  }

  def countByOriginalLanguageId(creds: Credentials, originalLanguageId: Int)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.originalLanguageId")(td => db.queryF(FilmRowQueries.CountByOriginalLanguageId(originalLanguageId))(td))
  }
  def getByOriginalLanguageId(creds: Credentials, originalLanguageId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.originalLanguageId")(td => db.queryF(FilmRowQueries.GetByOriginalLanguageId(originalLanguageId, orderBys, limit, offset))(td))
  }
  def getByOriginalLanguageIdSeq(creds: Credentials, originalLanguageIdSeq: Seq[Int])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (originalLanguageIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.originalLanguageId.seq") { td =>
        db.queryF(FilmRowQueries.GetByOriginalLanguageIdSeq(originalLanguageIdSeq))(td)
      }
    }
  }

  def countByRating(creds: Credentials, rating: MpaaRatingType)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.rating")(td => db.queryF(FilmRowQueries.CountByRating(rating))(td))
  }
  def getByRating(creds: Credentials, rating: MpaaRatingType, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.rating")(td => db.queryF(FilmRowQueries.GetByRating(rating, orderBys, limit, offset))(td))
  }
  def getByRatingSeq(creds: Credentials, ratingSeq: Seq[MpaaRatingType])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (ratingSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.rating.seq") { td =>
        db.queryF(FilmRowQueries.GetByRatingSeq(ratingSeq))(td)
      }
    }
  }

  def countByTitle(creds: Credentials, title: String)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.title")(td => db.queryF(FilmRowQueries.CountByTitle(title))(td))
  }
  def getByTitle(creds: Credentials, title: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.title")(td => db.queryF(FilmRowQueries.GetByTitle(title, orderBys, limit, offset))(td))
  }
  def getByTitleSeq(creds: Credentials, titleSeq: Seq[String])(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (titleSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.title.seq") { td =>
        db.queryF(FilmRowQueries.GetByTitleSeq(titleSeq))(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: FilmRow)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(FilmRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.filmId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Film.")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[FilmRow])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => db.executeF(FilmRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(FilmRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "filmId").toInt)
    })
  }

  def remove(creds: Credentials, filmId: Int)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, filmId)(td).flatMap {
      case Some(current) =>
        db.executeF(FilmRowQueries.removeByPrimaryKey(filmId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find FilmRow matching [$filmId]")
    })
  }

  def update(creds: Credentials, filmId: Int, fields: Seq[DataField])(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, filmId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Film [$filmId]")
      case Some(_) => db.executeF(FilmRowQueries.update(filmId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "filmId").flatMap(_.v).map(s => s.toInt).getOrElse(filmId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Film [$filmId]"
          case None => throw new IllegalStateException(s"Cannot find FilmRow matching [$filmId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find FilmRow matching [$filmId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[FilmRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, FilmRowQueries.fields)(td))
  }
}
