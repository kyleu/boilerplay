/* Generated File */
package models.task

import graphql.{GraphQLContext, GraphQLSchemaHelper}
import graphql.GraphQLUtils._
import java.util.UUID
import models.note.NoteRowSchema
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object ScheduledTaskRunRowSchema extends GraphQLSchemaHelper("scheduledTaskRunRow") {
  implicit val scheduledTaskRunRowPrimaryKeyId: HasId[ScheduledTaskRunRow, UUID] = HasId[ScheduledTaskRunRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.services.taskServices.scheduledTaskRunRowService.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val scheduledTaskRunRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val scheduledTaskRunRowIdArg = Argument("id", uuidType)
  val scheduledTaskRunRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val scheduledTaskRunRowTaskArg = Argument("task", StringType)
  val scheduledTaskRunRowTaskSeqArg = Argument("tasks", ListInputType(StringType))
  val scheduledTaskRunRowStatusArg = Argument("status", StringType)
  val scheduledTaskRunRowStatusSeqArg = Argument("statuss", ListInputType(StringType))
  val scheduledTaskRunRowStartedArg = Argument("started", localDateTimeType)
  val scheduledTaskRunRowStartedSeqArg = Argument("starteds", ListInputType(localDateTimeType))

  implicit lazy val scheduledTaskRunRowType: sangria.schema.ObjectType[GraphQLContext, ScheduledTaskRunRow] = deriveObjectType()

  implicit lazy val scheduledTaskRunRowResultType: sangria.schema.ObjectType[GraphQLContext, ScheduledTaskRunRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "scheduledTaskRunRow", desc = None, t = OptionType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByPrimaryKey(c.ctx.creds, c.arg(scheduledTaskRunRowIdArg))(td)
    }, scheduledTaskRunRowIdArg),
    unitField(name = "scheduledTaskRunRowSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByPrimaryKeySeq(c.ctx.creds, c.arg(scheduledTaskRunRowIdSeqArg))(td)
    }, scheduledTaskRunRowIdSeqArg),
    unitField(name = "scheduledTaskRunRowSearch", desc = None, t = scheduledTaskRunRowResultType, f = (c, td) => {
      runSearch(c.ctx.services.taskServices.scheduledTaskRunRowService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "scheduledTaskRunRowById", desc = None, t = OptionType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getById(c.ctx.creds, c.arg(scheduledTaskRunRowIdArg))(td).map(_.headOption)
    }, scheduledTaskRunRowIdArg),
    unitField(name = "scheduledTaskRunsByIdSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByIdSeq(c.ctx.creds, c.arg(scheduledTaskRunRowIdSeqArg))(td)
    }, scheduledTaskRunRowIdSeqArg),
    unitField(name = "scheduledTaskRunsByTask", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByTask(c.ctx.creds, c.arg(scheduledTaskRunRowTaskArg))(td)
    }, scheduledTaskRunRowTaskArg),
    unitField(name = "scheduledTaskRunsByTaskSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByTaskSeq(c.ctx.creds, c.arg(scheduledTaskRunRowTaskSeqArg))(td)
    }, scheduledTaskRunRowTaskSeqArg),
    unitField(name = "scheduledTaskRunsByStatus", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByStatus(c.ctx.creds, c.arg(scheduledTaskRunRowStatusArg))(td)
    }, scheduledTaskRunRowStatusArg),
    unitField(name = "scheduledTaskRunsByStatusSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByStatusSeq(c.ctx.creds, c.arg(scheduledTaskRunRowStatusSeqArg))(td)
    }, scheduledTaskRunRowStatusSeqArg),
    unitField(name = "scheduledTaskRunsByStarted", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByStarted(c.ctx.creds, c.arg(scheduledTaskRunRowStartedArg))(td)
    }, scheduledTaskRunRowStartedArg),
    unitField(name = "scheduledTaskRunsByStartedSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.services.taskServices.scheduledTaskRunRowService.getByStartedSeq(c.ctx.creds, c.arg(scheduledTaskRunRowStartedSeqArg))(td)
    }, scheduledTaskRunRowStartedSeqArg)
  )

  val scheduledTaskRunRowMutationType = ObjectType(
    name = "ScheduledTaskRunRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(scheduledTaskRunRowType), f = (c, td) => {
        c.ctx.services.taskServices.scheduledTaskRunRowService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(scheduledTaskRunRowType), f = (c, td) => {
        c.ctx.services.taskServices.scheduledTaskRunRowService.update(c.ctx.creds, c.arg(scheduledTaskRunRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, scheduledTaskRunRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = scheduledTaskRunRowType, f = (c, td) => {
        c.ctx.services.taskServices.scheduledTaskRunRowService.remove(c.ctx.creds, c.arg(scheduledTaskRunRowIdArg))(td)
      }, scheduledTaskRunRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "scheduledTaskRunRow", desc = None, t = scheduledTaskRunRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[ScheduledTaskRunRow]) = {
    ScheduledTaskRunRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
