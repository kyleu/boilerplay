/* Generated File */
package models.graphql.sync

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.sync.{SyncProgressRow, SyncProgressRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object SyncProgressRowSchema extends GraphQLSchemaHelper("syncProgressRow") {
  implicit val syncProgressRowPrimaryKeyId: HasId[SyncProgressRow, String] = HasId[SyncProgressRow, String](_.key)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[String]) = {
    c.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val syncProgressRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val syncProgressRowKeyArg = Argument("key", StringType)
  val syncProgressRowKeySeqArg = Argument("keys", ListInputType(StringType))

  val syncProgressRowStatusArg = Argument("status", StringType)
  val syncProgressRowStatusSeqArg = Argument("statuss", ListInputType(StringType))
  val syncProgressRowMessageArg = Argument("message", StringType)
  val syncProgressRowMessageSeqArg = Argument("messages", ListInputType(StringType))
  val syncProgressRowLastTimeArg = Argument("lastTime", localDateTimeType)
  val syncProgressRowLastTimeSeqArg = Argument("lastTimes", ListInputType(localDateTimeType))

  implicit lazy val syncProgressRowType: sangria.schema.ObjectType[GraphQLContext, SyncProgressRow] = deriveObjectType()

  implicit lazy val syncProgressRowResultType: sangria.schema.ObjectType[GraphQLContext, SyncProgressRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "syncProgressRow", desc = None, t = OptionType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByPrimaryKey(c.ctx.creds, c.arg(syncProgressRowKeyArg))(td)
    }, syncProgressRowKeyArg),
    unitField(name = "syncProgressRowSeq", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(syncProgressRowKeySeqArg))(td)
    }, syncProgressRowKeySeqArg),
    unitField(name = "syncProgressRowSearch", desc = None, t = syncProgressRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "syncProgressesByStatus", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByStatus(c.ctx.creds, c.arg(syncProgressRowStatusArg))(td)
    }, syncProgressRowStatusArg),
    unitField(name = "syncProgressesByStatusSeq", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByStatusSeq(c.ctx.creds, c.arg(syncProgressRowStatusSeqArg))(td)
    }, syncProgressRowStatusSeqArg),
    unitField(name = "syncProgressesByMessage", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByMessage(c.ctx.creds, c.arg(syncProgressRowMessageArg))(td)
    }, syncProgressRowMessageArg),
    unitField(name = "syncProgressesByMessageSeq", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByMessageSeq(c.ctx.creds, c.arg(syncProgressRowMessageSeqArg))(td)
    }, syncProgressRowMessageSeqArg),
    unitField(name = "syncProgressesByLastTime", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByLastTime(c.ctx.creds, c.arg(syncProgressRowLastTimeArg))(td)
    }, syncProgressRowLastTimeArg),
    unitField(name = "syncProgressesByLastTimeSeq", desc = None, t = ListType(syncProgressRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).getByLastTimeSeq(c.ctx.creds, c.arg(syncProgressRowLastTimeSeqArg))(td)
    }, syncProgressRowLastTimeSeqArg)
  )

  val syncProgressRowMutationType = ObjectType(
    name = "SyncProgressRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(syncProgressRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(syncProgressRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).update(c.ctx.creds, c.arg(syncProgressRowKeyArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, syncProgressRowKeyArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = syncProgressRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.sync.SyncProgressRowService]).remove(c.ctx.creds, c.arg(syncProgressRowKeyArg))(td)
      }, syncProgressRowKeyArg)
    )
  )

  val mutationFields = fields(unitField(name = "syncProgressRow", desc = None, t = syncProgressRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[SyncProgressRow]) = {
    SyncProgressRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
