/* Generated File */
package models.graphql.ddl

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.ddl.{FlywaySchemaHistoryRow, FlywaySchemaHistoryRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._
import services.ddl.FlywaySchemaHistoryRowService

object FlywaySchemaHistoryRowSchema extends GraphQLSchemaHelper("flywaySchemaHistoryRow") {
  implicit val flywaySchemaHistoryRowPrimaryKeyId: HasId[FlywaySchemaHistoryRow, Long] = HasId[FlywaySchemaHistoryRow, Long](_.installedRank)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Long]) = {
    c.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val flywaySchemaHistoryRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val flywaySchemaHistoryRowInstalledRankArg = Argument("installedRank", LongType)
  val flywaySchemaHistoryRowInstalledRankSeqArg = Argument("installedRanks", ListInputType(LongType))

  val flywaySchemaHistoryRowVersionArg = Argument("version", StringType)
  val flywaySchemaHistoryRowVersionSeqArg = Argument("versions", ListInputType(StringType))
  val flywaySchemaHistoryRowDescriptionArg = Argument("description", StringType)
  val flywaySchemaHistoryRowDescriptionSeqArg = Argument("descriptions", ListInputType(StringType))
  val flywaySchemaHistoryRowTypArg = Argument("typ", StringType)
  val flywaySchemaHistoryRowTypSeqArg = Argument("typs", ListInputType(StringType))
  val flywaySchemaHistoryRowInstalledOnArg = Argument("installedOn", localDateTimeType)
  val flywaySchemaHistoryRowInstalledOnSeqArg = Argument("installedOns", ListInputType(localDateTimeType))
  val flywaySchemaHistoryRowSuccessArg = Argument("success", BooleanType)
  val flywaySchemaHistoryRowSuccessSeqArg = Argument("successs", ListInputType(BooleanType))

  implicit lazy val flywaySchemaHistoryRowType: sangria.schema.ObjectType[GraphQLContext, FlywaySchemaHistoryRow] = deriveObjectType()

  implicit lazy val flywaySchemaHistoryRowResultType: sangria.schema.ObjectType[GraphQLContext, FlywaySchemaHistoryRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "flywaySchemaHistoryRow", desc = None, t = OptionType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByPrimaryKey(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankArg))(td)
    }, flywaySchemaHistoryRowInstalledRankArg),
    unitField(name = "flywaySchemaHistoryRowSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankSeqArg))(td)
    }, flywaySchemaHistoryRowInstalledRankSeqArg),
    unitField(name = "flywaySchemaHistoryRowSearch", desc = None, t = flywaySchemaHistoryRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "flywaySchemaHistoriesByVersion", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByVersion(c.ctx.creds, c.arg(flywaySchemaHistoryRowVersionArg))(td)
    }, flywaySchemaHistoryRowVersionArg),
    unitField(name = "flywaySchemaHistoriesByVersionSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByVersionSeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowVersionSeqArg))(td)
    }, flywaySchemaHistoryRowVersionSeqArg),
    unitField(name = "flywaySchemaHistoriesByDescription", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByDescription(c.ctx.creds, c.arg(flywaySchemaHistoryRowDescriptionArg))(td)
    }, flywaySchemaHistoryRowDescriptionArg),
    unitField(name = "flywaySchemaHistoriesByDescriptionSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByDescriptionSeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowDescriptionSeqArg))(td)
    }, flywaySchemaHistoryRowDescriptionSeqArg),
    unitField(name = "flywaySchemaHistoriesByTyp", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByTyp(c.ctx.creds, c.arg(flywaySchemaHistoryRowTypArg))(td)
    }, flywaySchemaHistoryRowTypArg),
    unitField(name = "flywaySchemaHistoriesByTypSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByTypSeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowTypSeqArg))(td)
    }, flywaySchemaHistoryRowTypSeqArg),
    unitField(name = "flywaySchemaHistoriesByInstalledOn", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByInstalledOn(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledOnArg))(td)
    }, flywaySchemaHistoryRowInstalledOnArg),
    unitField(name = "flywaySchemaHistoriesByInstalledOnSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getByInstalledOnSeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledOnSeqArg))(td)
    }, flywaySchemaHistoryRowInstalledOnSeqArg),
    unitField(name = "flywaySchemaHistoriesBySuccess", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getBySuccess(c.ctx.creds, c.arg(flywaySchemaHistoryRowSuccessArg))(td)
    }, flywaySchemaHistoryRowSuccessArg),
    unitField(name = "flywaySchemaHistoriesBySuccessSeq", desc = None, t = ListType(flywaySchemaHistoryRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).getBySuccessSeq(c.ctx.creds, c.arg(flywaySchemaHistoryRowSuccessSeqArg))(td)
    }, flywaySchemaHistoryRowSuccessSeqArg)
  )

  val flywaySchemaHistoryRowMutationType = ObjectType(
    name = "FlywaySchemaHistoryRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(flywaySchemaHistoryRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(flywaySchemaHistoryRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).update(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, flywaySchemaHistoryRowInstalledRankArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = flywaySchemaHistoryRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[FlywaySchemaHistoryRowService]).remove(c.ctx.creds, c.arg(flywaySchemaHistoryRowInstalledRankArg))(td)
      }, flywaySchemaHistoryRowInstalledRankArg)
    )
  )

  val mutationFields = fields(unitField(name = "flywaySchemaHistoryRow", desc = None, t = flywaySchemaHistoryRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[FlywaySchemaHistoryRow]) = {
    FlywaySchemaHistoryRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
