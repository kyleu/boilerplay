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

object SystemUserSchema extends SchemaHelper("systemUser") {
  implicit val roleEnum: EnumType[Role] = CommonSchema.deriveEnumeratumType(
    name = "RoleEnum",
    description = "The role of the system user.",
    values = Role.values.map(t => t -> t.entryName).toList
  )

  implicit val themeEnum: EnumType[Theme] = CommonSchema.deriveStringEnumeratumType(
    name = "ThemeEnum",
    description = "The selected theme color.",
    values = Theme.values.map(t => t -> t.color).toList
  )

  implicit val profileType: ObjectType[GraphQLContext, UserProfile] = deriveObjectType(ObjectTypeDescription("Information about the current session."))

  implicit val systemUserPrimaryKeyId: HasId[SystemUser, UUID] = HasId[SystemUser, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = c.app.coreServices.users.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val systemUserByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val systemUserByRoleRelation = Relation[SystemUser, Role]("byRole", x => Seq(x.role))
  val systemUserByRoleFetcher = Fetcher.rel[GraphQLContext, SystemUser, SystemUser, UUID](
    getByPrimaryKeySeq, (c, rels) => c.app.coreServices.users.getByRoleSeq(rels(systemUserByRoleRelation))(c.trace)
  )

  implicit val loginInfoType: ObjectType[GraphQLContext, LoginInfo] = deriveObjectType(ObjectTypeDescription("Information about login credentials."))
  implicit val userPreferenceType: ObjectType[GraphQLContext, UserPreferences] = {
    deriveObjectType(ObjectTypeDescription("Information about users of the system."))
  }
  implicit lazy val systemUserType: ObjectType[GraphQLContext, SystemUser] = deriveObjectType(
    AddFields(
      Field(
        name = "noteAuthorFkey",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => NoteSchema.noteByAuthorFetcher.deferRelSeq(
          NoteSchema.noteByAuthorRelation, c.value.id
        )
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.app.coreServices.notes.getFor(c.ctx.creds, "systemUser", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val systemUserResultType: ObjectType[GraphQLContext, SystemUserResult] = deriveObjectType()

  private[this] def toResult(r: SchemaHelper.SearchResult[SystemUser]) = {
    SystemUserResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }

  val userIdArg = Argument("id", uuidType, description = "Returns the User matching the provided Id.")
  val roleArg = Argument("role", roleEnum, description = "Filters the results to a provided SandboxTask.")

  val queryFields = fields[GraphQLContext, Unit](
    Field(
      name = "profile",
      description = Some("Returns information about the currently logged in user."),
      fieldType = profileType,
      resolve = c => traceB(c.ctx, "profile")(_ => UserProfile.fromUser(c.ctx.creds.user))
    ),
    Field(
      name = "systemUser",
      fieldType = systemUserResultType,
      arguments = queryArg :: reportFiltersArg :: orderBysArg :: limitArg :: offsetArg :: Nil,
      resolve = c => traceF(c.ctx, "search")(td => runSearch(c.ctx.app.coreServices.users, c, td).map(toResult))
    ),
    Field(
      name = "systemUserByRole",
      fieldType = ListType(systemUserType),
      arguments = roleArg :: Nil,
      resolve = c => systemUserByRoleFetcher.deferRelSeq(systemUserByRoleRelation, c.arg(roleArg))
    )
  )

  val userMutationType = ObjectType(
    name = "systemUser",
    description = "Mutations for System Users.",
    fields = fields[GraphQLContext, Unit](
      Field(
        name = "create",
        description = Some("Creates a new User using the provided fields."),
        arguments = DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = OptionType(systemUserType),
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "create")(tn => c.ctx.services.userServices.systemUserService.create(c.ctx.creds, dataFields)(tn))
        }
      ),
      Field(
        name = "update",
        description = Some("Updates the User with the provided id."),
        arguments = userIdArg :: DataFieldSchema.dataFieldsArg :: Nil,
        fieldType = systemUserType,
        resolve = c => {
          val dataFields = c.args.arg(DataFieldSchema.dataFieldsArg)
          traceF(c.ctx, "update")(tn => c.ctx.services.userServices.systemUserService.update(c.ctx.creds, c.args.arg(userIdArg), dataFields)(tn).map(_._1))
        }
      ),
      Field(
        name = "remove",
        description = Some("Removes the User with the provided id."),
        arguments = userIdArg :: Nil,
        fieldType = systemUserType,
        resolve = c => traceF(c.ctx, "remove")(tn => c.ctx.services.userServices.systemUserService.remove(c.ctx.creds, c.args.arg(userIdArg))(tn))
      )
    )
  )

  val mutationFields = fields[GraphQLContext, Unit](Field(name = "systemUser", fieldType = userMutationType, resolve = _ => ()))
}
