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

object SystemUserSchema extends SchemaHelper("systemUser") {
  implicit val roleEnum: EnumType[Role] = CommonSchema.deriveStringEnumeratumType(
    name = "RoleEnum",
    description = "The role of the system user.",
    values = Role.values.map(t => t -> t.value).toList
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
        name = "authoredNotes",
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

  val systemUserIdArg = Argument("id", uuidType, description = "Returns the User matching the provided Id.")
  val roleArg = Argument("role", roleEnum, description = "Filters the results to a provided SandboxTask.")

  val queryFields = fields[GraphQLContext, Unit](
    unitField(name = "profile", desc = Some("Returns information about the currently logged in user."), t = profileType, f = (c, td) => {
      Future.successful(UserProfile.fromUser(c.ctx.creds.user))
    }),
    unitField(name = "systemUser", desc = Some("Retrieves a single System User using its primary key."), t = OptionType(systemUserType), f = (c, td) => {
      c.ctx.services.userServices.systemUserService.getByPrimaryKey(c.ctx.creds, c.arg(systemUserIdArg))(td)
    }, systemUserIdArg),
    unitField(name = "systemUsers", desc = Some("Searches for System Users using the provided arguments."), t = systemUserResultType, f = (c, td) => {
      runSearch(c.ctx.services.userServices.systemUserService, c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
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
    fields = fields(
      unitField(name = "create", desc = Some("Creates a new System User using the provided fields."), t = OptionType(systemUserType), f = (c, td) => {
        c.ctx.services.userServices.systemUserService.create(c.ctx.creds, c.arg(DataFieldSchema.dataFieldsArg))(td)
      }, DataFieldSchema.dataFieldsArg),
      unitField(name = "update", desc = Some("Updates the System User with the provided id."), t = OptionType(systemUserType), f = (c, td) => {
        c.ctx.services.userServices.systemUserService.update(c.ctx.creds, c.arg(systemUserIdArg), c.arg(DataFieldSchema.dataFieldsArg))(td).map(_._1)
      }, systemUserIdArg, DataFieldSchema.dataFieldsArg),
      unitField(name = "remove", desc = Some("Removes the System User with the provided id."), t = systemUserType, f = (c, td) => {
        c.ctx.services.userServices.systemUserService.remove(c.ctx.creds, c.arg(systemUserIdArg))(td)
      }, systemUserIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "systemUser", desc = None, t = userMutationType, f = (c, td) => Future.successful(())))

  private[this] def toResult(r: SchemaHelper.SearchResult[SystemUser]) = {
    SystemUserResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
