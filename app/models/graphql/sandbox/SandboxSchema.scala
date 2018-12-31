package models.graphql.sandbox

import com.kyleu.projectile.graphql.GraphQLContext
import com.kyleu.projectile.graphql.GraphQLUtils.{deriveEnumeratumType, deriveObjectType}
import models.sandbox.SandboxTask
import sangria.macros.derive.ObjectTypeName
import sangria.schema.{Argument, EnumType, Field, ListType, ObjectType, OptionInputType, StringType, fields}

object SandboxSchema {
  implicit val sandboxTaskEnum: EnumType[SandboxTask] = deriveEnumeratumType(name = "SandboxTask", values = SandboxTask.values)

  val sandboxTaskArg = Argument("task", sandboxTaskEnum)
  val sandboxArgumentArg = Argument("arg", OptionInputType(StringType))

  val sandboxTaskType = ObjectType("SandboxTaskDetail", fields[Unit, SandboxTask](
    Field("id", StringType, resolve = _.value.toString),
    Field("name", StringType, resolve = _.value.name),
    Field("description", StringType, resolve = _.value.description)
  ))

  implicit val sandboxResultType: ObjectType[GraphQLContext, SandboxTask.Result] = deriveObjectType[GraphQLContext, SandboxTask.Result](
    ObjectTypeName("SandboxResult")
  )

  val queryFields = fields[GraphQLContext, Unit](Field(
    name = "sandbox",
    fieldType = ListType(sandboxTaskType),
    resolve = _ => SandboxTask.values
  ))

  val mutationFields = fields[GraphQLContext, Unit](
    Field(
      name = "sandbox",
      fieldType = sandboxResultType,
      arguments = sandboxTaskArg :: sandboxArgumentArg :: Nil,
      resolve = c => c.arg(sandboxTaskArg).run(SandboxTask.Config(c.ctx.tracing, c.ctx.injector, c.arg(sandboxArgumentArg)))(c.ctx.trace)
    )
  )
}
