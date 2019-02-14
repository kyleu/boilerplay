/* Generated File */
package models.queries.note

import com.kyleu.projectile.models.database.{DatabaseField, Row}
import com.kyleu.projectile.models.database.DatabaseFieldType._
import com.kyleu.projectile.models.queries.{BaseQueries, ResultFieldHelper}
import com.kyleu.projectile.models.result.data.DataField
import com.kyleu.projectile.models.result.filter.Filter
import com.kyleu.projectile.models.result.orderBy.OrderBy
import java.time.LocalDateTime
import java.util.UUID
import models.note.NoteRow

object NoteRowQueries extends BaseQueries[NoteRow]("noteRow", "note") {
  override val fields = Seq(
    DatabaseField(title = "Id", prop = "id", col = "id", typ = UuidType),
    DatabaseField(title = "Rel Type", prop = "relType", col = "rel_type", typ = StringType),
    DatabaseField(title = "Rel Pk", prop = "relPk", col = "rel_pk", typ = StringType),
    DatabaseField(title = "Text", prop = "text", col = "text", typ = StringType),
    DatabaseField(title = "Author", prop = "author", col = "author", typ = UuidType),
    DatabaseField(title = "Created", prop = "created", col = "created", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "rel_type", "rel_pk", "text", "author", "created")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll(filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new GetAll(filters, orderBys, limit, offset)
  }

  def search(q: Option[String], filters: Seq[Filter] = Nil, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) = {
    new Search(q, filters, orderBys, limit, offset)
  }
  def searchCount(q: Option[String], filters: Seq[Filter] = Nil) = new SearchCount(q, filters)
  def searchExact(q: String, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) = new SearchExact(q, orderBys, limit, offset)

  def getByPrimaryKey(id: UUID) = new GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  final case class CountByAuthor(author: UUID) extends ColCount(column = "author", values = Seq(author))
  final case class GetByAuthor(author: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("author") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(author)
  )
  final case class GetByAuthorSeq(authorSeq: Seq[UUID]) extends ColSeqQuery(column = "author", values = authorSeq)

  final case class CountByCreated(created: LocalDateTime) extends ColCount(column = "created", values = Seq(created))
  final case class GetByCreated(created: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("created") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(created)
  )
  final case class GetByCreatedSeq(createdSeq: Seq[LocalDateTime]) extends ColSeqQuery(column = "created", values = createdSeq)

  final case class CountById(id: UUID) extends ColCount(column = "id", values = Seq(id))
  final case class GetById(id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(id)
  )
  final case class GetByIdSeq(idSeq: Seq[UUID]) extends ColSeqQuery(column = "id", values = idSeq)

  final case class CountByRelPk(relPk: String) extends ColCount(column = "rel_pk", values = Seq(relPk))
  final case class GetByRelPk(relPk: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rel_pk") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(relPk)
  )
  final case class GetByRelPkSeq(relPkSeq: Seq[String]) extends ColSeqQuery(column = "rel_pk", values = relPkSeq)

  final case class CountByRelType(relType: String) extends ColCount(column = "rel_type", values = Seq(relType))
  final case class GetByRelType(relType: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rel_type") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(relType)
  )
  final case class GetByRelTypeSeq(relTypeSeq: Seq[String]) extends ColSeqQuery(column = "rel_type", values = relTypeSeq)

  final case class CountByText(text: String) extends ColCount(column = "text", values = Seq(text))
  final case class GetByText(text: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("text") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(text)
  )
  final case class GetByTextSeq(textSeq: Seq[String]) extends ColSeqQuery(column = "text", values = textSeq)

  def insert(model: NoteRow) = new Insert(model)
  def insertBatch(models: Seq[NoteRow]) = new InsertBatch(models)
  def create(dataFields: Seq[DataField]) = new InsertFields(dataFields)

  def removeByPrimaryKey(id: UUID) = new RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = new UpdateFields(Seq[Any](id), fields)

  override def fromRow(row: Row) = NoteRow(
    id = UuidType(row, "id"),
    relType = StringType.opt(row, "rel_type"),
    relPk = StringType.opt(row, "rel_pk"),
    text = StringType(row, "text"),
    author = UuidType(row, "author"),
    created = TimestampType(row, "created")
  )
}
