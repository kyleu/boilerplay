package models.sandbox

import models.graphql.{CommonSchema, GraphQLContext}
import sangria.macros.derive._
import sangria.schema._

object SandboxSchema {
  implicit val sandboxTaskEnum = CommonSchema.deriveEnumeratumType(
    name = "SandboxTask",
    description = "One-off tests that don't deserve a UI of their own.",
    values = SandboxTask.values.map(t => t -> t.description).toList
  )

  val sandboxTaskArg = Argument("task", sandboxTaskEnum, description = "Filters the results to a provided SandboxTask.")
  val sandboxArgumentArg = Argument("arg", OptionInputType(StringType), description = "Passes the provided argument to the SandboxTask.")

  val sandboxTaskType = ObjectType("SandboxTaskDetail", "Detailed information about sandbox tasks.", fields[Unit, SandboxTask](
    Field("id", StringType, Some("The  id for the sandbox task."), resolve = _.value.toString),
    Field("name", StringType, Some("The name of the sandbox task."), resolve = _.value.name),
    Field("description", StringType, Some("The description of the sandbox task."), resolve = _.value.description)
  ))

  implicit val sandboxResultType = deriveObjectType[GraphQLContext, SandboxTask.Result](
    ObjectTypeName("SandboxResult"),
    DocumentField("task", "The task used to provide the result."),
    DocumentField("status", "The result status, usually \"OK\"."),
    DocumentField("elapsed", "The execution time, in milliseconds."),
    DocumentField("result", "String detailing the results of this task.")
  )

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "sandbox",
    fieldType = ListType(sandboxTaskType),
    description = Some("Returns a list of one-off sandbox tests."),
    resolve = _ => SandboxTask.values
  ))

  val mutationFields = fields[GraphQLContext, Unit](
    Field(
      name = "sandbox",
      fieldType = sandboxResultType,
      description = Some("Allows calling of sandbox tests."),
      arguments = sandboxTaskArg :: sandboxArgumentArg :: Nil,
      resolve = c => c.arg(sandboxTaskArg).run(c.ctx.app, c.arg(sandboxArgumentArg))(c.ctx.trace)
    )
  )
}
