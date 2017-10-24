package models.audit

import java.util.UUID

import models.graphql.{GraphQLContext, SchemaHelper}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.audit.AuditRecordSchema._
import models.result.data.DataFieldSchema
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.macros.derive._
import sangria.schema._

import scala.concurrent.Future
import util.FutureUtils.graphQlContext

object AuditSchema extends SchemaHelper("audit") {
  implicit val auditPrimaryKeyId = HasId[Audit, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    Future.successful(c.app.auditService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace))
  }
  val auditByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val auditIdArg = Argument("id", uuidType, description = "Returns the Audit matching the provided Id.")

  val auditByUserIdRelation = Relation[Audit, UUID]("byUserId", x => Seq(x.id))
  val auditByUserIdFetcher = Fetcher.rel[GraphQLContext, Audit, Audit, UUID](
    getByPrimaryKeySeq, (c, rels) => Future.successful(c.app.auditService.getByUserIdSeq(c.creds, rels(auditByUserIdRelation))(c.trace))
  )

  implicit lazy val tagsType = ObjectType[GraphQLContext, Map[String, String]](name = "tags", fields = fields[GraphQLContext, Map[String, String]](Field(
    name = "entries",
    fieldType = ListType(StringType),
    resolve = c => traceB(c.ctx, "search")(td => c.value.map(c => c._1 + ":" + c._2).toList)
  )))

  implicit lazy val auditType: ObjectType[GraphQLContext, Audit] = deriveObjectType(
    AddFields(
      Field(
        name = "auditRecordAuditIdFkey",
        fieldType = ListType(AuditRecordSchema.auditRecordType),
        resolve = c => AuditRecordSchema.auditRecordByAuditIdFetcher.deferRelSeq(
          AuditRecordSchema.auditRecordByAuditIdRelation, c.value.id
        )
      )
    )
  )

  implicit lazy val auditResultType: ObjectType[GraphQLContext, AuditResult] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "audit",
    fieldType = auditResultType,
    arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
    resolve = c => traceB(c.ctx, "search")(td => toResult(runSearch(c.ctx.app.auditService, c, td)))
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
          traceB(c.ctx, "create")(tn => c.ctx.app.auditService.create(c.ctx.creds, dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the Audit with the provided id."),
        arguments = auditIdArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = auditType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceB(c.ctx, "update")(tn => c.ctx.app.auditService.update(c.ctx.creds, c.args.arg(auditIdArg), dataFields)(tn)._1)
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the Note with the provided id."),
        arguments = auditIdArg :: Nil,
        fieldType = auditType,
        resolve = c => traceB(c.ctx, "remove")(tn => c.ctx.app.auditService.remove(c.ctx.creds, c.args.arg(auditIdArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "audit", fieldType = auditMutationType, resolve = _ => ()))

  private[this] def toResult(r: SearchResult[Audit]) = {
    AuditResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
