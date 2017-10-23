package models.audit

import java.util.UUID
import models.graphql.{GraphQLContext, SchemaHelper}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.result.data.DataFieldSchema
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.macros.derive._
import sangria.schema._
import scala.concurrent.Future
import util.FutureUtils.graphQlContext

object AuditRecordSchema extends SchemaHelper("auditRecord") {
  implicit val auditRecordPrimaryKeyId = HasId[AuditRecord, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    Future.successful(c.app.auditRecordService.getByPrimaryKeySeq(idSeq)(c.trace))
  }
  val auditRecordByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val auditRecordIdArg = Argument("id", uuidType, description = "Returns the Audit Record matching the provided Id.")

  val auditRecordByAuditIdRelation = Relation[AuditRecord, UUID]("byAuditId", x => Seq(x.auditId))
  val auditRecordByAuditIdFetcher = Fetcher.rel[GraphQLContext, AuditRecord, AuditRecord, UUID](
    getByPrimaryKeySeq, (c, rels) => Future.successful(c.app.auditRecordService.getByAuditIdSeq(rels(auditRecordByAuditIdRelation))(c.trace))
  )

  implicit lazy val auditFieldType: ObjectType[GraphQLContext, AuditField] = deriveObjectType()

  implicit lazy val auditRecordType: ObjectType[GraphQLContext, AuditRecord] = deriveObjectType(
    AddFields(
      Field(
        name = "auditIdRel",
        fieldType = AuditSchema.auditType,
        resolve = ctx => AuditSchema.auditByPrimaryKeyFetcher.defer(ctx.value.auditId)
      )
    )
  )

  implicit lazy val auditRecordResultType: ObjectType[GraphQLContext, AuditRecordResult] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "auditRecord",
    fieldType = auditRecordResultType,
    arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
    resolve = c => traceB(c.ctx, "search")(td => toResult(runSearch(c.ctx.services.auditServices.auditRecordService, c, td)))
  ))

  val auditRecordMutationType = ObjectType(
    name = "auditRecord",
    description = "Mutations for Audit Records.",
    fields = fields[GraphQLContext, Unit](
      Field(
        name = "create",
        description = Some("Creates a new Audit Record using the provided fields."),
        arguments = DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = OptionType(auditRecordType),
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceB(c.ctx, "create")(tn => c.ctx.services.auditServices.auditRecordService.create(dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the Audit Record with the provided id."),
        arguments = auditRecordIdArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = auditRecordType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceB(c.ctx, "update")(tn => c.ctx.services.auditServices.auditRecordService.update(c.args.arg(auditRecordIdArg), dataFields)(tn)._1)
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the Audit Record with the provided id."),
        arguments = auditRecordIdArg :: Nil,
        fieldType = auditRecordType,
        resolve = c => traceB(c.ctx, "remove")(tn => c.ctx.services.auditServices.auditRecordService.remove(c.args.arg(auditRecordIdArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "auditRecord", fieldType = auditRecordMutationType, resolve = _ => ()))

  private[this] def toResult(r: SearchResult[AuditRecord]) = {
    AuditRecordResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
