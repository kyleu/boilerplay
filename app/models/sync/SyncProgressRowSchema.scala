/* Generated File */
package models.sync

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import models.note.NoteRowSchema
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object SyncProgressRowSchema extends GraphQLSchemaHelper("syncProgressRow") {
  implicit val syncProgressRowPrimaryKeyId: HasId[SyncProgressRow, String] = HasId[SyncProgressRow, String](_.key)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[String]) = {
    c.services.syncServices.syncProgressRowService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val syncProgressRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val syncProgressRowKeyArg = Argument("key", StringType)
  val syncProgressRowKeySeqArg = Argument("keys", ListInputType(StringType))

  val syncProgressRowStatusArg = Argument("status", StringType)
  val syncProgressRowStatusSeqArg = Argument("statuss", ListInputType(StringType))

  implicit lazy val syncProgressRowType: sangria.schema.ObjectType[GraphQLContext, SyncProgressRow] = deriveObjectType()

  implicit lazy val syncProgressRowResultType: sangria.schema.ObjectType[GraphQLContext, SyncProgressRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "syncProgressRow", desc = None, t = OptionType(syncProgressRowType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressRowService.getByPrimaryKey(c.ctx.creds, c.arg(syncProgressRowKeyArg))(td)
    }, syncProgressRowKeyArg),
    unitField(name = "syncProgressRowSeq", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressRowService.getByPrimaryKeySeq(c.ctx.creds, c.arg(syncProgressRowKeySeqArg))(td)
    }, syncProgressRowKeySeqArg),
    unitField(name = "syncProgressRowSearch", desc = None, t = syncProgressRowResultType, f = (c, td) => {
      runSearch(c.ctx.services.syncServices.syncProgressRowService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "syncProgressRowByKey", desc = None, t = OptionType(syncProgressRowType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressRowService.getByKey(c.ctx.creds, c.arg(syncProgressRowKeyArg))(td).map(_.headOption)
    }, syncProgressRowKeyArg),
    unitField(name = "syncProgressesByKeySeq", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressRowService.getByKeySeq(c.ctx.creds, c.arg(syncProgressRowKeySeqArg))(td)
    }, syncProgressRowKeySeqArg),
    unitField(name = "syncProgressesByStatus", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressRowService.getByStatus(c.ctx.creds, c.arg(syncProgressRowStatusArg))(td)
    }, syncProgressRowStatusArg),
    unitField(name = "syncProgressesByStatusSeq", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressRowService.getByStatusSeq(c.ctx.creds, c.arg(syncProgressRowStatusSeqArg))(td)
    }, syncProgressRowStatusSeqArg)
  )

  val syncProgressRowMutationType = ObjectType(
    name = "SyncProgressRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(syncProgressRowType), f = (c, td) => {
        c.ctx.services.syncServices.syncProgressRowService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(syncProgressRowType), f = (c, td) => {
        c.ctx.services.syncServices.syncProgressRowService.update(c.ctx.creds, c.arg(syncProgressRowKeyArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, syncProgressRowKeyArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = syncProgressRowType, f = (c, td) => {
        c.ctx.services.syncServices.syncProgressRowService.remove(c.ctx.creds, c.arg(syncProgressRowKeyArg))(td)
      }, syncProgressRowKeyArg)
    )
  )

  val mutationFields = fields(unitField(name = "syncProgressRow", desc = None, t = syncProgressRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[SyncProgressRow]) = {
    SyncProgressRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
