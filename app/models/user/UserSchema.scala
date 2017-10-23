package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import models.graphql.{CommonSchema, GraphQLContext, SchemaHelper}
import sangria.macros.derive._
import sangria.schema._
import models.graphql.CommonSchema._
import models.graphql.DateTimeSchema._
import models.note.NoteSchema
import models.result.data.DataFieldSchema
import models.result.filter.FilterSchema._
import models.result.orderBy.OrderBySchema._
import models.result.paging.PagingSchema.pagingOptionsType
import models.template.Theme
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import util.FutureUtils.graphQlContext

import scala.concurrent.Future

object UserSchema extends SchemaHelper("user") {
  implicit val roleEnum = CommonSchema.deriveEnumeratumType(
    name = "RoleEnum",
    description = "The role of the system user.",
    values = Role.values.map(t => t -> t.entryName).toList
  )

  implicit val themeEnum = CommonSchema.deriveStringEnumeratumType(
    name = "ThemeEnum",
    description = "The selected theme color.",
    values = Theme.values.map(t => t -> t.color).toList
  )

  implicit val profileType = deriveObjectType[GraphQLContext, UserProfile](ObjectTypeDescription("Information about the current session."))

  implicit val userPrimaryKeyId = HasId[User, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = Future.successful(c.app.userService.getByPrimaryKeySeq(idSeq)(c.trace))
  val userByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val userByRoleRelation = Relation[User, Role]("byRole", x => Seq(x.role))
  val userByRoleFetcher = Fetcher.rel[GraphQLContext, User, User, UUID](
    getByPrimaryKeySeq, (c, rels) => Future.successful(c.app.userService.getByRoleSeq(rels(userByRoleRelation))(c.trace))
  )

  implicit val loginInfoType = deriveObjectType[GraphQLContext, LoginInfo](ObjectTypeDescription("Information about login credentials."))
  implicit val userPreferenceType = deriveObjectType[GraphQLContext, UserPreferences](ObjectTypeDescription("Information about users of the system."))
  implicit lazy val userType: ObjectType[GraphQLContext, User] = deriveObjectType(
    AddFields(
      Field(
        name = "noteAuthorFkey",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => NoteSchema.noteByAuthorFetcher.deferRelSeq(
          NoteSchema.noteByAuthorRelation, c.value.id
        )
      )
    )
  )

  implicit lazy val userResultType: ObjectType[GraphQLContext, UserResult] = deriveObjectType()

  private[this] def toResult(r: SearchResult[User]) = {
    UserResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }

  val userIdArg = Argument("id", uuidType, description = "Returns the User matching the provided Id.")
  val roleArg = Argument("role", roleEnum, description = "Filters the results to a provided SandboxTask.")

  val queryFields = fields[GraphQLContext, Unit](
    Field(
      name = "profile",
      description = Some("Returns information about the currently logged in user."),
      fieldType = profileType,
      resolve = c => traceB(c.ctx, "profile") { _ =>
        val u = c.ctx.user
        UserProfile(u.id, u.username, u.profile.providerKey, u.role, u.preferences.theme, u.created)
      }
    ),
    Field(
      name = "user",
      fieldType = userResultType,
      arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
      resolve = c => traceB(c.ctx, "search")(td => toResult(runSearch(c.ctx.app.userService, c, td)))
    ),
    Field(
      name = "userByRole",
      fieldType = ListType(userType),
      arguments = roleArg :: Nil,
      resolve = c => userByRoleFetcher.deferRelSeq(userByRoleRelation, c.arg(roleArg))
    )
  )

  val userMutationType = ObjectType(
    name = "user",
    description = "Mutations for Users.",
    fields = fields[GraphQLContext, Unit](
      Field(
        name = "create",
        description = Some("Creates a new User using the provided fields."),
        arguments = DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = OptionType(userType),
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceB(c.ctx, "create")(tn => c.ctx.services.userServices.userService.create(dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the User with the provided id."),
        arguments = userIdArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = userType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceB(c.ctx, "update")(tn => c.ctx.services.userServices.userService.update(c.args.arg(userIdArg), dataFields)(tn)._1)
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the User with the provided id."),
        arguments = userIdArg :: Nil,
        fieldType = userType,
        resolve = c => traceB(c.ctx, "remove")(tn => c.ctx.services.userServices.userService.remove(c.args.arg(userIdArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "user", fieldType = userMutationType, resolve = _ => ()))
}
