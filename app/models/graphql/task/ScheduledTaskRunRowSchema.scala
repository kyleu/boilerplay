/* Generated File */
package models.graphql.task

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import java.util.UUID
import models.task.{ScheduledTaskRunRow, ScheduledTaskRunRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._
import services.task.ScheduledTaskRunRowService

object ScheduledTaskRunRowSchema extends GraphQLSchemaHelper("scheduledTaskRunRow") {
  implicit val scheduledTaskRunRowPrimaryKeyId: HasId[ScheduledTaskRunRow, UUID] = HasId[ScheduledTaskRunRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val scheduledTaskRunRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val scheduledTaskRunRowIdArg = Argument("id", uuidType)
  val scheduledTaskRunRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val scheduledTaskRunRowTaskArg = Argument("task", StringType)
  val scheduledTaskRunRowTaskSeqArg = Argument("tasks", ListInputType(StringType))
  val scheduledTaskRunRowArgumentsArg = Argument("arguments", ListInputType(StringType))
  val scheduledTaskRunRowArgumentsSeqArg = Argument("argumentss", ListInputType(ListInputType(StringType)))
  val scheduledTaskRunRowStatusArg = Argument("status", StringType)
  val scheduledTaskRunRowStatusSeqArg = Argument("statuss", ListInputType(StringType))
  val scheduledTaskRunRowStartedArg = Argument("started", localDateTimeType)
  val scheduledTaskRunRowStartedSeqArg = Argument("starteds", ListInputType(localDateTimeType))

  implicit lazy val scheduledTaskRunRowType: sangria.schema.ObjectType[GraphQLContext, ScheduledTaskRunRow] = deriveObjectType()

  implicit lazy val scheduledTaskRunRowResultType: sangria.schema.ObjectType[GraphQLContext, ScheduledTaskRunRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "scheduledTaskRunRow", desc = None, t = OptionType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByPrimaryKey(c.ctx.creds, c.arg(scheduledTaskRunRowIdArg))(td)
    }, scheduledTaskRunRowIdArg),
    unitField(name = "scheduledTaskRunRowSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(scheduledTaskRunRowIdSeqArg))(td)
    }, scheduledTaskRunRowIdSeqArg),
    unitField(name = "scheduledTaskRunRowSearch", desc = None, t = scheduledTaskRunRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "scheduledTaskRunsByTask", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByTask(c.ctx.creds, c.arg(scheduledTaskRunRowTaskArg))(td)
    }, scheduledTaskRunRowTaskArg),
    unitField(name = "scheduledTaskRunsByTaskSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByTaskSeq(c.ctx.creds, c.arg(scheduledTaskRunRowTaskSeqArg))(td)
    }, scheduledTaskRunRowTaskSeqArg),
    unitField(name = "scheduledTaskRunsByArguments", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByArguments(c.ctx.creds, c.arg(scheduledTaskRunRowArgumentsArg).toList)(td)
    }, scheduledTaskRunRowArgumentsArg),
    unitField(name = "scheduledTaskRunsByArgumentsSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByArgumentsSeq(c.ctx.creds, c.arg(scheduledTaskRunRowArgumentsSeqArg).map(_.toList))(td)
    }, scheduledTaskRunRowArgumentsSeqArg),
    unitField(name = "scheduledTaskRunsByStatus", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByStatus(c.ctx.creds, c.arg(scheduledTaskRunRowStatusArg))(td)
    }, scheduledTaskRunRowStatusArg),
    unitField(name = "scheduledTaskRunsByStatusSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByStatusSeq(c.ctx.creds, c.arg(scheduledTaskRunRowStatusSeqArg))(td)
    }, scheduledTaskRunRowStatusSeqArg),
    unitField(name = "scheduledTaskRunsByStarted", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByStarted(c.ctx.creds, c.arg(scheduledTaskRunRowStartedArg))(td)
    }, scheduledTaskRunRowStartedArg),
    unitField(name = "scheduledTaskRunsByStartedSeq", desc = None, t = ListType(scheduledTaskRunRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).getByStartedSeq(c.ctx.creds, c.arg(scheduledTaskRunRowStartedSeqArg))(td)
    }, scheduledTaskRunRowStartedSeqArg)
  )

  val scheduledTaskRunRowMutationType = ObjectType(
    name = "ScheduledTaskRunRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(scheduledTaskRunRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(scheduledTaskRunRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).update(c.ctx.creds, c.arg(scheduledTaskRunRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, scheduledTaskRunRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = scheduledTaskRunRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[ScheduledTaskRunRowService]).remove(c.ctx.creds, c.arg(scheduledTaskRunRowIdArg))(td)
      }, scheduledTaskRunRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "scheduledTaskRunRow", desc = None, t = scheduledTaskRunRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[ScheduledTaskRunRow]) = {
    ScheduledTaskRunRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
