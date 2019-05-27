/* Generated File */
package models.graphql.store

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.graphql.address.AddressRowSchema
import models.graphql.customer.CustomerRowSchema
import models.store.{StoreRow, StoreRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object StoreRowSchema extends GraphQLSchemaHelper("storeRow") {
  implicit val storeRowPrimaryKeyId: HasId[StoreRow, Int] = HasId[StoreRow, Int](_.storeId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.store.StoreRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val storeRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val storeRowStoreIdArg = Argument("storeId", IntType)
  val storeRowStoreIdSeqArg = Argument("storeIds", ListInputType(IntType))

  val storeRowManagerStaffIdArg = Argument("managerStaffId", IntType)
  val storeRowManagerStaffIdSeqArg = Argument("managerStaffIds", ListInputType(IntType))
  val storeRowAddressIdArg = Argument("addressId", IntType)
  val storeRowAddressIdSeqArg = Argument("addressIds", ListInputType(IntType))
  val storeRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val storeRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  val storeRowByAddressIdRelation = Relation[StoreRow, Int]("byAddressId", x => Seq(x.addressId))
  val storeRowByAddressIdFetcher = Fetcher.rel[GraphQLContext, StoreRow, StoreRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.store.StoreRowService]).getByAddressIdSeq(c.creds, rels(storeRowByAddressIdRelation))(c.trace)
  )

  implicit lazy val storeRowType: sangria.schema.ObjectType[GraphQLContext, StoreRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "customerStoreIdFkey",
        fieldType = ListType(CustomerRowSchema.customerRowType),
        resolve = c => CustomerRowSchema.customerRowByStoreIdFetcher.deferRelSeq(
          CustomerRowSchema.customerRowByStoreIdRelation, c.value.storeId
        )
      ),
      Field(
        name = "inventoryStoreIdFkey",
        fieldType = ListType(InventoryRowSchema.inventoryRowType),
        resolve = c => InventoryRowSchema.inventoryRowByStoreIdFetcher.deferRelSeq(
          InventoryRowSchema.inventoryRowByStoreIdRelation, c.value.storeId
        )
      ),
      Field(
        name = "staffStoreIdFkey",
        fieldType = ListType(StaffRowSchema.staffRowType),
        resolve = c => StaffRowSchema.staffRowByStoreIdFetcher.deferRelSeq(
          StaffRowSchema.staffRowByStoreIdRelation, c.value.storeId
        )
      ),
      Field(
        name = "storeAddressIdFkeyRel",
        fieldType = AddressRowSchema.addressRowType,
        resolve = ctx => AddressRowSchema.addressRowByPrimaryKeyFetcher.defer(ctx.value.addressId)
      )
    )
  )

  implicit lazy val storeRowResultType: sangria.schema.ObjectType[GraphQLContext, StoreRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "storeRow", desc = None, t = OptionType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByPrimaryKey(c.ctx.creds, c.arg(storeRowStoreIdArg))(td)
    }, storeRowStoreIdArg),
    unitField(name = "storeRowSeq", desc = None, t = ListType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(storeRowStoreIdSeqArg))(td)
    }, storeRowStoreIdSeqArg),
    unitField(name = "storeRowSearch", desc = None, t = storeRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.store.StoreRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "storeRowByManagerStaffId", desc = None, t = OptionType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByManagerStaffId(c.ctx.creds, c.arg(storeRowManagerStaffIdArg))(td).map(_.headOption)
    }, storeRowManagerStaffIdArg),
    unitField(name = "storesByManagerStaffIdSeq", desc = None, t = ListType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByManagerStaffIdSeq(c.ctx.creds, c.arg(storeRowManagerStaffIdSeqArg))(td)
    }, storeRowManagerStaffIdSeqArg),
    unitField(name = "storesByAddressId", desc = None, t = ListType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByAddressId(c.ctx.creds, c.arg(storeRowAddressIdArg))(td)
    }, storeRowAddressIdArg),
    unitField(name = "storesByAddressIdSeq", desc = None, t = ListType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByAddressIdSeq(c.ctx.creds, c.arg(storeRowAddressIdSeqArg))(td)
    }, storeRowAddressIdSeqArg),
    unitField(name = "storesByLastUpdate", desc = None, t = ListType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByLastUpdate(c.ctx.creds, c.arg(storeRowLastUpdateArg))(td)
    }, storeRowLastUpdateArg),
    unitField(name = "storesByLastUpdateSeq", desc = None, t = ListType(storeRowType), f = (c, td) => {
      c.ctx.getInstance[services.store.StoreRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(storeRowLastUpdateSeqArg))(td)
    }, storeRowLastUpdateSeqArg)
  )

  val storeRowMutationType = ObjectType(
    name = "StoreRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(storeRowType), f = (c, td) => {
        c.ctx.getInstance[services.store.StoreRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(storeRowType), f = (c, td) => {
        c.ctx.getInstance[services.store.StoreRowService].update(c.ctx.creds, c.arg(storeRowStoreIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, storeRowStoreIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = storeRowType, f = (c, td) => {
        c.ctx.getInstance[services.store.StoreRowService].remove(c.ctx.creds, c.arg(storeRowStoreIdArg))(td)
      }, storeRowStoreIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "storeRow", desc = None, t = storeRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[StoreRow]) = {
    StoreRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
