/* Generated File */
package models.graphql.customer

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.customer.{RentalRow, RentalRowResult}
import models.graphql.payment.PaymentRowSchema
import models.graphql.store.{InventoryRowSchema, StaffRowSchema}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object RentalRowSchema extends GraphQLSchemaHelper("rentalRow") {
  implicit val rentalRowPrimaryKeyId: HasId[RentalRow, Long] = HasId[RentalRow, Long](_.rentalId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Long]) = c.getInstance[services.customer.RentalRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val rentalRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val rentalRowRentalIdArg = Argument("rentalId", LongType)
  val rentalRowRentalIdSeqArg = Argument("rentalIds", ListInputType(LongType))

  val rentalRowRentalDateArg = Argument("rentalDate", zonedDateTimeType)
  val rentalRowRentalDateSeqArg = Argument("rentalDates", ListInputType(zonedDateTimeType))
  val rentalRowInventoryIdArg = Argument("inventoryId", LongType)
  val rentalRowInventoryIdSeqArg = Argument("inventoryIds", ListInputType(LongType))
  val rentalRowCustomerIdArg = Argument("customerId", IntType)
  val rentalRowCustomerIdSeqArg = Argument("customerIds", ListInputType(IntType))
  val rentalRowReturnDateArg = Argument("returnDate", zonedDateTimeType)
  val rentalRowReturnDateSeqArg = Argument("returnDates", ListInputType(zonedDateTimeType))
  val rentalRowStaffIdArg = Argument("staffId", IntType)
  val rentalRowStaffIdSeqArg = Argument("staffIds", ListInputType(IntType))

  val rentalRowByStaffIdRelation = Relation[RentalRow, Int]("byStaffId", x => Seq(x.staffId))
  val rentalRowByStaffIdFetcher = Fetcher.rel[GraphQLContext, RentalRow, RentalRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.customer.RentalRowService]).getByStaffIdSeq(c.creds, rels(rentalRowByStaffIdRelation))(c.trace)
  )

  val rentalRowByInventoryIdRelation = Relation[RentalRow, Long]("byInventoryId", x => Seq(x.inventoryId))
  val rentalRowByInventoryIdFetcher = Fetcher.rel[GraphQLContext, RentalRow, RentalRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.customer.RentalRowService]).getByInventoryIdSeq(c.creds, rels(rentalRowByInventoryIdRelation))(c.trace)
  )

  val rentalRowByCustomerIdRelation = Relation[RentalRow, Int]("byCustomerId", x => Seq(x.customerId))
  val rentalRowByCustomerIdFetcher = Fetcher.rel[GraphQLContext, RentalRow, RentalRow, Long](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.customer.RentalRowService]).getByCustomerIdSeq(c.creds, rels(rentalRowByCustomerIdRelation))(c.trace)
  )

  implicit lazy val rentalRowType: sangria.schema.ObjectType[GraphQLContext, RentalRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "paymentRentalIdFkey",
        fieldType = ListType(PaymentRowSchema.paymentRowType),
        resolve = c => PaymentRowSchema.paymentRowByRentalIdFetcher.deferRelSeq(
          PaymentRowSchema.paymentRowByRentalIdRelation, c.value.rentalId
        )
      ),
      Field(
        name = "rentalStaffIdFkeyRel",
        fieldType = StaffRowSchema.staffRowType,
        resolve = ctx => StaffRowSchema.staffRowByPrimaryKeyFetcher.defer(ctx.value.staffId)
      ),
      Field(
        name = "rentalInventoryIdFkeyRel",
        fieldType = InventoryRowSchema.inventoryRowType,
        resolve = ctx => InventoryRowSchema.inventoryRowByPrimaryKeyFetcher.defer(ctx.value.inventoryId)
      ),
      Field(
        name = "rentalCustomerIdFkeyRel",
        fieldType = CustomerRowSchema.customerRowType,
        resolve = ctx => CustomerRowSchema.customerRowByPrimaryKeyFetcher.defer(ctx.value.customerId)
      )
    )
  )

  implicit lazy val rentalRowResultType: sangria.schema.ObjectType[GraphQLContext, RentalRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "rentalRow", desc = None, t = OptionType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByPrimaryKey(c.ctx.creds, c.arg(rentalRowRentalIdArg))(td)
    }, rentalRowRentalIdArg),
    unitField(name = "rentalRowSeq", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(rentalRowRentalIdSeqArg))(td)
    }, rentalRowRentalIdSeqArg),
    unitField(name = "rentalRowSearch", desc = None, t = rentalRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.customer.RentalRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "rentalRowByRentalDate", desc = None, t = OptionType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByRentalDate(c.ctx.creds, c.arg(rentalRowRentalDateArg))(td).map(_.headOption)
    }, rentalRowRentalDateArg),
    unitField(name = "rentalsByRentalDateSeq", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByRentalDateSeq(c.ctx.creds, c.arg(rentalRowRentalDateSeqArg))(td)
    }, rentalRowRentalDateSeqArg),
    unitField(name = "rentalRowByInventoryId", desc = None, t = OptionType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByInventoryId(c.ctx.creds, c.arg(rentalRowInventoryIdArg))(td).map(_.headOption)
    }, rentalRowInventoryIdArg),
    unitField(name = "rentalsByInventoryIdSeq", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByInventoryIdSeq(c.ctx.creds, c.arg(rentalRowInventoryIdSeqArg))(td)
    }, rentalRowInventoryIdSeqArg),
    unitField(name = "rentalRowByCustomerId", desc = None, t = OptionType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByCustomerId(c.ctx.creds, c.arg(rentalRowCustomerIdArg))(td).map(_.headOption)
    }, rentalRowCustomerIdArg),
    unitField(name = "rentalsByCustomerIdSeq", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByCustomerIdSeq(c.ctx.creds, c.arg(rentalRowCustomerIdSeqArg))(td)
    }, rentalRowCustomerIdSeqArg),
    unitField(name = "rentalsByReturnDate", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByReturnDate(c.ctx.creds, c.arg(rentalRowReturnDateArg))(td)
    }, rentalRowReturnDateArg),
    unitField(name = "rentalsByReturnDateSeq", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByReturnDateSeq(c.ctx.creds, c.arg(rentalRowReturnDateSeqArg))(td)
    }, rentalRowReturnDateSeqArg),
    unitField(name = "rentalsByStaffId", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByStaffId(c.ctx.creds, c.arg(rentalRowStaffIdArg))(td)
    }, rentalRowStaffIdArg),
    unitField(name = "rentalsByStaffIdSeq", desc = None, t = ListType(rentalRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.RentalRowService].getByStaffIdSeq(c.ctx.creds, c.arg(rentalRowStaffIdSeqArg))(td)
    }, rentalRowStaffIdSeqArg)
  )

  val rentalRowMutationType = ObjectType(
    name = "RentalRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(rentalRowType), f = (c, td) => {
        c.ctx.getInstance[services.customer.RentalRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(rentalRowType), f = (c, td) => {
        c.ctx.getInstance[services.customer.RentalRowService].update(c.ctx.creds, c.arg(rentalRowRentalIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, rentalRowRentalIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = rentalRowType, f = (c, td) => {
        c.ctx.getInstance[services.customer.RentalRowService].remove(c.ctx.creds, c.arg(rentalRowRentalIdArg))(td)
      }, rentalRowRentalIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "rentalRow", desc = None, t = rentalRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[RentalRow]) = {
    RentalRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
