/* Generated File */
package models.audit

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import java.util.UUID
import models.note.NoteRowSchema
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object AuditRecordRowSchema extends GraphQLSchemaHelper("auditRecordRow") {
  implicit val auditRecordRowPrimaryKeyId: HasId[AuditRecordRow, UUID] = HasId[AuditRecordRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.services.auditServices.auditRecordRowService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val auditRecordRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val auditRecordRowIdArg = Argument("id", uuidType)
  val auditRecordRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val auditRecordRowTArg = Argument("t", StringType)
  val auditRecordRowTSeqArg = Argument("ts", ListInputType(StringType))
  val auditRecordRowPkArg = Argument("pk", ListInputType(StringType))
  val auditRecordRowPkSeqArg = Argument("pks", ListInputType(ListInputType(StringType)))
  val auditRecordRowChangesArg = Argument("changes", StringType)
  val auditRecordRowChangesSeqArg = Argument("changess", ListInputType(StringType))

  val auditRecordRowByAuditIdRelation = Relation[AuditRecordRow, UUID]("byAuditId", x => Seq(x.auditId))
  val auditRecordRowByAuditIdFetcher = Fetcher.rel[GraphQLContext, AuditRecordRow, AuditRecordRow, UUID](
    getByPrimaryKeySeq, (c, rels) => c.services.auditServices.auditRecordRowService.getByAuditIdSeq(c.creds, rels(auditRecordRowByAuditIdRelation))(c.trace)
  )

  implicit lazy val auditRecordRowType: sangria.schema.ObjectType[GraphQLContext, AuditRecordRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "auditRecordAuditIdFkeyRel",
        fieldType = AuditRowSchema.auditRowType,
        resolve = ctx => AuditRowSchema.auditRowByPrimaryKeyFetcher.defer(ctx.value.auditId)
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteRowSchema.noteRowType),
        resolve = c => c.ctx.app.coreServices.notes.getFor(c.ctx.creds, "auditRecordRow", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val auditRecordRowResultType: sangria.schema.ObjectType[GraphQLContext, AuditRecordRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "auditRecordRow", desc = None, t = OptionType(auditRecordRowType), f = (c, td) => {
      c.ctx.services.auditServices.auditRecordRowService.getByPrimaryKey(c.ctx.creds, c.arg(auditRecordRowIdArg))(td)
    }, auditRecordRowIdArg),
    unitField(name = "auditRecordRowSeq", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.services.auditServices.auditRecordRowService.getByPrimaryKeySeq(c.ctx.creds, c.arg(auditRecordRowIdSeqArg))(td)
    }, auditRecordRowIdSeqArg),
    unitField(name = "auditRecordRowSearch", desc = None, t = auditRecordRowResultType, f = (c, td) => {
      runSearch(c.ctx.services.auditServices.auditRecordRowService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "auditRecordRowById", desc = None, t = OptionType(auditRecordRowType), f = (c, td) => {
      c.ctx.services.auditServices.auditRecordRowService.getById(c.ctx.creds, c.arg(auditRecordRowIdArg))(td).map(_.headOption)
    }, auditRecordRowIdArg),
    unitField(name = "auditRecordsByIdSeq", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.services.auditServices.auditRecordRowService.getByIdSeq(c.ctx.creds, c.arg(auditRecordRowIdSeqArg))(td)
    }, auditRecordRowIdSeqArg),
    unitField(name = "auditRecordsByT", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.services.auditServices.auditRecordRowService.getByT(c.ctx.creds, c.arg(auditRecordRowTArg))(td)
    }, auditRecordRowTArg),
    unitField(name = "auditRecordsByTSeq", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.services.auditServices.auditRecordRowService.getByTSeq(c.ctx.creds, c.arg(auditRecordRowTSeqArg))(td)
    }, auditRecordRowTSeqArg)
  )

  val auditRecordRowMutationType = ObjectType(
    name = "AuditRecordRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(auditRecordRowType), f = (c, td) => {
        c.ctx.services.auditServices.auditRecordRowService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(auditRecordRowType), f = (c, td) => {
        c.ctx.services.auditServices.auditRecordRowService.update(c.ctx.creds, c.arg(auditRecordRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, auditRecordRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = auditRecordRowType, f = (c, td) => {
        c.ctx.services.auditServices.auditRecordRowService.remove(c.ctx.creds, c.arg(auditRecordRowIdArg))(td)
      }, auditRecordRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "auditRecordRow", desc = None, t = auditRecordRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[AuditRecordRow]) = {
    AuditRecordRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
