/* Generated File */
package models.note

import java.util.UUID
import models.graphql.{GraphQLContext, SchemaHelper}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.result.data.DataFieldSchema
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import models.user.SystemUserSchema
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.macros.derive._
import sangria.schema._
import scala.concurrent.Future
import util.FutureUtils.graphQlContext

object NoteSchema extends SchemaHelper("note") {
  implicit val notePrimaryKeyId: HasId[Note, UUID] = HasId[Note, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.services.noteServices.noteService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val noteByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val noteIdArg = Argument("id", uuidType, description = "Returns the Note matching the provided Id.")

  val noteByAuthorRelation = Relation[Note, UUID]("byAuthor", x => Seq(x.author))
  val noteByAuthorFetcher = Fetcher.rel[GraphQLContext, Note, Note, UUID](
    getByPrimaryKeySeq, (c, rels) => c.services.noteServices.noteService.getByAuthorSeq(c.creds, rels(noteByAuthorRelation))(c.trace)
  )

  implicit lazy val noteType: ObjectType[GraphQLContext, Note] = deriveObjectType(
    AddFields(
      Field(
        name = "author",
        fieldType = SystemUserSchema.systemUserType,
        resolve = ctx => SystemUserSchema.systemUserByPrimaryKeyFetcher.defer(ctx.value.author)
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.app.coreServices.notes.getFor(c.ctx.creds, "note", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val noteResultType: ObjectType[GraphQLContext, NoteResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "note", desc = Some("Retrieves a single Note using its primary key."), t = OptionType(noteType), f = (c, td) => {
      c.ctx.services.noteServices.noteService.getByPrimaryKey(c.ctx.creds, c.arg(noteIdArg))(td)
    }, noteIdArg),
    unitField(name = "notes", desc = Some("Searches for Notes using the provided arguments."), t = noteResultType, f = (c, td) => {
      runSearch(c.ctx.services.noteServices.noteService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg)
  )

  val noteMutationType = ObjectType(
    name = "NoteMutations",
    description = "Mutations for Notes.",
    fields = fields(
      unitField(name = "create", desc = Some("Creates a new Note using the provided fields."), t = OptionType(noteType), f = (c, td) => {
        c.ctx.services.noteServices.noteService.create(c.ctx.creds, c.arg(DataFieldSchema.dataFieldsArg))(td)
      }, DataFieldSchema.dataFieldsArg),
      unitField(name = "update", desc = Some("Updates the Note with the provided id."), t = OptionType(noteType), f = (c, td) => {
        c.ctx.services.noteServices.noteService.update(c.ctx.creds, c.arg(noteIdArg), c.arg(DataFieldSchema.dataFieldsArg))(td).map(_._1)
      }, noteIdArg, DataFieldSchema.dataFieldsArg),
      unitField(name = "remove", desc = Some("Removes the Note with the provided id."), t = noteType, f = (c, td) => {
        c.ctx.services.noteServices.noteService.remove(c.ctx.creds, c.arg(noteIdArg))(td)
      }, noteIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "note", desc = None, t = noteMutationType, f = (c, td) => Future.successful(())))

  private[this] def toResult(r: SchemaHelper.SearchResult[Note]) = {
    NoteResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
