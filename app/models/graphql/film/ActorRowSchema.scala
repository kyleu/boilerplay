/* Generated File */
package models.graphql.film

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.film.{ActorRow, ActorRowResult}
import sangria.execution.deferred.{Fetcher, HasId}
import sangria.schema._

object ActorRowSchema extends GraphQLSchemaHelper("actorRow") {
  implicit val actorRowPrimaryKeyId: HasId[ActorRow, Int] = HasId[ActorRow, Int](_.actorId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.film.ActorRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val actorRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val actorRowActorIdArg = Argument("actorId", IntType)
  val actorRowActorIdSeqArg = Argument("actorIds", ListInputType(IntType))

  val actorRowFirstNameArg = Argument("firstName", StringType)
  val actorRowFirstNameSeqArg = Argument("firstNames", ListInputType(StringType))
  val actorRowLastNameArg = Argument("lastName", StringType)
  val actorRowLastNameSeqArg = Argument("lastNames", ListInputType(StringType))
  val actorRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val actorRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  implicit lazy val actorRowType: sangria.schema.ObjectType[GraphQLContext, ActorRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "films",
        fieldType = ListType(FilmActorRowSchema.filmActorRowType),
        resolve = c => FilmActorRowSchema.filmActorRowByActorIdFetcher.deferRelSeq(
          FilmActorRowSchema.filmActorRowByActorIdRelation, c.value.actorId
        )
      )
    )
  )

  implicit lazy val actorRowResultType: sangria.schema.ObjectType[GraphQLContext, ActorRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "actorRow", desc = None, t = OptionType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByPrimaryKey(c.ctx.creds, c.arg(actorRowActorIdArg))(td)
    }, actorRowActorIdArg),
    unitField(name = "actorRowSeq", desc = None, t = ListType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(actorRowActorIdSeqArg))(td)
    }, actorRowActorIdSeqArg),
    unitField(name = "actorRowSearch", desc = None, t = actorRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.film.ActorRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "actorsByFirstName", desc = None, t = ListType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByFirstName(c.ctx.creds, c.arg(actorRowFirstNameArg))(td)
    }, actorRowFirstNameArg),
    unitField(name = "actorsByFirstNameSeq", desc = None, t = ListType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByFirstNameSeq(c.ctx.creds, c.arg(actorRowFirstNameSeqArg))(td)
    }, actorRowFirstNameSeqArg),
    unitField(name = "actorsByLastName", desc = None, t = ListType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByLastName(c.ctx.creds, c.arg(actorRowLastNameArg))(td)
    }, actorRowLastNameArg),
    unitField(name = "actorsByLastNameSeq", desc = None, t = ListType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByLastNameSeq(c.ctx.creds, c.arg(actorRowLastNameSeqArg))(td)
    }, actorRowLastNameSeqArg),
    unitField(name = "actorsByLastUpdate", desc = None, t = ListType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByLastUpdate(c.ctx.creds, c.arg(actorRowLastUpdateArg))(td)
    }, actorRowLastUpdateArg),
    unitField(name = "actorsByLastUpdateSeq", desc = None, t = ListType(actorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.ActorRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(actorRowLastUpdateSeqArg))(td)
    }, actorRowLastUpdateSeqArg)
  )

  val actorRowMutationType = ObjectType(
    name = "ActorRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(actorRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.ActorRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(actorRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.ActorRowService].update(c.ctx.creds, c.arg(actorRowActorIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, actorRowActorIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = actorRowType, f = (c, td) => {
        c.ctx.getInstance[services.film.ActorRowService].remove(c.ctx.creds, c.arg(actorRowActorIdArg))(td)
      }, actorRowActorIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "actorRow", desc = None, t = actorRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[ActorRow]) = {
    ActorRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
