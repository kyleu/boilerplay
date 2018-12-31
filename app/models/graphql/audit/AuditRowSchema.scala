/* Generated File */
package models.graphql.audit

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import java.util.UUID
import models.audit.{AuditRow, AuditRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._
import services.audit.AuditRowService

object AuditRowSchema extends GraphQLSchemaHelper("auditRow") {
  implicit val auditRowPrimaryKeyId: HasId[AuditRow, UUID] = HasId[AuditRow, UUID](_.id)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[UUID]) = {
    c.injector.getInstance(classOf[AuditRowService]).getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  }
  val auditRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val auditRowIdArg = Argument("id", uuidType)
  val auditRowIdSeqArg = Argument("ids", ListInputType(uuidType))

  val auditRowActArg = Argument("act", StringType)
  val auditRowActSeqArg = Argument("acts", ListInputType(StringType))
  val auditRowAppArg = Argument("app", StringType)
  val auditRowAppSeqArg = Argument("apps", ListInputType(StringType))
  val auditRowClientArg = Argument("client", StringType)
  val auditRowClientSeqArg = Argument("clients", ListInputType(StringType))
  val auditRowServerArg = Argument("server", StringType)
  val auditRowServerSeqArg = Argument("servers", ListInputType(StringType))
  val auditRowUserIdArg = Argument("userId", uuidType)
  val auditRowUserIdSeqArg = Argument("userIds", ListInputType(uuidType))

  implicit lazy val auditRowType: sangria.schema.ObjectType[GraphQLContext, AuditRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "auditRecordAuditIdFkey",
        fieldType = ListType(AuditRecordRowSchema.auditRecordRowType),
        resolve = c => AuditRecordRowSchema.auditRecordRowByAuditIdFetcher.deferRelSeq(
          AuditRecordRowSchema.auditRecordRowByAuditIdRelation, c.value.id
        )
      ),
      Field(
        name = "relatedNotes",
        fieldType = ListType(NoteSchema.noteType),
        resolve = c => c.ctx.noteLookup(c.ctx.creds, "auditRow", c.value.id)(c.ctx.trace)
      )
    )
  )

  implicit lazy val auditRowResultType: sangria.schema.ObjectType[GraphQLContext, AuditRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "auditRow", desc = None, t = OptionType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByPrimaryKey(c.ctx.creds, c.arg(auditRowIdArg))(td)
    }, auditRowIdArg),
    unitField(name = "auditRowSeq", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByPrimaryKeySeq(c.ctx.creds, c.arg(auditRowIdSeqArg))(td)
    }, auditRowIdSeqArg),
    unitField(name = "auditRowSearch", desc = None, t = auditRowResultType, f = (c, td) => {
      runSearch(c.ctx.injector.getInstance(classOf[AuditRowService]), c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "auditsByAct", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByAct(c.ctx.creds, c.arg(auditRowActArg))(td)
    }, auditRowActArg),
    unitField(name = "auditsByActSeq", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByActSeq(c.ctx.creds, c.arg(auditRowActSeqArg))(td)
    }, auditRowActSeqArg),
    unitField(name = "auditsByApp", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByApp(c.ctx.creds, c.arg(auditRowAppArg))(td)
    }, auditRowAppArg),
    unitField(name = "auditsByAppSeq", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByAppSeq(c.ctx.creds, c.arg(auditRowAppSeqArg))(td)
    }, auditRowAppSeqArg),
    unitField(name = "auditsByClient", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByClient(c.ctx.creds, c.arg(auditRowClientArg))(td)
    }, auditRowClientArg),
    unitField(name = "auditsByClientSeq", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByClientSeq(c.ctx.creds, c.arg(auditRowClientSeqArg))(td)
    }, auditRowClientSeqArg),
    unitField(name = "auditsByServer", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByServer(c.ctx.creds, c.arg(auditRowServerArg))(td)
    }, auditRowServerArg),
    unitField(name = "auditsByServerSeq", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByServerSeq(c.ctx.creds, c.arg(auditRowServerSeqArg))(td)
    }, auditRowServerSeqArg),
    unitField(name = "auditsByUserId", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByUserId(c.ctx.creds, c.arg(auditRowUserIdArg))(td)
    }, auditRowUserIdArg),
    unitField(name = "auditsByUserIdSeq", desc = None, t = ListType(auditRowType), f = (c, td) => {
      c.ctx.injector.getInstance(classOf[AuditRowService]).getByUserIdSeq(c.ctx.creds, c.arg(auditRowUserIdSeqArg))(td)
    }, auditRowUserIdSeqArg)
  )

  val auditRowMutationType = ObjectType(
    name = "AuditRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(auditRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[AuditRowService]).create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(auditRowType), f = (c, td) => {
        c.ctx.injector.getInstance(classOf[AuditRowService]).update(c.ctx.creds, c.arg(auditRowIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, auditRowIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = auditRowType, f = (c, td) => {
        c.ctx.injector.getInstance(classOf[AuditRowService]).remove(c.ctx.creds, c.arg(auditRowIdArg))(td)
      }, auditRowIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "auditRow", desc = None, t = auditRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[AuditRow]) = {
    AuditRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
