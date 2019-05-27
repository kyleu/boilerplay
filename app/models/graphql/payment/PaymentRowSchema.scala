/* Generated File */
package models.graphql.payment

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.graphql.customer.{CustomerRowSchema, RentalRowSchema}
import models.graphql.store.StaffRowSchema
import models.payment.{PaymentRow, PaymentRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object PaymentRowSchema extends GraphQLSchemaHelper("paymentRow") {
  implicit val paymentRowPrimaryKeyId: HasId[PaymentRow, Long] = HasId[PaymentRow, Long](_.paymentId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Long]) = c.getInstance[services.payment.PaymentRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val paymentRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val paymentRowPaymentIdArg = Argument("paymentId", LongType)
  val paymentRowPaymentIdSeqArg = Argument("paymentIds", ListInputType(LongType))

  val paymentRowCustomerIdArg = Argument("customerId", IntType)
  val paymentRowCustomerIdSeqArg = Argument("customerIds", ListInputType(IntType))
  val paymentRowStaffIdArg = Argument("staffId", IntType)
  val paymentRowStaffIdSeqArg = Argument("staffIds", ListInputType(IntType))
  val paymentRowRentalIdArg = Argument("rentalId", LongType)
  val paymentRowRentalIdSeqArg = Argument("rentalIds", ListInputType(LongType))
  val paymentRowAmountArg = Argument("amount", BigDecimalType)
  val paymentRowAmountSeqArg = Argument("amounts", ListInputType(BigDecimalType))

  val paymentRowByStaffIdRelation = Relation[PaymentRow, Int]("byStaffId", x => Seq(x.staffId))
  val paymentRowByStaffIdFetcher = Fetcher.rel[GraphQLContext, PaymentRow, PaymentRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.payment.PaymentRowService]).getByStaffIdSeq(c.creds, rels(paymentRowByStaffIdRelation))(c.trace)
  )

  val paymentRowByRentalIdRelation = Relation[PaymentRow, Long]("byRentalId", x => Seq(x.rentalId))
  val paymentRowByRentalIdFetcher = Fetcher.rel[GraphQLContext, PaymentRow, PaymentRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.payment.PaymentRowService]).getByRentalIdSeq(c.creds, rels(paymentRowByRentalIdRelation))(c.trace)
  )

  val paymentRowByCustomerIdRelation = Relation[PaymentRow, Int]("byCustomerId", x => Seq(x.customerId))
  val paymentRowByCustomerIdFetcher = Fetcher.rel[GraphQLContext, PaymentRow, PaymentRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.payment.PaymentRowService]).getByCustomerIdSeq(c.creds, rels(paymentRowByCustomerIdRelation))(c.trace)
  )

  implicit lazy val paymentRowType: sangria.schema.ObjectType[GraphQLContext, PaymentRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "paymentStaffIdFkeyRel",
        fieldType = StaffRowSchema.staffRowType,
        resolve = ctx => StaffRowSchema.staffRowByPrimaryKeyFetcher.defer(ctx.value.staffId)
      ),
      Field(
        name = "paymentRentalIdFkeyRel",
        fieldType = RentalRowSchema.rentalRowType,
        resolve = ctx => RentalRowSchema.rentalRowByPrimaryKeyFetcher.defer(ctx.value.rentalId)
      ),
      Field(
        name = "paymentCustomerIdFkeyRel",
        fieldType = CustomerRowSchema.customerRowType,
        resolve = ctx => CustomerRowSchema.customerRowByPrimaryKeyFetcher.defer(ctx.value.customerId)
      )
    )
  )

  implicit lazy val paymentRowResultType: sangria.schema.ObjectType[GraphQLContext, PaymentRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "paymentRow", desc = None, t = OptionType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByPrimaryKey(c.ctx.creds, c.arg(paymentRowPaymentIdArg))(td)
    }, paymentRowPaymentIdArg),
    unitField(name = "paymentRowSeq", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(paymentRowPaymentIdSeqArg))(td)
    }, paymentRowPaymentIdSeqArg),
    unitField(name = "paymentRowSearch", desc = None, t = paymentRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.payment.PaymentRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "paymentsByCustomerId", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByCustomerId(c.ctx.creds, c.arg(paymentRowCustomerIdArg))(td)
    }, paymentRowCustomerIdArg),
    unitField(name = "paymentsByCustomerIdSeq", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByCustomerIdSeq(c.ctx.creds, c.arg(paymentRowCustomerIdSeqArg))(td)
    }, paymentRowCustomerIdSeqArg),
    unitField(name = "paymentsByStaffId", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByStaffId(c.ctx.creds, c.arg(paymentRowStaffIdArg))(td)
    }, paymentRowStaffIdArg),
    unitField(name = "paymentsByStaffIdSeq", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByStaffIdSeq(c.ctx.creds, c.arg(paymentRowStaffIdSeqArg))(td)
    }, paymentRowStaffIdSeqArg),
    unitField(name = "paymentsByRentalId", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByRentalId(c.ctx.creds, c.arg(paymentRowRentalIdArg))(td)
    }, paymentRowRentalIdArg),
    unitField(name = "paymentsByRentalIdSeq", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByRentalIdSeq(c.ctx.creds, c.arg(paymentRowRentalIdSeqArg))(td)
    }, paymentRowRentalIdSeqArg),
    unitField(name = "paymentsByAmount", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByAmount(c.ctx.creds, c.arg(paymentRowAmountArg))(td)
    }, paymentRowAmountArg),
    unitField(name = "paymentsByAmountSeq", desc = None, t = ListType(paymentRowType), f = (c, td) => {
      c.ctx.getInstance[services.payment.PaymentRowService].getByAmountSeq(c.ctx.creds, c.arg(paymentRowAmountSeqArg))(td)
    }, paymentRowAmountSeqArg)
  )

  val paymentRowMutationType = ObjectType(
    name = "PaymentRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(paymentRowType), f = (c, td) => {
        c.ctx.getInstance[services.payment.PaymentRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(paymentRowType), f = (c, td) => {
        c.ctx.getInstance[services.payment.PaymentRowService].update(c.ctx.creds, c.arg(paymentRowPaymentIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, paymentRowPaymentIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = paymentRowType, f = (c, td) => {
        c.ctx.getInstance[services.payment.PaymentRowService].remove(c.ctx.creds, c.arg(paymentRowPaymentIdArg))(td)
      }, paymentRowPaymentIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "paymentRow", desc = None, t = paymentRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[PaymentRow]) = {
    PaymentRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
