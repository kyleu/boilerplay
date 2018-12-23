/* Generated File */
package models.note

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import java.util.UUID
import models.user.SystemUserSchema
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object NoteRowSchema extends GraphQLSchemaHelper("noteRow") {
  implicit val noteRowPrimaryKeyId: HasId[NoteRow, UUID] = HasId[NoteRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.services.noteServices.noteRowService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val noteRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val noteRowIdArg = Argument("id", uuidType)
  val noteRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val noteRowByAuthorRelation = Relation[NoteRow, UUID]("byAuthor", x => Seq(x.author))
  val noteRowByAuthorFetcher = Fetcher.rel[GraphQLContext, NoteRow, NoteRow, UUID](
    getByPrimaryKeySeq, (c, rels) => c.services.noteServices.noteRowService.getByAuthorSeq(c.creds, rels(noteRowByAuthorRelation))(c.trace)
  )

  implicit lazy val noteRowType: sangria.schema.ObjectType[GraphQLContext, NoteRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "authorRel",
        fieldType = SystemUserSchema.systemUserType,
        resolve = ctx => SystemUserSchema.systemUserByPrimaryKeyFetcher.defer(ctx.value.author)
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteRowSchema.noteRowType),
        resolve = c => c.ctx.app.coreServices.notes.getFor(c.ctx.creds, "noteRow", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val noteRowResultType: sangria.schema.ObjectType[GraphQLContext, NoteRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "noteRow", desc = None, t = OptionType(noteRowType), f = (c, td) => {
      c.ctx.services.noteServices.noteRowService.getByPrimaryKey(c.ctx.creds, c.arg(noteRowIdArg))(td)
    }, noteRowIdArg),
    unitField(name = "noteRowSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.services.noteServices.noteRowService.getByPrimaryKeySeq(c.ctx.creds, c.arg(noteRowIdSeqArg))(td)
    }, noteRowIdSeqArg),
    unitField(name = "noteRowSearch", desc = None, t = noteRowResultType, f = (c, td) => {
      runSearch(c.ctx.services.noteServices.noteRowService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "noteRowById", desc = None, t = OptionType(noteRowType), f = (c, td) => {
      c.ctx.services.noteServices.noteRowService.getById(c.ctx.creds, c.arg(noteRowIdArg))(td).map(_.headOption)
    }, noteRowIdArg),
    unitField(name = "notesByIdSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.services.noteServices.noteRowService.getByIdSeq(c.ctx.creds, c.arg(noteRowIdSeqArg))(td)
    }, noteRowIdSeqArg)
  )

  val noteRowMutationType = ObjectType(
    name = "NoteRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(noteRowType), f = (c, td) => {
        c.ctx.services.noteServices.noteRowService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(noteRowType), f = (c, td) => {
        c.ctx.services.noteServices.noteRowService.update(c.ctx.creds, c.arg(noteRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, noteRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = noteRowType, f = (c, td) => {
        c.ctx.services.noteServices.noteRowService.remove(c.ctx.creds, c.arg(noteRowIdArg))(td)
      }, noteRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "noteRow", desc = None, t = noteRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[NoteRow]) = {
    NoteRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
