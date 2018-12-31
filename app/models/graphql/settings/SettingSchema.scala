/* Generated File */
package models.graphql.settings

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.graphql.settings.SettingKeyTypeSchema.settingKeyTypeEnumType
import models.settings.{Setting, SettingKeyType, SettingResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._
import services.settings.SettingService

object SettingSchema extends GraphQLSchemaHelper("setting") {
  implicit val settingPrimaryKeyId: HasId[Setting, SettingKeyType] = HasId[Setting, SettingKeyType](_.k)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[SettingKeyType]) = {
    c.injector.getInstance(classOf[SettingService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val settingByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val settingKArg = Argument("k", settingKeyTypeEnumType)
  val settingKSeqArg = Argument("ks", ListInputType(settingKeyTypeEnumType))

  implicit lazy val settingType: sangria.schema.ObjectType[GraphQLContext, Setting] = deriveObjectType()

  implicit lazy val settingResultType: sangria.schema.ObjectType[GraphQLContext, SettingResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "setting", desc = None, t = OptionType(settingType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[SettingService]).getByPrimaryKey(c.ctx.creds, c.arg(settingKArg))(td)
    }, settingKArg),
    unitField(name = "settingSeq", desc = None, t = ListType(settingType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[SettingService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(settingKSeqArg))(td)
    }, settingKSeqArg),
    unitField(name = "settingSearch", desc = None, t = settingResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[SettingService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg)
  )

  val settingMutationType = ObjectType(
    name = "SettingMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(settingType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[SettingService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(settingType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[SettingService]).update(c.ctx.creds, c.arg(settingKArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, settingKArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = settingType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[SettingService]).remove(c.ctx.creds, c.arg(settingKArg))(td)
      }, settingKArg)
    )
  )

  val mutationFields = fields(unitField(name = "setting", desc = None, t = settingMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[Setting]) = {
    SettingResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
