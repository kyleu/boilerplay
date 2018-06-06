package models.sandbox

import models.graphql.{CommonSchema, GraphQLContext, SchemaHelper}
import sangria.macros.derive._
import sangria.schema._

import scala.concurrent.Future

object SandboxSchema extends SchemaHelper("sandbox") {
  implicit val sandboxTaskEnum: EnumType[SandboxTask] = CommonSchema.deriveEnumeratumType(
    name = "SandboxTask",
    values = SandboxTask.values
  )

  val sandboxTaskArg = Argument("task", sandboxTaskEnum, description = "Filters the results to a provided SandboxTask.")
  val sandboxArgumentArg = Argument("arg", OptionInputType(StringType), description = "Passes the provided argument to the SandboxTask.")

  val sandboxTaskType = ObjectType("SandboxTaskDetail", "Detailed information about sandbox tasks.", fields[Unit, SandboxTask](
    Field("id", StringType, Some("The  id for the sandbox task."), resolve = _.value.toString),
    Field("name", StringType, Some("The name of the sandbox task."), resolve = _.value.name),
    Field("description", StringType, Some("The description of the sandbox task."), resolve = _.value.description)
  ))

  implicit val sandboxResultType: ObjectType[GraphQLContext, SandboxTask.Result] = deriveObjectType[GraphQLContext, SandboxTask.Result](
    ObjectTypeName("SandboxResult"),
    DocumentField("task", "The task used to provide the result."),
    DocumentField("status", "The result status, usually \"OK\"."),
    DocumentField("elapsed", "The execution time, in milliseconds."),
    DocumentField("result", "String detailing the results of this task.")
  )

  val queryFields = fields(unitField(
    name = "sandbox",
    desc = Some("Returns a list of one-off sandbox tests."),
    t = ListType(sandboxTaskType),
    f = (c, td) => {
      Future.successful(SandboxTask.values)
    }
  ))

  val mutationFields = fields(unitField(name = "sandbox", desc = Some("Allows calling of sandbox tests."), t = sandboxResultType, f = (c, td) => {
    c.arg(sandboxTaskArg).run(SandboxTask.Config(c.ctx.app, c.ctx.services, c.ctx.svc, c.arg(sandboxArgumentArg)))(c.ctx.trace)
  }, sandboxTaskArg, sandboxArgumentArg))
}
