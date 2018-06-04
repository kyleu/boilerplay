/* Generated File */
package models.sync

import models.graphql.{GraphQLContext, SchemaHelper}
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.note.NoteSchema
import models.result.data.DataFieldSchema
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.macros.derive._
import sangria.schema._
import scala.concurrent.Future
import util.FutureUtils.graphQlContext

object SyncProgressSchema extends SchemaHelper("syncProgress") {
  implicit val syncProgressPrimaryKeyId: HasId[SyncProgress, String] = HasId[SyncProgress, String](_.key)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[String]) = {
    c.services.syncServices.syncProgressService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val syncProgressByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val syncProgressKeyArg = Argument("key", StringType, description = "Returns the Sync Progress matching the provided Key.")

  implicit lazy val syncProgressType: ObjectType[GraphQLContext, SyncProgress] = deriveObjectType()

  implicit lazy val syncProgressResultType: ObjectType[GraphQLContext, SyncProgressResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "syncProgress", desc = Some("Retrieves a single Sync Progress using its primary key."), t = OptionType(syncProgressType), f = (c, td) => {
      c.ctx.services.syncServices.syncProgressService.getByPrimaryKey(c.ctx.creds, c.arg(syncProgressKeyArg))(td)
    }, syncProgressKeyArg),
    unitField(name = "syncProgresses", desc = Some("Searches for Sync Progresses using the provided arguments."), t = syncProgressResultType, f = (c, td) => {
      runSearch(c.ctx.services.syncServices.syncProgressService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg)
  )

  val syncProgressMutationType = ObjectType(
    name = "SyncProgressMutations",
    description = "Mutations for Sync Progresses.",
    fields = fields(
      unitField(name = "create", desc = Some("Creates a new Sync Progress using the provided fields."), t = OptionType(syncProgressType), f = (c, td) => {
        c.ctx.services.syncServices.syncProgressService.create(c.ctx.creds, c.arg(DataFieldSchema.dataFieldsArg))(td)
      }, DataFieldSchema.dataFieldsArg),
      unitField(name = "update", desc = Some("Updates the Sync Progress with the provided key."), t = OptionType(syncProgressType), f = (c, td) => {
        c.ctx.services.syncServices.syncProgressService.update(c.ctx.creds, c.arg(syncProgressKeyArg), c.arg(DataFieldSchema.dataFieldsArg))(td).map(_._1)
      }, syncProgressKeyArg, DataFieldSchema.dataFieldsArg),
      unitField(name = "remove", desc = Some("Removes the Sync Progress with the provided id."), t = syncProgressType, f = (c, td) => {
        c.ctx.services.syncServices.syncProgressService.remove(c.ctx.creds, c.arg(syncProgressKeyArg))(td)
      }, syncProgressKeyArg)
    )
  )

  val mutationFields = fields(unitField(name = "syncProgress", desc = None, t = syncProgressMutationType, f = (c, td) => Future.successful(())))

  private[this] def toResult(r: SchemaHelper.SearchResult[SyncProgress]) = {
    SyncProgressResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
