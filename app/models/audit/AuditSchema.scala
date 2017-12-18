package models.audit

import java.util.UUID

import models.graphql.{GraphQLContext, SchemaHelper}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.audit.AuditRecordSchema._
import models.note.NoteSchema
import models.result.data.DataFieldSchema
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.macros.derive._
import sangria.schema._
import util.FutureUtils.graphQlContext

object AuditSchema extends SchemaHelper("audit") {
  implicit val auditPrimaryKeyId: HasId[Audit, UUID] = HasId[Audit, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = c.app.coreServices.audits.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val auditByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val auditIdArg = Argument("id", uuidType, description = "Returns the Audit matching the provided Id.")

  val auditByUserIdRelation = Relation[Audit, UUID]("byUserId", x => Seq(x.id))
  val auditByUserIdFetcher = Fetcher.rel[GraphQLContext, Audit, Audit, UUID](
    getByPrimaryKeySeq, (c, rels) => c.app.coreServices.audits.getByUserIdSeq(c.creds, rels(auditByUserIdRelation))(c.trace)
  )

  implicit lazy val auditType: ObjectType[GraphQLContext, Audit] = deriveObjectType(
    AddFields(
      Field(
        name = "auditRecordAuditIdFkey",
        fieldType = ListType(AuditRecordSchema.auditRecordType),
        resolve = c => AuditRecordSchema.auditRecordByAuditIdFetcher.deferRelSeq(
          AuditRecordSchema.auditRecordByAuditIdRelation, c.value.id
        )
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.app.coreServices.notes.getFor(c.ctx.creds, "audit", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val auditResultType: ObjectType[GraphQLContext, AuditResult] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "audit",
    fieldType = auditResultType,
    arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
    resolve = c => traceF(c.ctx, "search")(td => runSearch(c.ctx.app.coreServices.audits, c, td).map(toResult))
  ))

  val auditMutationType = ObjectType(
    name = "audit",
    description = "Mutations for Audits.",
    fields = fields[GraphQLContext, Unit](
      Field(
        name = "create",
        description = Some("Creates a new Audit using the provided fields."),
        arguments = DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = OptionType(auditType),
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "create")(tn => c.ctx.app.coreServices.audits.create(c.ctx.creds, dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the Audit with the provided id."),
        arguments = auditIdArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = auditType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "update")(tn => c.ctx.app.coreServices.audits.update(c.ctx.creds, c.args.arg(auditIdArg), dataFields)(tn).map(_._1))
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the Note with the provided id."),
        arguments = auditIdArg :: Nil,
        fieldType = auditType,
        resolve = c => traceF(c.ctx, "remove")(tn => c.ctx.app.coreServices.audits.remove(c.ctx.creds, c.args.arg(auditIdArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "audit", fieldType = auditMutationType, resolve = _ => ()))

  private[this] def toResult(r: SearchResult[Audit]) = {
    AuditResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
