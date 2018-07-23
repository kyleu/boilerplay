package models.audit

import java.util.UUID

import graphql.GraphQLUtils._
import graphql.{GraphQLContext, GraphQLSchemaHelper}
import models.note.NoteSchema
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.macros.derive.{AddFields, deriveObjectType}
import sangria.schema._

import scala.concurrent.Future

object AuditRecordSchema extends GraphQLSchemaHelper("auditRecord") {
  implicit val auditRecordPrimaryKeyId: HasId[AuditRecord, UUID] = HasId[AuditRecord, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.services.auditServices.auditRecordService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val auditRecordByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val auditRecordIdArg = Argument("id", uuidType, description = "Returns the Audit Record matching the provided Id.")

  val auditRecordByAuditIdRelation = Relation[AuditRecord, UUID]("byAuditId", x => Seq(x.auditId))
  val auditRecordByAuditIdFetcher = Fetcher.rel[GraphQLContext, AuditRecord, AuditRecord, UUID](
    getByPrimaryKeySeq, (c, rels) => c.services.auditServices.auditRecordService.getByAuditIdSeq(c.creds, rels(auditRecordByAuditIdRelation))(c.trace)
  )

  implicit lazy val auditFieldType: ObjectType[GraphQLContext, AuditField] = deriveObjectType()

  implicit lazy val auditRecordType: ObjectType[GraphQLContext, AuditRecord] = deriveObjectType(
    AddFields(
      Field(
        name = "auditIdRel",
        fieldType = AuditSchema.auditType,
        resolve = ctx => AuditSchema.auditByPrimaryKeyFetcher.defer(ctx.value.auditId)
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.app.coreServices.notes.getFor(c.ctx.creds, "auditRecord", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val auditRecordResultType: ObjectType[GraphQLContext, AuditRecordResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "auditRecord", desc = None, t = OptionType(auditRecordType), f = (c, td) => {
      c.ctx.services.auditServices.auditRecordService.getByPrimaryKey(c.ctx.creds, c.arg(auditRecordIdArg))(td)
    }, auditRecordIdArg),
    unitField(name = "auditRecords", desc = None, t = auditRecordResultType, f = (c, td) => {
      runSearch(c.ctx.services.auditServices.auditRecordService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg)
  )

  val auditRecordMutationType = ObjectType(
    name = "auditRecord",
    description = "Mutations for Audit Records.",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(auditRecordType), f = (c, td) => {
        c.ctx.services.auditServices.auditRecordService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(auditRecordType), f = (c, td) => {
        c.ctx.services.auditServices.auditRecordService.update(c.ctx.creds, c.arg(auditRecordIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, auditRecordIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = auditRecordType, f = (c, td) => {
        c.ctx.services.auditServices.auditRecordService.remove(c.ctx.creds, c.arg(auditRecordIdArg))(td)
      }, auditRecordIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "auditRecord", desc = None, t = auditRecordMutationType, f = (_, _) => Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[AuditRecord]) = {
    AuditRecordResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
