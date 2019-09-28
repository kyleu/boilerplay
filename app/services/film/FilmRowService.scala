/* Generated File */
package services.film

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.ModelServiceHelper
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.{Credentials, CsvUtils}
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.sql.Connection
import java.time.ZonedDateTime
import models.film.{FilmRow, MpaaRatingType}
import models.queries.film.FilmRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class FilmRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[FilmRow]("filmRow", "film" -> "FilmRow") {
  def getByPrimaryKey(creds: Credentials, filmId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(FilmRowQueries.getByPrimaryKey(filmId), conn)(td))
  }
  def getByPrimaryKeyRequired(creds: Credentials, filmId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = getByPrimaryKey(creds, filmId, conn).map { opt =>
    opt.getOrElse(throw new IllegalStateException(s"Cannot load filmRow with filmId [$filmId]"))
  }
  def getByPrimaryKeySeq(creds: Credentials, filmIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (filmIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(FilmRowQueries.getByPrimaryKeySeq(filmIdSeq), conn)(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(FilmRowQueries.countAll(filters), conn)(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(FilmRowQueries.getAll(filters, orderBys, limit, offset), conn)(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(FilmRowQueries.searchCount(q, filters), conn)(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(FilmRowQueries.search(q, filters, orderBys, limit, offset), conn)(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(FilmRowQueries.searchExact(q, orderBys, limit, offset), conn)(td))
  }

  def countByDescription(creds: Credentials, description: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.description")(td => db.queryF(FilmRowQueries.CountByDescription(description), conn)(td))
  }
  def getByDescription(creds: Credentials, description: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.description")(td => db.queryF(FilmRowQueries.GetByDescription(description, orderBys, limit, offset), conn)(td))
  }
  def getByDescriptionSeq(creds: Credentials, descriptionSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (descriptionSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.description.seq") { td =>
        db.queryF(FilmRowQueries.GetByDescriptionSeq(descriptionSeq), conn)(td)
      }
    }
  }

  def countByFilmId(creds: Credentials, filmId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.filmId")(td => db.queryF(FilmRowQueries.CountByFilmId(filmId), conn)(td))
  }
  def getByFilmId(creds: Credentials, filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.filmId")(td => db.queryF(FilmRowQueries.GetByFilmId(filmId, orderBys, limit, offset), conn)(td))
  }
  def getByFilmIdSeq(creds: Credentials, filmIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (filmIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.filmId.seq") { td =>
        db.queryF(FilmRowQueries.GetByFilmIdSeq(filmIdSeq), conn)(td)
      }
    }
  }

  def countByFulltext(creds: Credentials, fulltext: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.fulltext")(td => db.queryF(FilmRowQueries.CountByFulltext(fulltext), conn)(td))
  }
  def getByFulltext(creds: Credentials, fulltext: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.fulltext")(td => db.queryF(FilmRowQueries.GetByFulltext(fulltext, orderBys, limit, offset), conn)(td))
  }
  def getByFulltextSeq(creds: Credentials, fulltextSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (fulltextSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.fulltext.seq") { td =>
        db.queryF(FilmRowQueries.GetByFulltextSeq(fulltextSeq), conn)(td)
      }
    }
  }

  def countByLanguageId(creds: Credentials, languageId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.languageId")(td => db.queryF(FilmRowQueries.CountByLanguageId(languageId), conn)(td))
  }
  def getByLanguageId(creds: Credentials, languageId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.languageId")(td => db.queryF(FilmRowQueries.GetByLanguageId(languageId, orderBys, limit, offset), conn)(td))
  }
  def getByLanguageIdSeq(creds: Credentials, languageIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (languageIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.languageId.seq") { td =>
        db.queryF(FilmRowQueries.GetByLanguageIdSeq(languageIdSeq), conn)(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(FilmRowQueries.CountByLastUpdate(lastUpdate), conn)(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(FilmRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset), conn)(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(FilmRowQueries.GetByLastUpdateSeq(lastUpdateSeq), conn)(td)
      }
    }
  }

  def countByLength(creds: Credentials, length: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.length")(td => db.queryF(FilmRowQueries.CountByLength(length), conn)(td))
  }
  def getByLength(creds: Credentials, length: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.length")(td => db.queryF(FilmRowQueries.GetByLength(length, orderBys, limit, offset), conn)(td))
  }
  def getByLengthSeq(creds: Credentials, lengthSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lengthSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.length.seq") { td =>
        db.queryF(FilmRowQueries.GetByLengthSeq(lengthSeq), conn)(td)
      }
    }
  }

  def countByOriginalLanguageId(creds: Credentials, originalLanguageId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.originalLanguageId")(td => db.queryF(FilmRowQueries.CountByOriginalLanguageId(originalLanguageId), conn)(td))
  }
  def getByOriginalLanguageId(creds: Credentials, originalLanguageId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.originalLanguageId")(td => db.queryF(FilmRowQueries.GetByOriginalLanguageId(originalLanguageId, orderBys, limit, offset), conn)(td))
  }
  def getByOriginalLanguageIdSeq(creds: Credentials, originalLanguageIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (originalLanguageIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.originalLanguageId.seq") { td =>
        db.queryF(FilmRowQueries.GetByOriginalLanguageIdSeq(originalLanguageIdSeq), conn)(td)
      }
    }
  }

  def countByRating(creds: Credentials, rating: MpaaRatingType, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.rating")(td => db.queryF(FilmRowQueries.CountByRating(rating), conn)(td))
  }
  def getByRating(creds: Credentials, rating: MpaaRatingType, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.rating")(td => db.queryF(FilmRowQueries.GetByRating(rating, orderBys, limit, offset), conn)(td))
  }
  def getByRatingSeq(creds: Credentials, ratingSeq: Seq[MpaaRatingType], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (ratingSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.rating.seq") { td =>
        db.queryF(FilmRowQueries.GetByRatingSeq(ratingSeq), conn)(td)
      }
    }
  }

  def countByReleaseYear(creds: Credentials, releaseYear: Long, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.releaseYear")(td => db.queryF(FilmRowQueries.CountByReleaseYear(releaseYear), conn)(td))
  }
  def getByReleaseYear(creds: Credentials, releaseYear: Long, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.releaseYear")(td => db.queryF(FilmRowQueries.GetByReleaseYear(releaseYear, orderBys, limit, offset), conn)(td))
  }
  def getByReleaseYearSeq(creds: Credentials, releaseYearSeq: Seq[Long], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (releaseYearSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.releaseYear.seq") { td =>
        db.queryF(FilmRowQueries.GetByReleaseYearSeq(releaseYearSeq), conn)(td)
      }
    }
  }

  def countByRentalDuration(creds: Credentials, rentalDuration: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.rentalDuration")(td => db.queryF(FilmRowQueries.CountByRentalDuration(rentalDuration), conn)(td))
  }
  def getByRentalDuration(creds: Credentials, rentalDuration: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.rentalDuration")(td => db.queryF(FilmRowQueries.GetByRentalDuration(rentalDuration, orderBys, limit, offset), conn)(td))
  }
  def getByRentalDurationSeq(creds: Credentials, rentalDurationSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (rentalDurationSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.rentalDuration.seq") { td =>
        db.queryF(FilmRowQueries.GetByRentalDurationSeq(rentalDurationSeq), conn)(td)
      }
    }
  }

  def countByRentalRate(creds: Credentials, rentalRate: BigDecimal, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.rentalRate")(td => db.queryF(FilmRowQueries.CountByRentalRate(rentalRate), conn)(td))
  }
  def getByRentalRate(creds: Credentials, rentalRate: BigDecimal, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.rentalRate")(td => db.queryF(FilmRowQueries.GetByRentalRate(rentalRate, orderBys, limit, offset), conn)(td))
  }
  def getByRentalRateSeq(creds: Credentials, rentalRateSeq: Seq[BigDecimal], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (rentalRateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.rentalRate.seq") { td =>
        db.queryF(FilmRowQueries.GetByRentalRateSeq(rentalRateSeq), conn)(td)
      }
    }
  }

  def countByTitle(creds: Credentials, title: String, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.title")(td => db.queryF(FilmRowQueries.CountByTitle(title), conn)(td))
  }
  def getByTitle(creds: Credentials, title: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.title")(td => db.queryF(FilmRowQueries.GetByTitle(title, orderBys, limit, offset), conn)(td))
  }
  def getByTitleSeq(creds: Credentials, titleSeq: Seq[String], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (titleSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.title.seq") { td =>
        db.queryF(FilmRowQueries.GetByTitleSeq(titleSeq), conn)(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: FilmRow, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(FilmRowQueries.insert(model), conn)(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.filmId, conn)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Film")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[FilmRow], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(FilmRowQueries.insertBatch(models), conn)(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(FilmRowQueries.create(fields), conn)(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "filmId").toInt, conn)
    })
  }

  def remove(creds: Credentials, filmId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, filmId, conn)(td).flatMap {
      case Some(current) =>
        db.executeF(FilmRowQueries.removeByPrimaryKey(filmId), conn)(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find FilmRow matching [$filmId]")
    })
  }

  def update(creds: Credentials, filmId: Int, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, filmId, conn)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Film [$filmId]")
      case Some(_) => db.executeF(FilmRowQueries.update(filmId, fields), conn)(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "filmId").flatMap(_.v).map(s => s.toInt).getOrElse(filmId), conn)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Film [$filmId]"
          case None => throw new IllegalStateException(s"Cannot find FilmRow matching [$filmId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find FilmRow matching [$filmId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Int], fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    Future.sequence(pks.map(pk => update(creds, pk, fields, conn))).map { x =>
      s"Updated [${fields.size}] fields for [${x.size} of ${pks.size}] FilmRow"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[FilmRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, FilmRowQueries.fields)(td))
  }
}
