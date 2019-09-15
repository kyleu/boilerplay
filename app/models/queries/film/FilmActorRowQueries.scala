/* Generated File */
package models.queries.film

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.film.FilmActorRow

object FilmActorRowQueries extends BaseQueries[FilmActorRow]("filmActorRow", "film_actor") {
  override val fields = Seq(
    DatabaseField(title = "Actor Id", prop = "actorId", col = "actor_id", typ = IntegerType),
    DatabaseField(title = "Film Id", prop = "filmId", col = "film_id", typ = IntegerType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override protected val pkColumns = Seq("actor_id", "film_id")
  override protected val searchColumns = Seq("actor_id", "film_id", "last_update")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(actorId: Int, filmId: Int) = new GetByPrimaryKey(Seq[Any](actorId, filmId))
  def getByPrimaryKeySeq(idSeq: Seq[(Int, Int)]) = new SeqQuery(
    whereClause = Some(idSeq.map(_ => "(\"actor_id\" = ? and \"film_id\" = ?)").mkString(" or ")),
    values = idSeq.flatMap(_.productIterator.toSeq)
  )

  final case class CountByActorId(actorId: Int) extends ColCount(column = "actor_id", values = Seq(actorId))
  final case class GetByActorId(actorId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("actor_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(actorId)
  )
  final case class GetByActorIdSeq(actorIdSeq: Seq[Int]) extends ColSeqQuery(column = "actor_id", values = actorIdSeq)

  final case class CountByFilmId(filmId: Int) extends ColCount(column = "film_id", values = Seq(filmId))
  final case class GetByFilmId(filmId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("film_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(filmId)
  )
  final case class GetByFilmIdSeq(filmIdSeq: Seq[Int]) extends ColSeqQuery(column = "film_id", values = filmIdSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  def insert(model: FilmActorRow) = new Insert(model)
  def insertBatch(models: Seq[FilmActorRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(actorId: Int, filmId: Int) = new RemoveByPrimaryKey(Seq[Any](actorId, filmId))

  def update(actorId: Int, filmId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](actorId, filmId), fields)
  def updateBulk(pks: Seq[Seq[Any]], fields: Seq[DataField]) = new UpdateFieldsBulk(pks, fields)

  override def fromRow(row: Row) = FilmActorRow(
    actorId = IntegerType(row, "actor_id"),
    filmId = IntegerType(row, "film_id"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
