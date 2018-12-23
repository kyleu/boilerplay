/* Generated File */
package models.ddl

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import models.note.NoteRowSchema
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object FlywaySchemaHistoryRowSchema extends GraphQLSchemaHelper("flywaySchemaHistoryRow") {
  implicit val flywaySchemaHistoryRowPrimaryKeyId: HasId[FlywaySchemaHistoryRow, Long] = HasId[FlywaySchemaHistoryRow, Long](_.installedRank)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Long]) = {
    c.services.ddlServices.flywaySchemaHistoryRowService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val flywaySchemaHistoryRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val flywaySchemaHistoryRowInstalledRankArg = Argument("installedRank", LongType)
  val flywaySchemaHistoryRowInstalledRankSeqArg = Argument("installedRanks", ListInputType(LongType))

  val flywaySchemaHistoryRowSuccessArg = Argument("success", BooleanType)
  val flywaySchemaHistoryRowSuccessSeqArg = Argument("successs", ListInputType(BooleanType))

  implicit lazy val flywaySchemaHistoryRowType: sangria.schema.ObjectType[GraphQLContext, FlywaySchemaHistoryRow] = deriveObjectType()

  implicit lazy val flywaySchemaHistoryRowResultType: sangria.schema.ObjectType[GraphQLContext, FlywaySchemaHistoryRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "flywaySchemaHistoryRow", desc = None, t = OptionType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryRowService.getByPrimaryKey(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankArg))(td)
    }, flywaySchemaHistoryRowInstalledRankArg),
    unitField(name = "flywaySchemaHistoryRowSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryRowService.getByPrimaryKeySeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankSeqArg))(td)
    }, flywaySchemaHistoryRowInstalledRankSeqArg),
    unitField(name = "flywaySchemaHistoryRowSearch", desc = None, t = flywaySchemaHistoryRowResultType, f = (c, td) => {
      runSearch(c.ctx.services.ddlServices.flywaySchemaHistoryRowService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "flywaySchemaHistoryRowByInstalledRank", desc = None, t = OptionType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryRowService.getByInstalledRank(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankArg))(td).map(_.headOption)
    }, flywaySchemaHistoryRowInstalledRankArg),
    unitField(name = "flywaySchemaHistoriesByInstalledRankSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryRowService.getByInstalledRankSeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankSeqArg))(td)
    }, flywaySchemaHistoryRowInstalledRankSeqArg),
    unitField(name = "flywaySchemaHistoriesBySuccess", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryRowService.getBySuccess(c.ctx.creds, c.arg(flywaySchemaHistoryRowSuccessArg))(td)
    }, flywaySchemaHistoryRowSuccessArg),
    unitField(name = "flywaySchemaHistoriesBySuccessSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryRowService.getBySuccessSeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowSuccessSeqArg))(td)
    }, flywaySchemaHistoryRowSuccessSeqArg)
  )

  val flywaySchemaHistoryRowMutationType = ObjectType(
    name = "FlywaySchemaHistoryRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(flywaySchemaHistoryRowType), f = (c, td) => {
        c.ctx.services.ddlServices.flywaySchemaHistoryRowService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(flywaySchemaHistoryRowType), f = (c, td) => {
        c.ctx.services.ddlServices.flywaySchemaHistoryRowService.update(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, flywaySchemaHistoryRowInstalledRankArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = flywaySchemaHistoryRowType, f = (c, td) => {
        c.ctx.services.ddlServices.flywaySchemaHistoryRowService.remove(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankArg))(td)
      }, flywaySchemaHistoryRowInstalledRankArg)
    )
  )

  val mutationFields = fields(unitField(name = "flywaySchemaHistoryRow", desc = None, t = flywaySchemaHistoryRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[FlywaySchemaHistoryRow]) = {
    FlywaySchemaHistoryRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
