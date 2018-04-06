/* Generated File */
package models.task

import java.util.UUID
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

object ScheduledTaskRunSchema extends SchemaHelper("scheduledTaskRun") {
  implicit val scheduledTaskRunPrimaryKeyId: HasId[ScheduledTaskRun, UUID] = HasId[ScheduledTaskRun, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.services.taskServices.scheduledTaskRunService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val scheduledTaskRunByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val scheduledTaskRunIdArg = Argument("id", uuidType, description = "Returns the Scheduled Task Run matching the provided Id.")

  implicit lazy val scheduledTaskRunType: ObjectType[GraphQLContext, ScheduledTaskRun] = deriveObjectType()

  implicit lazy val scheduledTaskRunResultType: ObjectType[GraphQLContext, ScheduledTaskRunResult] = deriveObjectType()

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "scheduledTaskRun",
    fieldType = scheduledTaskRunResultType,
    arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
    resolve = c => traceF(c.ctx, "search")(td => runSearch(c.ctx.services.taskServices.scheduledTaskRunService, c, td).map(toResult))
  ))

  val scheduledTaskRunMutationType = ObjectType(
    name = "ScheduledTaskRunMutations",
    description = "Mutations for Scheduled Task Runs.",
    fields = fields[GraphQLContext, Unit](
      Field(
        name = "create",
        description = Some("Creates a new Scheduled Task Run using the provided fields."),
        arguments = DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = OptionType(scheduledTaskRunType),
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "create")(tn => c.ctx.services.taskServices.scheduledTaskRunService.create(c.ctx.creds, dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the Scheduled Task Run with the provided id."),
        arguments = scheduledTaskRunIdArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = scheduledTaskRunType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "update")(tn => c.ctx.services.taskServices.scheduledTaskRunService.update(c.ctx.creds, c.args.arg(scheduledTaskRunIdArg), dataFields)(tn).map(_._1))
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the Scheduled Task Run with the provided id."),
        arguments = scheduledTaskRunIdArg :: Nil,
        fieldType = scheduledTaskRunType,
        resolve = c => traceF(c.ctx, "remove")(tn => c.ctx.services.taskServices.scheduledTaskRunService.remove(c.ctx.creds, c.args.arg(scheduledTaskRunIdArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "scheduledTaskRun", fieldType = scheduledTaskRunMutationType, resolve = _ => ()))

  private[this] def toResult(r: SchemaHelper.SearchResult[ScheduledTaskRun]) = {
    ScheduledTaskRunResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
