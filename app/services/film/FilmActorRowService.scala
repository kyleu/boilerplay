/* Generated File */
package services.film

import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import com.kyleu.projectile.services.{Credentials, ModelServiceHelper}
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.util.CsvUtils
import com.kyleu.projectile.util.tracing.{TraceData, TracingService}
import java.time.ZonedDateTime
import models.film.FilmActorRow
import models.queries.film.FilmActorRowQueries
import scala.concurrent.{ExecutionContext, Future}

@javax.inject.Singleton
class FilmActorRowService @javax.inject.Inject() (val db: JdbcDatabase, override val tracing: TracingService)(implicit ec: ExecutionContext) extends ModelServiceHelper[FilmActorRow]("filmActorRow") {
  def getByPrimaryKey(creds: Credentials, actorId: Int, filmId: Int)(implicit trace: TraceData) = {
    traceF("get.by.primary.key")(td => db.queryF(FilmActorRowQueries.getByPrimaryKey(actorId, filmId))(td))
  }
  def getByPrimaryKeySeq(creds: Credentials, pkSeq: Seq[(Int, Int)])(implicit trace: TraceData) = if (pkSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.primary.key.seq")(td => db.queryF(FilmActorRowQueries.getByPrimaryKeySeq(pkSeq))(td))
  }

  override def countAll(creds: Credentials, filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("get.all.count")(td => db.queryF(FilmActorRowQueries.countAll(filters))(td))
  }
  override def getAll(creds: Credentials, filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = {
    traceF("get.all")(td => db.queryF(FilmActorRowQueries.getAll(filters, orderBys, limit, offset))(td))
  }

  // Search
  override def searchCount(creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil)(implicit trace: TraceData) = {
    traceF("search.count")(td => db.queryF(FilmActorRowQueries.searchCount(q, filters))(td))
  }
  override def search(
    creds: Credentials, q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search")(td => db.queryF(FilmActorRowQueries.search(q, filters, orderBys, limit, offset))(td))
  }

  def searchExact(
    creds: Credentials, q: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None
  )(implicit trace: TraceData) = {
    traceF("search.exact")(td => db.queryF(FilmActorRowQueries.searchExact(q, orderBys, limit, offset))(td))
  }

  def countByActorId(creds: Credentials, actorId: Int)(implicit trace: TraceData) = traceF("count.by.actorId") { td =>
    db.queryF(FilmActorRowQueries.CountByActorId(actorId))(td)
  }
  def getByActorId(creds: Credentials, actorId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.actorId") { td =>
    db.queryF(FilmActorRowQueries.GetByActorId(actorId, orderBys, limit, offset))(td)
  }
  def getByActorIdSeq(creds: Credentials, actorIdSeq: Seq[Int])(implicit trace: TraceData) = if (actorIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.actorId.seq") { td =>
      db.queryF(FilmActorRowQueries.GetByActorIdSeq(actorIdSeq))(td)
    }
  }

  def countByFilmId(creds: Credentials, filmId: Int)(implicit trace: TraceData) = traceF("count.by.filmId") { td =>
    db.queryF(FilmActorRowQueries.CountByFilmId(filmId))(td)
  }
  def getByFilmId(creds: Credentials, filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.filmId") { td =>
    db.queryF(FilmActorRowQueries.GetByFilmId(filmId, orderBys, limit, offset))(td)
  }
  def getByFilmIdSeq(creds: Credentials, filmIdSeq: Seq[Int])(implicit trace: TraceData) = if (filmIdSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.filmId.seq") { td =>
      db.queryF(FilmActorRowQueries.GetByFilmIdSeq(filmIdSeq))(td)
    }
  }

  def countByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime)(implicit trace: TraceData) = traceF("count.by.lastUpdate") { td =>
    db.queryF(FilmActorRowQueries.CountByLastUpdate(lastUpdate))(td)
  }
  def getByLastUpdate(creds: Credentials, lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None)(implicit trace: TraceData) = traceF("get.by.lastUpdate") { td =>
    db.queryF(FilmActorRowQueries.GetByLastUpdate(lastUpdate, orderBys, limit, offset))(td)
  }
  def getByLastUpdateSeq(creds: Credentials, lastUpdateSeq: Seq[ZonedDateTime])(implicit trace: TraceData) = if (lastUpdateSeq.isEmpty) {
    Future.successful(Nil)
  } else {
    traceF("get.by.lastUpdate.seq") { td =>
      db.queryF(FilmActorRowQueries.GetByLastUpdateSeq(lastUpdateSeq))(td)
    }
  }

  // Mutations
  def insert(creds: Credentials, model: FilmActorRow)(implicit trace: TraceData) = traceF("insert") { td =>
    db.executeF(FilmActorRowQueries.insert(model))(td).flatMap {
      case 1 => getByPrimaryKey(creds, model.actorId, model.filmId)(td)
      case _ => throw new IllegalStateException("Unable to find newly-inserted Film Actor.")
    }
  }
  def insertBatch(creds: Credentials, models: Seq[FilmActorRow])(implicit trace: TraceData) = {
    traceF("insertBatch")(td => db.executeF(FilmActorRowQueries.insertBatch(models))(td))
  }
  def create(creds: Credentials, fields: Seq[DataField])(implicit trace: TraceData) = traceF("create") { td =>
    db.executeF(FilmActorRowQueries.create(fields))(td).flatMap { _ =>
      getByPrimaryKey(creds, fieldVal(fields, "actorId").toInt, fieldVal(fields, "filmId").toInt)
    }
  }

  def remove(creds: Credentials, actorId: Int, filmId: Int)(implicit trace: TraceData) = {
    traceF("remove")(td => getByPrimaryKey(creds, actorId, filmId)(td).flatMap {
      case Some(current) =>
        db.executeF(FilmActorRowQueries.removeByPrimaryKey(actorId, filmId))(td).map(_ => current)
      case None => throw new IllegalStateException(s"Cannot find FilmActorRow matching [$actorId, $filmId]")
    })
  }

  def update(creds: Credentials, actorId: Int, filmId: Int, fields: Seq[DataField])(implicit trace: TraceData) = {
    traceF("update")(td => getByPrimaryKey(creds, actorId, filmId)(td).flatMap {
      case Some(current) if fields.isEmpty => Future.successful(current -> s"No changes required for Film Actor [$actorId, $filmId]")
      case Some(_) => db.executeF(FilmActorRowQueries.update(actorId, filmId, fields))(td).flatMap { _ =>
        getByPrimaryKey(creds, fields.find(_.k == "actorId").flatMap(_.v).map(s => s.toInt).getOrElse(actorId), fields.find(_.k == "filmId").flatMap(_.v).map(s => s.toInt).getOrElse(filmId))(td).map {
          case Some(newModel) =>
            newModel -> s"Updated [${fields.size}] fields of Film Actor [$actorId, $filmId]"
          case None => throw new IllegalStateException(s"Cannot find FilmActorRow matching [$actorId, $filmId]")
        }
      }
      case None => throw new IllegalStateException(s"Cannot find FilmActorRow matching [$actorId, $filmId]")
    })
  }

  def csvFor(totalCount: Int, rows: Seq[FilmActorRow])(implicit trace: TraceData) = {
    traceB("export.csv")(td => CsvUtils.csvFor(Some(key), totalCount, rows, FilmActorRowQueries.fields)(td))
  }
}
