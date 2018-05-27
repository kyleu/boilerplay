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
import scala.concurrent.Future
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

  val queryFields = fields(
    unitField(name = "scheduledTaskRun", desc = Some("Retrieves a single Scheduled Task Run using its primary key."), t = OptionType(scheduledTaskRunType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunService.getByPrimaryKey(c.ctx.creds, c.args.arg(scheduledTaskRunIdArg))(td)
    }, scheduledTaskRunIdArg),
    unitField(name = "scheduledTaskRuns", desc = Some("Searches for Scheduled Task Runs using the provided arguments."), t = scheduledTaskRunResultType, f = (c, td) => {
      runSearch(c.ctx.services.taskServices.scheduledTaskRunService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg)
  )

  val scheduledTaskRunMutationType = ObjectType(
    name = "ScheduledTaskRunMutations",
    description = "Mutations for Scheduled Task Runs.",
    fields = fields(
      unitField(name = "create", desc = Some("Creates a new Scheduled Task Run using the provided fields."), t = OptionType(scheduledTaskRunType), f = (c, td) => {
        c.ctx.services.taskServices.scheduledTaskRunService.create(c.ctx.creds, c.args.arg(DataFieldSchema.dataFieldsArg))(td)
      }, DataFieldSchema.dataFieldsArg),
      unitField(name = "update", desc = Some("Updates the Scheduled Task Run with the provided id."), t = OptionType(scheduledTaskRunType), f = (c, td) => {
        c.ctx.services.taskServices.scheduledTaskRunService.update(c.ctx.creds, c.args.arg(scheduledTaskRunIdArg), c.args.arg(DataFieldSchema.dataFieldsArg))(td).map(_._1)
      }, scheduledTaskRunIdArg, DataFieldSchema.dataFieldsArg),
      unitField(name = "remove", desc = Some("Removes the Scheduled Task Run with the provided id."), t = scheduledTaskRunType, f = (c, td) => {
        c.ctx.services.taskServices.scheduledTaskRunService.remove(c.ctx.creds, c.args.arg(scheduledTaskRunIdArg))(td)
      }, scheduledTaskRunIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "scheduledTaskRun", desc = None, t = scheduledTaskRunMutationType, f = (c, td) => Future.successful(())))

  private[this] def toResult(r: SchemaHelper.SearchResult[ScheduledTaskRun]) = {
    ScheduledTaskRunResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
