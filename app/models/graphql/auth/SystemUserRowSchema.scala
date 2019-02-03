/* Generated File */
package models.graphql.auth

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import java.util.UUID
import models.auth.{SystemUserRow, SystemUserRowResult}
import models.graphql.note.NoteRowSchema
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object SystemUserRowSchema extends GraphQLSchemaHelper("systemUserRow") {
  implicit val systemUserRowPrimaryKeyId: HasId[SystemUserRow, UUID] = HasId[SystemUserRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val systemUserRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val systemUserRowIdArg = Argument("id", uuidType)
  val systemUserRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val systemUserRowUsernameArg = Argument("username", StringType)
  val systemUserRowUsernameSeqArg = Argument("usernames", ListInputType(StringType))
  val systemUserRowProviderArg = Argument("provider", StringType)
  val systemUserRowProviderSeqArg = Argument("providers", ListInputType(StringType))
  val systemUserRowKeyArg = Argument("key", StringType)
  val systemUserRowKeySeqArg = Argument("keys", ListInputType(StringType))

  implicit lazy val systemUserRowType: sangria.schema.ObjectType[GraphQLContext, SystemUserRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "noteAuthorFkey",
        fieldType = ListType(NoteRowSchema.noteRowType),
        resolve = c => NoteRowSchema.noteRowByAuthorFetcher.deferRelSeq(
          NoteRowSchema.noteRowByAuthorRelation, c.value.id
        )
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.noteLookup(c.ctx.creds, "systemUserRow", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val systemUserRowResultType: sangria.schema.ObjectType[GraphQLContext, SystemUserRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "systemUserRow", desc = None, t = OptionType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByPrimaryKey(c.ctx.creds, c.arg(systemUserRowIdArg))(td)
    }, systemUserRowIdArg),
    unitField(name = "systemUserRowSeq", desc = None, t = ListType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(systemUserRowIdSeqArg))(td)
    }, systemUserRowIdSeqArg),
    unitField(name = "systemUserRowSearch", desc = None, t = systemUserRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "systemUserRowByUsername", desc = None, t = OptionType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByUsername(c.ctx.creds, c.arg(systemUserRowUsernameArg))(td).map(_.headOption)
    }, systemUserRowUsernameArg),
    unitField(name = "systemUsersByUsernameSeq", desc = None, t = ListType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByUsernameSeq(c.ctx.creds, c.arg(systemUserRowUsernameSeqArg))(td)
    }, systemUserRowUsernameSeqArg),
    unitField(name = "systemUserRowByProvider", desc = None, t = OptionType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByProvider(c.ctx.creds, c.arg(systemUserRowProviderArg))(td).map(_.headOption)
    }, systemUserRowProviderArg),
    unitField(name = "systemUsersByProviderSeq", desc = None, t = ListType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByProviderSeq(c.ctx.creds, c.arg(systemUserRowProviderSeqArg))(td)
    }, systemUserRowProviderSeqArg),
    unitField(name = "systemUserRowByKey", desc = None, t = OptionType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByKey(c.ctx.creds, c.arg(systemUserRowKeyArg))(td).map(_.headOption)
    }, systemUserRowKeyArg),
    unitField(name = "systemUsersByKeySeq", desc = None, t = ListType(systemUserRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).getByKeySeq(c.ctx.creds, c.arg(systemUserRowKeySeqArg))(td)
    }, systemUserRowKeySeqArg)
  )

  val systemUserRowMutationType = ObjectType(
    name = "SystemUserRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(systemUserRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(systemUserRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).update(c.ctx.creds, c.arg(systemUserRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, systemUserRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = systemUserRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[services.auth.SystemUserRowService]).remove(c.ctx.creds, c.arg(systemUserRowIdArg))(td)
      }, systemUserRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "systemUserRow", desc = None, t = systemUserRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[SystemUserRow]) = {
    SystemUserRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
