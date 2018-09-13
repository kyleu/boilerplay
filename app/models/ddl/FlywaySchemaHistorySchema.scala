/* Generated File */
package models.ddl

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import models.note.NoteSchema
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object FlywaySchemaHistorySchema extends GraphQLSchemaHelper("flywaySchemaHistory") {
  implicit val flywaySchemaHistoryPrimaryKeyId: HasId[FlywaySchemaHistory, Long] = HasId[FlywaySchemaHistory, Long](_.installedRank)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Long]) = {
    c.services.ddlServices.flywaySchemaHistoryService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val flywaySchemaHistoryByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val flywaySchemaHistoryInstalledRankArg = Argument("installedRank", LongType)
  val flywaySchemaHistoryInstalledRankSeqArg = Argument("installedRanks", ListInputType(LongType))

  val flywaySchemaHistorySuccessArg = Argument("success", BooleanType)
  val flywaySchemaHistorySuccessSeqArg = Argument("successs", ListInputType(BooleanType))

  implicit lazy val flywaySchemaHistoryType: sangria.schema.ObjectType[GraphQLContext, FlywaySchemaHistory] = deriveObjectType()

  implicit lazy val flywaySchemaHistoryResultType: sangria.schema.ObjectType[GraphQLContext, FlywaySchemaHistoryResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "flywaySchemaHistory", desc = None, t = OptionType(flywaySchemaHistoryType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryService.getByPrimaryKey(c.ctx.creds, c.arg(flywaySchemaHistoryInstalledRankArg))(td)
    }, flywaySchemaHistoryInstalledRankArg),
    unitField(name = "flywaySchemaHistorySeq", desc = None, t = ListType(flywaySchemaHistoryType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryService.getByPrimaryKeySeq(c.ctx.creds, c.arg(flywaySchemaHistoryInstalledRankSeqArg))(td)
    }, flywaySchemaHistoryInstalledRankSeqArg),
    unitField(name = "flywaySchemaHistorySearch", desc = None, t = flywaySchemaHistoryResultType, f = (c, td) => {
      runSearch(c.ctx.services.ddlServices.flywaySchemaHistoryService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "flywaySchemaHistoryByInstalledRank", desc = None, t = OptionType(flywaySchemaHistoryType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryService.getByInstalledRank(c.ctx.creds, c.arg(flywaySchemaHistoryInstalledRankArg))(td).map(_.headOption)
    }, flywaySchemaHistoryInstalledRankArg),
    unitField(name = "flywaySchemaHistoryByInstalledRankSeq", desc = None, t = ListType(flywaySchemaHistoryType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryService.getByInstalledRankSeq(c.ctx.creds, c.arg(flywaySchemaHistoryInstalledRankSeqArg))(td)
    }, flywaySchemaHistoryInstalledRankSeqArg),
    unitField(name = "flywaySchemaHistoriesBySuccess", desc = None, t = ListType(flywaySchemaHistoryType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryService.getBySuccess(c.ctx.creds, c.arg(flywaySchemaHistorySuccessArg))(td)
    }, flywaySchemaHistorySuccessArg),
    unitField(name = "flywaySchemaHistoriesBySuccessSeq", desc = None, t = ListType(flywaySchemaHistoryType), f = (c, td) => {
      c.ctx.services.ddlServices.flywaySchemaHistoryService.getBySuccessSeq(c.ctx.creds, c.arg(flywaySchemaHistorySuccessSeqArg))(td)
    }, flywaySchemaHistorySuccessSeqArg)
  )

  val flywaySchemaHistoryMutationType = ObjectType(
    name = "FlywaySchemaHistoryMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(flywaySchemaHistoryType), f = (c, td) => {
        c.ctx.services.ddlServices.flywaySchemaHistoryService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(flywaySchemaHistoryType), f = (c, td) => {
        c.ctx.services.ddlServices.flywaySchemaHistoryService.update(c.ctx.creds, c.arg(flywaySchemaHistoryInstalledRankArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, flywaySchemaHistoryInstalledRankArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = flywaySchemaHistoryType, f = (c, td) => {
        c.ctx.services.ddlServices.flywaySchemaHistoryService.remove(c.ctx.creds, c.arg(flywaySchemaHistoryInstalledRankArg))(td)
      }, flywaySchemaHistoryInstalledRankArg)
    )
  )

  val mutationFields = fields(unitField(name = "flywaySchemaHistory", desc = None, t = flywaySchemaHistoryMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[FlywaySchemaHistory]) = {
    FlywaySchemaHistoryResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
