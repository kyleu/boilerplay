/* Generated File */
package models.queries.note

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
    DatabaseField(title = "Type", prop = "relType", col = "rel_type", typ = StringType),
    DatabaseField(title = "PK", prop = "relPk", col = "rel_pk", typ = StringType),
    DatabaseField(title = "Text", prop = "text", col = "text", typ = StringType),
    DatabaseField(title = "Author", prop = "author", col = "author", typ = UuidType),
    DatabaseField(title = "Created", prop = "created", col = "created", typ = TimestampType)
  )
  override protected val pkColumns = Seq("id")
  override protected val searchColumns = Seq("id", "rel_type", "rel_pk", "text", "author")

  def countAll(filters: Seq[Filter] = Nil) = onCountAll(filters)
  def getAll = GetAll

  val search = Search
  val searchCount = SearchCount
  val searchExact = SearchExact

  case class CountByAuthor(author: UUID) extends ColCount(column = "author", values = Seq(author))
  case class GetByAuthor(author: UUID, orderBys: Seq[OrderBy], limit: Option[Int], offset: Option[Int]) extends SeqQuery(
    whereClause = Some(quote("author") + "  = ?"), orderBy = ResultFieldHelper.orderClause(fields, orderBys: _*),
    limit = limit, offset = offset, values = Seq(author)
  )
  case class GetByAuthorSeq(authorSeq: Seq[UUID]) extends ColSeqQuery(column = "author", values = authorSeq)

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
