/* Generated File */
package models.graphql.note

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import java.util.UUID
import models.graphql.user.SystemUserSchema
import models.note.{NoteRow, NoteRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._
import services.note.NoteRowService

object NoteRowSchema extends GraphQLSchemaHelper("noteRow") {
  implicit val noteRowPrimaryKeyId: HasId[NoteRow, UUID] = HasId[NoteRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.injector.getInstance(classOf[NoteRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val noteRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val noteRowIdArg = Argument("id", uuidType)
  val noteRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val noteRowRelTypeArg = Argument("relType", StringType)
  val noteRowRelTypeSeqArg = Argument("relTypes", ListInputType(StringType))
  val noteRowRelPkArg = Argument("relPk", StringType)
  val noteRowRelPkSeqArg = Argument("relPks", ListInputType(StringType))
  val noteRowTextArg = Argument("text", StringType)
  val noteRowTextSeqArg = Argument("texts", ListInputType(StringType))
  val noteRowAuthorArg = Argument("author", uuidType)
  val noteRowAuthorSeqArg = Argument("authors", ListInputType(uuidType))
  val noteRowCreatedArg = Argument("created", localDateTimeType)
  val noteRowCreatedSeqArg = Argument("createds", ListInputType(localDateTimeType))

  val noteRowByAuthorRelation = Relation[NoteRow, UUID]("byAuthor", x => Seq(x.author))
  val noteRowByAuthorFetcher = Fetcher.rel[GraphQLContext, NoteRow, NoteRow, UUID](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[NoteRowService]).getByAuthorSeq(c.creds, rels(noteRowByAuthorRelation))(c.trace)
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
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.noteLookup(c.ctx.creds, "noteRow", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val noteRowResultType: sangria.schema.ObjectType[GraphQLContext, NoteRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "noteRow", desc = None, t = OptionType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByPrimaryKey(c.ctx.creds, c.arg(noteRowIdArg))(td)
    }, noteRowIdArg),
    unitField(name = "noteRowSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(noteRowIdSeqArg))(td)
    }, noteRowIdSeqArg),
    unitField(name = "noteRowSearch", desc = None, t = noteRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[NoteRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "notesByRelType", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByRelType(c.ctx.creds, c.arg(noteRowRelTypeArg))(td)
    }, noteRowRelTypeArg),
    unitField(name = "notesByRelTypeSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByRelTypeSeq(c.ctx.creds, c.arg(noteRowRelTypeSeqArg))(td)
    }, noteRowRelTypeSeqArg),
    unitField(name = "notesByRelPk", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByRelPk(c.ctx.creds, c.arg(noteRowRelPkArg))(td)
    }, noteRowRelPkArg),
    unitField(name = "notesByRelPkSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByRelPkSeq(c.ctx.creds, c.arg(noteRowRelPkSeqArg))(td)
    }, noteRowRelPkSeqArg),
    unitField(name = "notesByText", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByText(c.ctx.creds, c.arg(noteRowTextArg))(td)
    }, noteRowTextArg),
    unitField(name = "notesByTextSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByTextSeq(c.ctx.creds, c.arg(noteRowTextSeqArg))(td)
    }, noteRowTextSeqArg),
    unitField(name = "notesByAuthor", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByAuthor(c.ctx.creds, c.arg(noteRowAuthorArg))(td)
    }, noteRowAuthorArg),
    unitField(name = "notesByAuthorSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByAuthorSeq(c.ctx.creds, c.arg(noteRowAuthorSeqArg))(td)
    }, noteRowAuthorSeqArg),
    unitField(name = "notesByCreated", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByCreated(c.ctx.creds, c.arg(noteRowCreatedArg))(td)
    }, noteRowCreatedArg),
    unitField(name = "notesByCreatedSeq", desc = None, t = ListType(noteRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[NoteRowService]).getByCreatedSeq(c.ctx.creds, c.arg(noteRowCreatedSeqArg))(td)
    }, noteRowCreatedSeqArg)
  )

  val noteRowMutationType = ObjectType(
    name = "NoteRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(noteRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[NoteRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(noteRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[NoteRowService]).update(c.ctx.creds, c.arg(noteRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, noteRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = noteRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[NoteRowService]).remove(c.ctx.creds, c.arg(noteRowIdArg))(td)
      }, noteRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "noteRow", desc = None, t = noteRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[NoteRow]) = {
    NoteRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
