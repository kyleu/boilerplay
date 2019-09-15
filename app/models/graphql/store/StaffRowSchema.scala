/* Generated File */
package models.graphql.store

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.graphql.address.AddressRowSchema
import models.graphql.customer.RentalRowSchema
import models.graphql.payment.PaymentRowSchema
import models.store.{StaffRow, StaffRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object StaffRowSchema extends GraphQLSchemaHelper("staffRow") {
  implicit val staffRowPrimaryKeyId: HasId[StaffRow, Int] = HasId[StaffRow, Int](_.staffId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.store.StaffRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val staffRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val staffRowStaffIdArg = Argument("staffId", IntType)
  val staffRowStaffIdSeqArg = Argument("staffIds", ListInputType(IntType))

  val staffRowFirstNameArg = Argument("firstName", StringType)
  val staffRowFirstNameSeqArg = Argument("firstNames", ListInputType(StringType))
  val staffRowLastNameArg = Argument("lastName", StringType)
  val staffRowLastNameSeqArg = Argument("lastNames", ListInputType(StringType))
  val staffRowAddressIdArg = Argument("addressId", IntType)
  val staffRowAddressIdSeqArg = Argument("addressIds", ListInputType(IntType))
  val staffRowEmailArg = Argument("email", StringType)
  val staffRowEmailSeqArg = Argument("emails", ListInputType(StringType))
  val staffRowStoreIdArg = Argument("storeId", IntType)
  val staffRowStoreIdSeqArg = Argument("storeIds", ListInputType(IntType))
  val staffRowUsernameArg = Argument("username", StringType)
  val staffRowUsernameSeqArg = Argument("usernames", ListInputType(StringType))

  val staffRowByStoreIdRelation = Relation[StaffRow, Int]("byStoreId", x => Seq(x.storeId))
  val staffRowByStoreIdFetcher = Fetcher.rel[GraphQLContext, StaffRow, StaffRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.store.StaffRowService]).getByStoreIdSeq(c.creds, rels(staffRowByStoreIdRelation))(c.trace)
  )

  val staffRowByAddressIdRelation = Relation[StaffRow, Int]("byAddressId", x => Seq(x.addressId))
  val staffRowByAddressIdFetcher = Fetcher.rel[GraphQLContext, StaffRow, StaffRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.store.StaffRowService]).getByAddressIdSeq(c.creds, rels(staffRowByAddressIdRelation))(c.trace)
  )

  implicit lazy val staffRowType: sangria.schema.ObjectType[GraphQLContext, StaffRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "payments",
        fieldType = ListType(PaymentRowSchema.paymentRowType),
        resolve = c => PaymentRowSchema.paymentRowByStaffIdFetcher.deferRelSeq(
          PaymentRowSchema.paymentRowByStaffIdRelation, c.value.staffId
        )
      ),
      Field(
        name = "rentals",
        fieldType = ListType(RentalRowSchema.rentalRowType),
        resolve = c => RentalRowSchema.rentalRowByStaffIdFetcher.deferRelSeq(
          RentalRowSchema.rentalRowByStaffIdRelation, c.value.staffId
        )
      ),
      Field(
        name = "store",
        fieldType = StoreRowSchema.storeRowType,
        resolve = ctx => StoreRowSchema.storeRowByPrimaryKeyFetcher.defer(ctx.value.storeId)
      ),
      Field(
        name = "address",
        fieldType = AddressRowSchema.addressRowType,
        resolve = ctx => AddressRowSchema.addressRowByPrimaryKeyFetcher.defer(ctx.value.addressId)
      )
    )
  )

  implicit lazy val staffRowResultType: sangria.schema.ObjectType[GraphQLContext, StaffRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "staffRow", desc = None, t = OptionType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByPrimaryKey(c.ctx.creds, c.arg(staffRowStaffIdArg))(td)
    }, staffRowStaffIdArg),
    unitField(name = "staffRowSeq", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(staffRowStaffIdSeqArg))(td)
    }, staffRowStaffIdSeqArg),
    unitField(name = "staffRowSearch", desc = None, t = staffRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.store.StaffRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "staffByFirstName", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByFirstName(c.ctx.creds, c.arg(staffRowFirstNameArg))(td)
    }, staffRowFirstNameArg),
    unitField(name = "staffByFirstNameSeq", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByFirstNameSeq(c.ctx.creds, c.arg(staffRowFirstNameSeqArg))(td)
    }, staffRowFirstNameSeqArg),
    unitField(name = "staffByLastName", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByLastName(c.ctx.creds, c.arg(staffRowLastNameArg))(td)
    }, staffRowLastNameArg),
    unitField(name = "staffByLastNameSeq", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByLastNameSeq(c.ctx.creds, c.arg(staffRowLastNameSeqArg))(td)
    }, staffRowLastNameSeqArg),
    unitField(name = "staffByAddressId", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByAddressId(c.ctx.creds, c.arg(staffRowAddressIdArg))(td)
    }, staffRowAddressIdArg),
    unitField(name = "staffByAddressIdSeq", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByAddressIdSeq(c.ctx.creds, c.arg(staffRowAddressIdSeqArg))(td)
    }, staffRowAddressIdSeqArg),
    unitField(name = "staffByEmail", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByEmail(c.ctx.creds, c.arg(staffRowEmailArg))(td)
    }, staffRowEmailArg),
    unitField(name = "staffByEmailSeq", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByEmailSeq(c.ctx.creds, c.arg(staffRowEmailSeqArg))(td)
    }, staffRowEmailSeqArg),
    unitField(name = "staffByStoreId", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByStoreId(c.ctx.creds, c.arg(staffRowStoreIdArg))(td)
    }, staffRowStoreIdArg),
    unitField(name = "staffByStoreIdSeq", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByStoreIdSeq(c.ctx.creds, c.arg(staffRowStoreIdSeqArg))(td)
    }, staffRowStoreIdSeqArg),
    unitField(name = "staffByUsername", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByUsername(c.ctx.creds, c.arg(staffRowUsernameArg))(td)
    }, staffRowUsernameArg),
    unitField(name = "staffByUsernameSeq", desc = None, t = ListType(staffRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StaffRowService].getByUsernameSeq(c.ctx.creds, c.arg(staffRowUsernameSeqArg))(td)
    }, staffRowUsernameSeqArg)
  )

  val staffRowMutationType = ObjectType(
    name = "StaffRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(staffRowType), f = (c, td) => {
        c.ctx.getInstance[services.store.StaffRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(staffRowType), f = (c, td) => {
        c.ctx.getInstance[services.store.StaffRowService].update(c.ctx.creds, c.arg(staffRowStaffIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, staffRowStaffIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = staffRowType, f = (c, td) => {
        c.ctx.getInstance[services.store.StaffRowService].remove(c.ctx.creds, c.arg(staffRowStaffIdArg))(td)
      }, staffRowStaffIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "staffRow", desc = None, t = staffRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[StaffRow]) = {
    StaffRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
