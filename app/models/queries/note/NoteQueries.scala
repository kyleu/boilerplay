/* Generated File */
package models.queries.note

import java.time.LocalDateTime
import java.util.UUID
import models.database.{DatabaseField, Row}
import models.database.DatabaseFieldType._
import models.note.Note
import models.queries.BaseQueries
import models.result.ResultFieldHelper
import models.result.data.DataField
import models.result.filter.Filter
import models.result.orderBy.OrderBy

object NoteQueries extends BaseQueries[Note]("note", "note") {
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
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

  def getByPrimaryKey(id: UUID) = GetByPrimaryKey(Seq(id))
  def getByPrimaryKeySeq(idSeq: Seq[UUID]) = new ColSeqQuery(column = "id", values = idSeq)

  case class CountByAuthor(author: UUID) extends ColCount(column = "author", values = Seq(author))
  case class GetByAuthor(author: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("author") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(author)
  )
  case class GetByAuthorSeq(authorSeq: Seq[UUID]) extends ColSeqQuery(column = "author", values = authorSeq)

  case class CountByCreated(created: LocalDateTime) extends ColCount(column = "created", values = Seq(created))
  case class GetByCreated(created: LocalDateTime, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("created") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(created)
  )
  case class GetByCreatedSeq(createdSeq: Seq[LocalDateTime]) extends ColSeqQuery(column = "created", values = createdSeq)

  case class CountById(id: UUID) extends ColCount(column = "id", values = Seq(id))
  case class GetById(id: UUID, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("id") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(id)
  )
  case class GetByIdSeq(idSeq: Seq[UUID]) extends ColSeqQuery(column = "id", values = idSeq)

  case class CountByRelPk(relPk: String) extends ColCount(column = "rel_pk", values = Seq(relPk))
  case class GetByRelPk(relPk: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rel_pk") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(relPk)
  )
  case class GetByRelPkSeq(relPkSeq: Seq[String]) extends ColSeqQuery(column = "rel_pk", values = relPkSeq)

  case class CountByRelType(relType: String) extends ColCount(column = "rel_type", values = Seq(relType))
  case class GetByRelType(relType: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("rel_type") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(relType)
  )
  case class GetByRelTypeSeq(relTypeSeq: Seq[String]) extends ColSeqQuery(column = "rel_type", values = relTypeSeq)

  case class CountByText(text: String) extends ColCount(column = "text", values = Seq(text))
  case class GetByText(text: String, orderBys: Seq[OrderBy] = Nil, limit: Option[Int] = None, offset: Option[Int] = None) extends SeqQuery(
    whereClause = Some(quote("text") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(text)
  )
  case class GetByTextSeq(textSeq: Seq[String]) extends ColSeqQuery(column = "text", values = textSeq)

  def insert(model: Note) = Insert(model)
  def insertBatch(models: Seq[Note]) = InsertBatch(models)
  def create(dataFields: Seq[DataField]) = CreateFields(dataFields)

  def removeByPrimaryKey(id: UUID) = RemoveByPrimaryKey(Seq[Any](id))

  def update(id: UUID, fields: Seq[DataField]) = UpdateFields(Seq[Any](id), fields)

  override def fromRow(row: Row) = Note(
    id = UuidType(row, "id"),
    relType = StringType.opt(row, "rel_type"),
    relPk = StringType.opt(row, "rel_pk"),
    text = StringType(row, "text"),
    author = UuidType(row, "author"),
    created = TimestampType(row, "created")
  )
}
