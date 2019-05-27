/* Generated File */
package models.graphql.customer

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.customer.{CustomerRow, CustomerRowResult}
import models.graphql.address.AddressRowSchema
import models.graphql.payment.PaymentRowSchema
import models.graphql.store.StoreRowSchema
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object CustomerRowSchema extends GraphQLSchemaHelper("customerRow") {
  implicit val customerRowPrimaryKeyId: HasId[CustomerRow, Int] = HasId[CustomerRow, Int](_.customerId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.customer.CustomerRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val customerRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val customerRowCustomerIdArg = Argument("customerId", IntType)
  val customerRowCustomerIdSeqArg = Argument("customerIds", ListInputType(IntType))

  val customerRowStoreIdArg = Argument("storeId", IntType)
  val customerRowStoreIdSeqArg = Argument("storeIds", ListInputType(IntType))
  val customerRowFirstNameArg = Argument("firstName", StringType)
  val customerRowFirstNameSeqArg = Argument("firstNames", ListInputType(StringType))
  val customerRowLastNameArg = Argument("lastName", StringType)
  val customerRowLastNameSeqArg = Argument("lastNames", ListInputType(StringType))
  val customerRowEmailArg = Argument("email", StringType)
  val customerRowEmailSeqArg = Argument("emails", ListInputType(StringType))
  val customerRowAddressIdArg = Argument("addressId", IntType)
  val customerRowAddressIdSeqArg = Argument("addressIds", ListInputType(IntType))

  val customerRowByStoreIdRelation = Relation[CustomerRow, Int]("byStoreId", x => Seq(x.storeId))
  val customerRowByStoreIdFetcher = Fetcher.rel[GraphQLContext, CustomerRow, CustomerRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.customer.CustomerRowService]).getByStoreIdSeq(c.creds, rels(customerRowByStoreIdRelation))(c.trace)
  )

  val customerRowByAddressIdRelation = Relation[CustomerRow, Int]("byAddressId", x => Seq(x.addressId))
  val customerRowByAddressIdFetcher = Fetcher.rel[GraphQLContext, CustomerRow, CustomerRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.customer.CustomerRowService]).getByAddressIdSeq(c.creds, rels(customerRowByAddressIdRelation))(c.trace)
  )

  implicit lazy val customerRowType: sangria.schema.ObjectType[GraphQLContext, CustomerRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "paymentCustomerIdFkey",
        fieldType = ListType(PaymentRowSchema.paymentRowType),
        resolve = c => PaymentRowSchema.paymentRowByCustomerIdFetcher.deferRelSeq(
          PaymentRowSchema.paymentRowByCustomerIdRelation, c.value.customerId
        )
      ),
      Field(
        name = "rentalCustomerIdFkey",
        fieldType = ListType(RentalRowSchema.rentalRowType),
        resolve = c => RentalRowSchema.rentalRowByCustomerIdFetcher.deferRelSeq(
          RentalRowSchema.rentalRowByCustomerIdRelation, c.value.customerId
        )
      ),
      Field(
        name = "customerStoreIdFkeyRel",
        fieldType = StoreRowSchema.storeRowType,
        resolve = ctx => StoreRowSchema.storeRowByPrimaryKeyFetcher.defer(ctx.value.storeId)
      ),
      Field(
        name = "customerAddressIdFkeyRel",
        fieldType = AddressRowSchema.addressRowType,
        resolve = ctx => AddressRowSchema.addressRowByPrimaryKeyFetcher.defer(ctx.value.addressId)
      )
    )
  )

  implicit lazy val customerRowResultType: sangria.schema.ObjectType[GraphQLContext, CustomerRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "customerRow", desc = None, t = OptionType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByPrimaryKey(c.ctx.creds, c.arg(customerRowCustomerIdArg))(td)
    }, customerRowCustomerIdArg),
    unitField(name = "customerRowSeq", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(customerRowCustomerIdSeqArg))(td)
    }, customerRowCustomerIdSeqArg),
    unitField(name = "customerRowSearch", desc = None, t = customerRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.customer.CustomerRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "customersByStoreId", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByStoreId(c.ctx.creds, c.arg(customerRowStoreIdArg))(td)
    }, customerRowStoreIdArg),
    unitField(name = "customersByStoreIdSeq", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByStoreIdSeq(c.ctx.creds, c.arg(customerRowStoreIdSeqArg))(td)
    }, customerRowStoreIdSeqArg),
    unitField(name = "customersByFirstName", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByFirstName(c.ctx.creds, c.arg(customerRowFirstNameArg))(td)
    }, customerRowFirstNameArg),
    unitField(name = "customersByFirstNameSeq", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByFirstNameSeq(c.ctx.creds, c.arg(customerRowFirstNameSeqArg))(td)
    }, customerRowFirstNameSeqArg),
    unitField(name = "customersByLastName", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByLastName(c.ctx.creds, c.arg(customerRowLastNameArg))(td)
    }, customerRowLastNameArg),
    unitField(name = "customersByLastNameSeq", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByLastNameSeq(c.ctx.creds, c.arg(customerRowLastNameSeqArg))(td)
    }, customerRowLastNameSeqArg),
    unitField(name = "customersByEmail", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByEmail(c.ctx.creds, c.arg(customerRowEmailArg))(td)
    }, customerRowEmailArg),
    unitField(name = "customersByEmailSeq", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByEmailSeq(c.ctx.creds, c.arg(customerRowEmailSeqArg))(td)
    }, customerRowEmailSeqArg),
    unitField(name = "customersByAddressId", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByAddressId(c.ctx.creds, c.arg(customerRowAddressIdArg))(td)
    }, customerRowAddressIdArg),
    unitField(name = "customersByAddressIdSeq", desc = None, t = ListType(customerRowType), f = (c, td) => {
      c.ctx.getInstance[services.customer.CustomerRowService].getByAddressIdSeq(c.ctx.creds, c.arg(customerRowAddressIdSeqArg))(td)
    }, customerRowAddressIdSeqArg)
  )

  val customerRowMutationType = ObjectType(
    name = "CustomerRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(customerRowType), f = (c, td) => {
        c.ctx.getInstance[services.customer.CustomerRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(customerRowType), f = (c, td) => {
        c.ctx.getInstance[services.customer.CustomerRowService].update(c.ctx.creds, c.arg(customerRowCustomerIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, customerRowCustomerIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = customerRowType, f = (c, td) => {
        c.ctx.getInstance[services.customer.CustomerRowService].remove(c.ctx.creds, c.arg(customerRowCustomerIdArg))(td)
      }, customerRowCustomerIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "customerRow", desc = None, t = customerRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[CustomerRow]) = {
    CustomerRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
