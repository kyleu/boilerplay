package models.user

import java.util.UUID

import com.mohiva.play.silhouette.api.LoginInfo
import graphql.CommonSchema
import graphql.{GraphQLContext, GraphQLSchemaHelper}
import sangria.macros.derive.{AddFields, deriveObjectType}
import sangria.schema._
import graphql.GraphQLUtils._
import models.note.NoteSchema
import models.template.Theme
import sangria.execution.deferred.{Fetcher, HasId, Relation}

import scala.concurrent.Future

object SystemUserSchema extends GraphQLSchemaHelper("systemUser") {
  implicit val roleEnum: EnumType[Role] = CommonSchema.deriveStringEnumeratumType(name = "RoleEnum", values = Role.values)
  implicit val themeEnum: EnumType[Theme] = CommonSchema.deriveStringEnumeratumType(name = "ThemeEnum", values = Theme.values)
  implicit val profileType: ObjectType[GraphQLContext, UserProfile] = deriveObjectType()

  implicit val systemUserPrimaryKeyId: HasId[SystemUser, UUID] = HasId[SystemUser, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = c.app.coreServices.users.getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val systemUserByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val systemUserByRoleRelation = Relation[SystemUser, Role]("byRole", x => Seq(x.role))
  val systemUserByRoleFetcher = Fetcher.rel[GraphQLContext, SystemUser, SystemUser, UUID](
    getByPrimaryKeySeq, (c, rels) => c.app.coreServices.users.getByRoleSeq(rels(systemUserByRoleRelation))(c.trace)
  )

  implicit val loginInfoType: ObjectType[GraphQLContext, LoginInfo] = deriveObjectType()
  implicit val userPreferenceType: ObjectType[GraphQLContext, UserPreferences] = deriveObjectType()
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
    unitField(name = "profile", desc = None, t = profileType, f = (c, td) => {
      Future.successful(UserProfile.fromUser(c.ctx.creds.user))
    }),
    unitField(name = "systemUser", desc = None, t = OptionType(systemUserType), f = (c, td) => {
      c.ctx.services.userServices.systemUserService.getByPrimaryKey(c.ctx.creds, c.arg(systemUserIdArg))(td)
    }, systemUserIdArg),
    unitField(name = "systemUsers", desc = None, t = systemUserResultType, f = (c, td) => {
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
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(systemUserType), f = (c, td) => {
        c.ctx.services.userServices.systemUserService.create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(systemUserType), f = (c, td) => {
        c.ctx.services.userServices.systemUserService.update(c.ctx.creds, c.arg(systemUserIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, systemUserIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = systemUserType, f = (c, td) => {
        c.ctx.services.userServices.systemUserService.remove(c.ctx.creds, c.arg(systemUserIdArg))(td)
      }, systemUserIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "systemUser", desc = None, t = userMutationType, f = (c, td) => Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[SystemUser]) = {
    SystemUserResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
