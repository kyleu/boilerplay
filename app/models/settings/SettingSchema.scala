/* Generated File */
package models.settings

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import models.note.NoteSchema
import models.settings.SettingKeySchema.settingKeyEnumType
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object SettingSchema extends GraphQLSchemaHelper("setting") {
  implicit val settingPrimaryKeyId: HasId[Setting, SettingKey] = HasId[Setting, SettingKey](_.k)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[SettingKey]) = {
    c.services.settingsServices.settingService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val settingByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val settingKArg = Argument("k", settingKeyEnumType)
  val settingKSeqArg = Argument("ks", ListInputType(settingKeyEnumType))

  implicit lazy val settingType: sangria.schema.ObjectType[GraphQLContext, Setting] = deriveObjectType()

  implicit lazy val settingResultType: sangria.schema.ObjectType[GraphQLContext, SettingResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "setting", desc = None, t = OptionType(settingType), f = (c, td) => {
      c.ctx.services.settingsServices.settingService.getByPrimaryKey(c.ctx.creds, c.arg(settingKArg))(td)
    }, settingKArg),
    unitField(name = "settingSeq", desc = None, t = ListType(settingType), f = (c, td) => {
      c.ctx.services.settingsServices.settingService.getByPrimaryKeySeq(c.ctx.creds, c.arg(settingKSeqArg))(td)
    }, settingKSeqArg),
    unitField(name = "settingSearch", desc = None, t = settingResultType, f = (c, td) => {
      runSearch(c.ctx.services.settingsServices.settingService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "settingByK", desc = None, t = OptionType(settingType), f = (c, td) => {
      c.ctx.services.settingsServices.settingService.getByK(c.ctx.creds, c.arg(settingKArg))(td).map(_.headOption)
    }, settingKArg),
    unitField(name = "settingsByKSeq", desc = None, t = ListType(settingType), f = (c, td) => {
      c.ctx.services.settingsServices.settingService.getByKSeq(c.ctx.creds, c.arg(settingKSeqArg))(td)
    }, settingKSeqArg)
  )

  val settingMutationType = ObjectType(
    name = "SettingMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(settingType), f = (c, td) => {
        c.ctx.services.settingsServices.settingService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(settingType), f = (c, td) => {
        c.ctx.services.settingsServices.settingService.update(c.ctx.creds, c.arg(settingKArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, settingKArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = settingType, f = (c, td) => {
        c.ctx.services.settingsServices.settingService.remove(c.ctx.creds, c.arg(settingKArg))(td)
      }, settingKArg)
    )
  )

  val mutationFields = fields(unitField(name = "setting", desc = None, t = settingMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[Setting]) = {
    SettingResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
