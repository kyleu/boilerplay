/* Generated File */
package models.sync

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import models.note.NoteSchema
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object SyncProgressSchema extends GraphQLSchemaHelper("syncProgress") {
  implicit val syncProgressPrimaryKeyId: HasId[SyncProgress, String] = HasId[SyncProgress, String](_.key)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[String]) = {
    c.services.syncServices.syncProgressService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val syncProgressByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val syncProgressKeyArg = Argument("key", StringType)
  val syncProgressKeySeqArg = Argument("keys", ListInputType(StringType))

  val syncProgressStatusArg = Argument("status", StringType)
  val syncProgressStatusSeqArg = Argument("statuss", ListInputType(StringType))

  implicit lazy val syncProgressType: sangria.schema.ObjectType[GraphQLContext, SyncProgress] = deriveObjectType()

  implicit lazy val syncProgressResultType: sangria.schema.ObjectType[GraphQLContext, SyncProgressResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "syncProgress", desc = None, t = OptionType(syncProgressType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressService.getByPrimaryKey(c.ctx.creds, c.arg(syncProgressKeyArg))(td)
    }, syncProgressKeyArg),
    unitField(name = "syncProgressSeq", desc = None, t = ListType(syncProgressType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressService.getByPrimaryKeySeq(c.ctx.creds, c.arg(syncProgressKeySeqArg))(td)
    }, syncProgressKeySeqArg),
    unitField(name = "syncProgressSearch", desc = None, t = syncProgressResultType, f = (c, td) => {
      runSearch(c.ctx.services.syncServices.syncProgressService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "syncProgressByKey", desc = None, t = OptionType(syncProgressType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressService.getByKey(c.ctx.creds, c.arg(syncProgressKeyArg))(td).map(_.headOption)
    }, syncProgressKeyArg),
    unitField(name = "syncProgressByKeySeq", desc = None, t = ListType(syncProgressType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressService.getByKeySeq(c.ctx.creds, c.arg(syncProgressKeySeqArg))(td)
    }, syncProgressKeySeqArg),
    unitField(name = "syncProgressesByStatus", desc = None, t = ListType(syncProgressType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressService.getByStatus(c.ctx.creds, c.arg(syncProgressStatusArg))(td)
    }, syncProgressStatusArg),
    unitField(name = "syncProgressesByStatusSeq", desc = None, t = ListType(syncProgressType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressService.getByStatusSeq(c.ctx.creds, c.arg(syncProgressStatusSeqArg))(td)
    }, syncProgressStatusSeqArg)
  )

  val syncProgressMutationType = ObjectType(
    name = "SyncProgressMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(syncProgressType), f = (c, td) => {
        c.ctx.services.syncServices.syncProgressService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(syncProgressType), f = (c, td) => {
        c.ctx.services.syncServices.syncProgressService.update(c.ctx.creds, c.arg(syncProgressKeyArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, syncProgressKeyArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = syncProgressType, f = (c, td) => {
        c.ctx.services.syncServices.syncProgressService.remove(c.ctx.creds, c.arg(syncProgressKeyArg))(td)
      }, syncProgressKeyArg)
    )
  )

  val mutationFields = fields(unitField(name = "syncProgress", desc = None, t = syncProgressMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[SyncProgress]) = {
    SyncProgressResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
