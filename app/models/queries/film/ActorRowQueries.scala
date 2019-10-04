/* Generated File */
package models.queries.film

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.ZonedDateTime
import models.film.ActorRow

object ActorRowQueries extends BaseQueries[ActorRow]("actorRow", "actor") {
  override val fields = Seq(
    DatabaseField(title = "Actor Id", prop = "actorId", col = "actor_id", typ = IntegerType),
    DatabaseField(title = "First Name", prop = "firstName", col = "first_name", typ = StringType),
    DatabaseField(title = "Last Name", prop = "lastName", col = "last_name", typ = StringType),
    DatabaseField(title = "Last Update", prop = "lastUpdate", col = "last_update", typ = TimestampZonedType)
  )
  override val pkColumns = Seq("actor_id")
  override protected val searchColumns = Seq("actor_id", "first_name", "last_name", "last_update")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(actorId: Int) = new GetByPrimaryKey(Seq(actorId))
  def getByPrimaryKeySeq(actorIdSeq: Seq[Int]) = new ColSeqQuery(column = "actor_id", values = actorIdSeq)

  final case class CountByActorId(actorId: Int) extends ColCount(column = "actor_id", values = Seq(actorId))
  final case class GetByActorId(actorId: Int, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("actor_id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(actorId)
  )
  final case class GetByActorIdSeq(actorIdSeq: Seq[Int]) extends ColSeqQuery(column = "actor_id", values = actorIdSeq)

  final case class CountByFirstName(firstName: String) extends ColCount(column = "first_name", values = Seq(firstName))
  final case class GetByFirstName(firstName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("first_name") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(firstName)
  )
  final case class GetByFirstNameSeq(firstNameSeq: Seq[String]) extends ColSeqQuery(column = "first_name", values = firstNameSeq)

  final case class CountByLastName(lastName: String) extends ColCount(column = "last_name", values = Seq(lastName))
  final case class GetByLastName(lastName: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_name") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastName)
  )
  final case class GetByLastNameSeq(lastNameSeq: Seq[String]) extends ColSeqQuery(column = "last_name", values = lastNameSeq)

  final case class CountByLastUpdate(lastUpdate: ZonedDateTime) extends ColCount(column = "last_update", values = Seq(lastUpdate))
  final case class GetByLastUpdate(lastUpdate: ZonedDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("last_update") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(lastUpdate)
  )
  final case class GetByLastUpdateSeq(lastUpdateSeq: Seq[ZonedDateTime]) extends ColSeqQuery(column = "last_update", values = lastUpdateSeq)

  def insert(model: ActorRow) = new Insert(model)
  def insertBatch(models: Seq[ActorRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(actorId: Int) = new RemoveByPrimaryKey(Seq[Any](actorId))

  def update(actorId: Int, fields: Seq[DataField]) = new UpdateFields(Seq[Any](actorId), fields)
  def updateBulk(pks: Seq[Seq[Any]], fields: Seq[DataField]) = new UpdateFieldsBulk(pks, fields)

  override def fromRow(row: Row) = ActorRow(
    actorId = IntegerType(row, "actor_id"),
    firstName = StringType(row, "first_name"),
    lastName = StringType(row, "last_name"),
    lastUpdate = TimestampZonedType(row, "last_update")
  )
}
