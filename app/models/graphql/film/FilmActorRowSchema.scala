/* Generated File */
package models.graphql.film

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import models.film.{FilmActorRow, FilmActorRowResult}
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object FilmActorRowSchema extends GraphQLSchemaHelper("filmActorRow") {
  implicit val filmActorRowPrimaryKeyId: HasId[FilmActorRow, (Int, Int)] = HasId[FilmActorRow, (Int, Int)](x => (x.actorId, x.filmId))
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[(Int, Int)]) = c.getInstance[services.film.FilmActorRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val filmActorRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val filmActorRowActorIdArg = Argument("actorId", IntType)
  val filmActorRowActorIdSeqArg = Argument("actorIds", ListInputType(IntType))
  val filmActorRowFilmIdArg = Argument("filmId", IntType)
  val filmActorRowFilmIdSeqArg = Argument("filmIds", ListInputType(IntType))

  val filmActorRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val filmActorRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  val filmActorRowByFilmIdRelation = Relation[FilmActorRow, Int]("byFilmId", x => Seq(x.filmId))
  val filmActorRowByFilmIdFetcher = Fetcher.rel[GraphQLContext, FilmActorRow, FilmActorRow, (Int, Int)](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.film.FilmActorRowService]).getByFilmIdSeq(c.creds, rels(filmActorRowByFilmIdRelation))(c.trace)
  )

  val filmActorRowByActorIdRelation = Relation[FilmActorRow, Int]("byActorId", x => Seq(x.actorId))
  val filmActorRowByActorIdFetcher = Fetcher.rel[GraphQLContext, FilmActorRow, FilmActorRow, (Int, Int)](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.film.FilmActorRowService]).getByActorIdSeq(c.creds, rels(filmActorRowByActorIdRelation))(c.trace)
  )

  implicit lazy val filmActorRowType: sangria.schema.ObjectType[GraphQLContext, FilmActorRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "film",
        fieldType = FilmRowSchema.filmRowType,
        resolve = ctx => FilmRowSchema.filmRowByPrimaryKeyFetcher.defer(ctx.value.filmId)
      ),
      Field(
        name = "actor",
        fieldType = ActorRowSchema.actorRowType,
        resolve = ctx => ActorRowSchema.actorRowByPrimaryKeyFetcher.defer(ctx.value.actorId)
      )
    )
  )

  implicit lazy val filmActorRowResultType: sangria.schema.ObjectType[GraphQLContext, FilmActorRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "filmActorRow", desc = None, t = OptionType(filmActorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmActorRowService].getByPrimaryKey(c.ctx.creds, c.arg(filmActorRowActorIdArg), c.arg(filmActorRowFilmIdArg))(td)
    }, filmActorRowActorIdArg, filmActorRowFilmIdArg),
    unitField(name = "filmActorRowSearch", desc = None, t = filmActorRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.film.FilmActorRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "filmActorsByLastUpdate", desc = None, t = ListType(filmActorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmActorRowService].getByLastUpdate(c.ctx.creds, c.arg(filmActorRowLastUpdateArg))(td)
    }, filmActorRowLastUpdateArg),
    unitField(name = "filmActorsByLastUpdateSeq", desc = None, t = ListType(filmActorRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmActorRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(filmActorRowLastUpdateSeqArg))(td)
    }, filmActorRowLastUpdateSeqArg)
  )

  val filmActorRowMutationType = ObjectType(
    name = "FilmActorRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(filmActorRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.FilmActorRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(filmActorRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.FilmActorRowService].update(c.ctx.creds, c.arg(filmActorRowActorIdArg), c.arg(filmActorRowFilmIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, filmActorRowActorIdArg, filmActorRowFilmIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = filmActorRowType, f = (c, td) => {
        c.ctx.getInstance[services.film.FilmActorRowService].remove(c.ctx.creds, c.arg(filmActorRowActorIdArg), c.arg(filmActorRowFilmIdArg))(td)
      }, filmActorRowActorIdArg, filmActorRowFilmIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "filmActorRow", desc = None, t = filmActorRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[FilmActorRow]) = {
    FilmActorRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
