package models.note

import java.util.UUID
import models.graphql.{GraphQLContext, SchemaHelper}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.result.data.DataFieldSchema
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import models.user.UserSchema
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.macros.derive._
import sangria.schema._
import scala.concurrent.Future
import util.FutureUtils.graphQlContext

object NoteSchema extends SchemaHelper("note") {
  implicit val notePrimaryKeyId = HasId[Note, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    Future.successful(c.services.noteService.getByPrimaryKeySeq(idSeq)(c.trace))
  }
  val noteByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val noteIdArg = Argument("id", uuidType, description = "Returns the Note matching the provided Id.")

  val noteByAuthorRelation = Relation[Note, UUID]("byAuthor", x => Seq(x.author))
  val noteByAuthorFetcher = Fetcher.rel[GraphQLContext, Note, Note, UUID](
    getByPrimaryKeySeq, (c, rels) => Future.successful(c.services.noteService.getByAuthorSeq(rels(noteByAuthorRelation))(c.trace))
  )

  implicit lazy val noteType: ObjectType[GraphQLContext, Note] = deriveObjectType(
    AddFields(
      Field(
        name = "authorRel",
        fieldType = UserSchema.userType,
        resolve = ctx => UserSchema.userByPrimaryKeyFetcher.defer(ctx.value.author)
      )
    )
  )

  implicit lazy val noteResultType: ObjectType[GraphQLContext, NoteResult] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "note",
    fieldType = noteResultType,
    arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
    resolve = c => traceB(c.ctx, "search")(td => toResult(runSearch(c.ctx.services.noteService, c, td)))
  ))

  val noteMutationType = ObjectType(
    name = "note",
    description = "Mutations for Notes.",
    fields = fields[GraphQLContext, Unit](
      Field(
        name = "create",
        description = Some("Creates a new Note using the provided fields."),
        arguments = DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = OptionType(noteType),
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceB(c.ctx, "create")(tn => c.ctx.services.noteService.create(dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the Note with the provided id."),
        arguments = noteIdArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = noteType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceB(c.ctx, "update")(tn => c.ctx.services.noteService.update(c.args.arg(noteIdArg), dataFields)(tn))
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the Note with the provided id."),
        arguments = noteIdArg :: Nil,
        fieldType = noteType,
        resolve = c => traceB(c.ctx, "remove")(tn => c.ctx.services.noteService.remove(c.args.arg(noteIdArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "note", fieldType = noteMutationType, resolve = _ => ()))

  private[this] def toResult(r: SearchResult[Note]) = {
    NoteResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
