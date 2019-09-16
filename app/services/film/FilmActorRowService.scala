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
import models.film.FilmActorRow
import models.queries.film.FilmActorRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class FilmActorRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[FilmActorRow]("filmActorRow", "film" -> "FilmActorRow") {
  def getByPrimaryKey(creds: Credentials, actorId: Int, filmId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.primary.key")(td => db.queryF(FilmActorRowQueries.getByPrimaryKey(actorId, filmId), conn)(td))
  }
  def getByPrimaryKeySeq(creds: Credentials, pkSeq: Seq[(Int, Int)], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (pkSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.primary.key.seq")(td => db.queryF(FilmActorRowQueries.getByPrimaryKeySeq(pkSeq), conn)(td))
    }
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all.count")(td => db.queryF(FilmActorRowQueries.countAll(filters), conn)(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.all")(td => db.queryF(FilmActorRowQueries.getAll(filters, orderBys, limit, offset), conn)(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.count")(td => db.queryF(FilmActorRowQueries.searchCount(q, filters), conn)(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search")(td => db.queryF(FilmActorRowQueries.search(q, filters, orderBys, limit, offset), conn)(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None
  )(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("search.exact")(td => db.queryF(FilmActorRowQueries.searchExact(q, orderBys, limit, offset), conn)(td))
  }

  def countByActorId(creds: Credentials, actorId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.actorId")(td => db.queryF(FilmActorRowQueries.CountByActorId(actorId), conn)(td))
  }
  def getByActorId(creds: Credentials, actorId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.actorId")(td => db.queryF(FilmActorRowQueries.GetByActorId(actorId, orderBys, limit, offset), conn)(td))
  }
  def getByActorIdSeq(creds: Credentials, actorIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (actorIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.actorId.seq") { td =>
        db.queryF(FilmActorRowQueries.GetByActorIdSeq(actorIdSeq), conn)(td)
      }
    }
  }

  def countByFilmId(creds: Credentials, filmId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.filmId")(td => db.queryF(FilmActorRowQueries.CountByFilmId(filmId), conn)(td))
  }
  def getByFilmId(creds: Credentials, filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.filmId")(td => db.queryF(FilmActorRowQueries.GetByFilmId(filmId, orderBys, limit, offset), conn)(td))
  }
  def getByFilmIdSeq(creds: Credentials, filmIdSeq: Seq[Int], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (filmIdSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.filmId.seq") { td =>
        db.queryF(FilmActorRowQueries.GetByFilmIdSeq(filmIdSeq), conn)(td)
      }
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("count.by.lastUpdate")(td => db.queryF(FilmActorRowQueries.CountByLastUpdate(lastUpdate), conn)(td))
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    traceF("get.by.lastUpdate")(td => db.queryF(FilmActorRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset), conn)(td))
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "view") {
    if (lastUpdateSeq.isEmpty) {
      Future.successful(Nil)
    } else {
      traceF("get.by.lastUpdate.seq") { td =>
        db.queryF(FilmActorRowQueries.GetByLastUpdateSeq(lastUpdateSeq), conn)(td)
      }
    }
  }

  // Mutations
  def insert(creds: Credentials, model: FilmActorRow, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insert")(td => db.executeF(FilmActorRowQueries.insert(model), conn)(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.actorId, model.filmId, conn)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Film Actor")
    })
  }
  def insertBatch(creds: Credentials, models: Seq[FilmActorRow], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("insertBatch")(td => if (models.isEmpty) {
      Future.successful(0)
    } else {
      db.executeF(FilmActorRowQueries.insertBatch(models), conn)(td)
    })
  }
  def create(creds: Credentials, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("create")(td => db.executeF(FilmActorRowQueries.create(fields), conn)(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "actorId").toInt, fieldVal(fields, "filmId").toInt, conn)
    })
  }

  def remove(creds: Credentials, actorId: Int, filmId: Int, conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("remove")(td => getByPrimaryKey(creds, actorId, filmId, conn)(td).flatMap {
      case Some(current) =>
        db.executeF(FilmActorRowQueries.removeByPrimaryKey(actorId, filmId), conn)(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find FilmActorRow matching [$actorId, $filmId]")
    })
  }

  def update(creds: Credentials, actorId: Int, filmId: Int, fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    traceF("update")(td => getByPrimaryKey(creds, actorId, filmId, conn)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Film Actor [$actorId, $filmId]")
      case Some(_) => db.executeF(FilmActorRowQueries.update(actorId, filmId, fields), conn)(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "actorId").flatMap(_.v).map(s => s.toInt).getOrElse(actorId), fields.find(_.k == "filmId").flatMap(_.v).map(s => s.toInt).getOrElse(filmId), conn)(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Film Actor [$actorId, $filmId]"
          case None => throw new IllegalStateException(s"Cannot find FilmActorRow matching [$actorId, $filmId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find FilmActorRow matching [$actorId, $filmId]")
    })
  }

  def updateBulk(creds: Credentials, pks: Seq[Seq[Any]], fields: Seq[DataField], conn: Option[Connection] = None)(implicit trace: TraceData) = checkPerm(creds, "edit") {
    db.executeF(FilmActorRowQueries.updateBulk(pks, fields), conn)(trace).map { x =>
      s"Updated [${fields.size}] fields for [$x of ${pks.size}] Film Actors"
    }
  }

  def csvFor(totalCount: Int, rows: Seq[FilmActorRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, FilmActorRowQueries.fields)(td))
  }
}
