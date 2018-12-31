/* Generated File */
package models.graphql.audit

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import java.util.UUID
import models.audit.{AuditRecordRow, AuditRecordRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._
import services.audit.AuditRecordRowService

object AuditRecordRowSchema extends GraphQLSchemaHelper("auditRecordRow") {
  implicit val auditRecordRowPrimaryKeyId: HasId[AuditRecordRow, UUID] = HasId[AuditRecordRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.injector.getInstance(classOf[AuditRecordRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val auditRecordRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val auditRecordRowIdArg = Argument("id", uuidType)
  val auditRecordRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val auditRecordRowTArg = Argument("t", StringType)
  val auditRecordRowTSeqArg = Argument("ts", ListInputType(StringType))
  val auditRecordRowPkArg = Argument("pk", ListInputType(StringType))
  val auditRecordRowPkSeqArg = Argument("pks", ListInputType(ListInputType(StringType)))

  val auditRecordRowByAuditIdRelation = Relation[AuditRecordRow, UUID]("byAuditId", x => Seq(x.auditId))
  val auditRecordRowByAuditIdFetcher = Fetcher.rel[GraphQLContext, AuditRecordRow, AuditRecordRow, UUID](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[AuditRecordRowService]).getByAuditIdSeq(c.creds, rels(auditRecordRowByAuditIdRelation))(c.trace)
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
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.noteLookup(c.ctx.creds, "auditRecordRow", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val auditRecordRowResultType: sangria.schema.ObjectType[GraphQLContext, AuditRecordRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "auditRecordRow", desc = None, t = OptionType(auditRecordRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRecordRowService]).getByPrimaryKey(c.ctx.creds, c.arg(auditRecordRowIdArg))(td)
    }, auditRecordRowIdArg),
    unitField(name = "auditRecordRowSeq", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRecordRowService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(auditRecordRowIdSeqArg))(td)
    }, auditRecordRowIdSeqArg),
    unitField(name = "auditRecordRowSearch", desc = None, t = auditRecordRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[AuditRecordRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "auditRecordsByT", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRecordRowService]).getByT(c.ctx.creds, c.arg(auditRecordRowTArg))(td)
    }, auditRecordRowTArg),
    unitField(name = "auditRecordsByTSeq", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRecordRowService]).getByTSeq(c.ctx.creds, c.arg(auditRecordRowTSeqArg))(td)
    }, auditRecordRowTSeqArg),
    unitField(name = "auditRecordsByPk", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRecordRowService]).getByPk(c.ctx.creds, c.arg(auditRecordRowPkArg).toList)(td)
    }, auditRecordRowPkArg),
    unitField(name = "auditRecordsByPkSeq", desc = None, t = ListType(auditRecordRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRecordRowService]).getByPkSeq(c.ctx.creds, c.arg(auditRecordRowPkSeqArg).map(_.toList))(td)
    }, auditRecordRowPkSeqArg)
  )

  val auditRecordRowMutationType = ObjectType(
    name = "AuditRecordRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(auditRecordRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[AuditRecordRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(auditRecordRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[AuditRecordRowService]).update(c.ctx.creds, c.arg(auditRecordRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, auditRecordRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = auditRecordRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[AuditRecordRowService]).remove(c.ctx.creds, c.arg(auditRecordRowIdArg))(td)
      }, auditRecordRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "auditRecordRow", desc = None, t = auditRecordRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[AuditRecordRow]) = {
    AuditRecordRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
