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

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "syncProgress",
    fieldType = syncProgressResultType,
    arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
    resolve = c => traceF(c.ctx, "search")(td => runSearch(c.ctx.services.syncServices.syncProgressService, c, td).map(toResult))
  ))

  val syncProgressMutationType = ObjectType(
    name = "SyncProgressMutations",
    description = "Mutations for Sync Progresses.",
    fields = fields[GraphQLContext, Unit](
      Field(
        name = "create",
        description = Some("Creates a new Sync Progress using the provided fields."),
        arguments = DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = OptionType(syncProgressType),
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "create")(tn => c.ctx.services.syncServices.syncProgressService.create(c.ctx.creds, dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the Sync Progress with the provided key."),
        arguments = syncProgressKeyArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = syncProgressType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "update")(tn => c.ctx.services.syncServices.syncProgressService.update(c.ctx.creds, c.args.arg(syncProgressKeyArg), dataFields)(tn).map(_._1))
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the Sync Progress with the provided id."),
        arguments = syncProgressKeyArg :: Nil,
        fieldType = syncProgressType,
        resolve = c => traceF(c.ctx, "remove")(tn => c.ctx.services.syncServices.syncProgressService.remove(c.ctx.creds, c.args.arg(syncProgressKeyArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "syncProgress", fieldType = syncProgressMutationType, resolve = _ => ()))

  private[this] def toResult(r: SchemaHelper.SearchResult[SyncProgress]) = {
    SyncProgressResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
