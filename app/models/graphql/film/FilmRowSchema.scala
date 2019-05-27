/* Generated File */
package models.graphql.film

import com.kyleu.projectile.graphql.{GraphQLContext, GraphQLSchemaHelper}
import com.kyleu.projectile.graphql.GraphQLUtils._
import com.kyleu.projectile.models.graphql.note.NoteSchema
import models.film.{FilmRow, FilmRowResult}
import models.graphql.customer.LanguageRowSchema
import models.graphql.film.MpaaRatingTypeSchema.mpaaRatingTypeEnumType
import models.graphql.store.InventoryRowSchema
import sangria.execution.deferred.{Fetcher, HasId, Relation}
import sangria.schema._

object FilmRowSchema extends GraphQLSchemaHelper("filmRow") {
  implicit val filmRowPrimaryKeyId: HasId[FilmRow, Int] = HasId[FilmRow, Int](_.filmId)
  private[this] def getByPrimaryKeySeq(c: GraphQLContext, idSeq: Seq[Int]) = c.getInstance[services.film.FilmRowService].getByPrimaryKeySeq(c.creds, idSeq)(c.trace)
  val filmRowByPrimaryKeyFetcher = Fetcher(getByPrimaryKeySeq)

  val filmRowFilmIdArg = Argument("filmId", IntType)
  val filmRowFilmIdSeqArg = Argument("filmIds", ListInputType(IntType))

  val filmRowTitleArg = Argument("title", StringType)
  val filmRowTitleSeqArg = Argument("titles", ListInputType(StringType))
  val filmRowLanguageIdArg = Argument("languageId", IntType)
  val filmRowLanguageIdSeqArg = Argument("languageIds", ListInputType(IntType))
  val filmRowOriginalLanguageIdArg = Argument("originalLanguageId", IntType)
  val filmRowOriginalLanguageIdSeqArg = Argument("originalLanguageIds", ListInputType(IntType))
  val filmRowRatingArg = Argument("rating", mpaaRatingTypeEnumType)
  val filmRowRatingSeqArg = Argument("ratings", ListInputType(mpaaRatingTypeEnumType))
  val filmRowLastUpdateArg = Argument("lastUpdate", zonedDateTimeType)
  val filmRowLastUpdateSeqArg = Argument("lastUpdates", ListInputType(zonedDateTimeType))

  val filmRowByLanguageIdRelation = Relation[FilmRow, Int]("byLanguageId", x => Seq(x.languageId))
  val filmRowByLanguageIdFetcher = Fetcher.rel[GraphQLContext, FilmRow, FilmRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.film.FilmRowService]).getByLanguageIdSeq(c.creds, rels(filmRowByLanguageIdRelation))(c.trace)
  )

  val filmRowByOriginalLanguageIdRelation = Relation[FilmRow, Option[Int]]("byOriginalLanguageId", x => Seq(x.originalLanguageId))
  val filmRowByOriginalLanguageIdFetcher = Fetcher.rel[GraphQLContext, FilmRow, FilmRow, Int](
    getByPrimaryKeySeq, (c, rels) => c.injector.getInstance(classOf[services.film.FilmRowService]).getByOriginalLanguageIdSeq(c.creds, rels(filmRowByOriginalLanguageIdRelation).flatten)(c.trace)
  )

  implicit lazy val filmRowType: sangria.schema.ObjectType[GraphQLContext, FilmRow] = deriveObjectType(
    sangria.macros.derive.AddFields(
      Field(
        name = "filmActorFilmIdFkey",
        fieldType = ListType(FilmActorRowSchema.filmActorRowType),
        resolve = c => FilmActorRowSchema.filmActorRowByFilmIdFetcher.deferRelSeq(
          FilmActorRowSchema.filmActorRowByFilmIdRelation, c.value.filmId
        )
      ),
      Field(
        name = "filmCategoryFilmIdFkey",
        fieldType = ListType(FilmCategoryRowSchema.filmCategoryRowType),
        resolve = c => FilmCategoryRowSchema.filmCategoryRowByFilmIdFetcher.deferRelSeq(
          FilmCategoryRowSchema.filmCategoryRowByFilmIdRelation, c.value.filmId
        )
      ),
      Field(
        name = "inventoryFilmIdFkey",
        fieldType = ListType(InventoryRowSchema.inventoryRowType),
        resolve = c => InventoryRowSchema.inventoryRowByFilmIdFetcher.deferRelSeq(
          InventoryRowSchema.inventoryRowByFilmIdRelation, c.value.filmId
        )
      ),
      Field(
        name = "filmLanguageIdFkeyRel",
        fieldType = LanguageRowSchema.languageRowType,
        resolve = ctx => LanguageRowSchema.languageRowByPrimaryKeyFetcher.defer(ctx.value.languageId)
      ),
      Field(
        name = "filmOriginalLanguageIdFkeyRel",
        fieldType = OptionType(LanguageRowSchema.languageRowType),
        resolve = ctx => LanguageRowSchema.languageRowByPrimaryKeyFetcher.deferOpt(ctx.value.originalLanguageId)
      )
    )
  )

  implicit lazy val filmRowResultType: sangria.schema.ObjectType[GraphQLContext, FilmRowResult] = deriveObjectType()

  val queryFields = fields(
    unitField(name = "filmRow", desc = None, t = OptionType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByPrimaryKey(c.ctx.creds, c.arg(filmRowFilmIdArg))(td)
    }, filmRowFilmIdArg),
    unitField(name = "filmRowSeq", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByPrimaryKeySeq(c.ctx.creds, c.arg(filmRowFilmIdSeqArg))(td)
    }, filmRowFilmIdSeqArg),
    unitField(name = "filmRowSearch", desc = None, t = filmRowResultType, f = (c, td) => {
      runSearch(c.ctx.getInstance[services.film.FilmRowService], c, td).map(toResult)
    }, queryArg, reportFiltersArg, orderBysArg, limitArg, offsetArg),
    unitField(name = "filmsByTitle", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByTitle(c.ctx.creds, c.arg(filmRowTitleArg))(td)
    }, filmRowTitleArg),
    unitField(name = "filmsByTitleSeq", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByTitleSeq(c.ctx.creds, c.arg(filmRowTitleSeqArg))(td)
    }, filmRowTitleSeqArg),
    unitField(name = "filmsByLanguageId", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByLanguageId(c.ctx.creds, c.arg(filmRowLanguageIdArg))(td)
    }, filmRowLanguageIdArg),
    unitField(name = "filmsByLanguageIdSeq", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByLanguageIdSeq(c.ctx.creds, c.arg(filmRowLanguageIdSeqArg))(td)
    }, filmRowLanguageIdSeqArg),
    unitField(name = "filmsByOriginalLanguageId", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByOriginalLanguageId(c.ctx.creds, c.arg(filmRowOriginalLanguageIdArg))(td)
    }, filmRowOriginalLanguageIdArg),
    unitField(name = "filmsByOriginalLanguageIdSeq", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByOriginalLanguageIdSeq(c.ctx.creds, c.arg(filmRowOriginalLanguageIdSeqArg))(td)
    }, filmRowOriginalLanguageIdSeqArg),
    unitField(name = "filmsByRating", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByRating(c.ctx.creds, c.arg(filmRowRatingArg))(td)
    }, filmRowRatingArg),
    unitField(name = "filmsByRatingSeq", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByRatingSeq(c.ctx.creds, c.arg(filmRowRatingSeqArg))(td)
    }, filmRowRatingSeqArg),
    unitField(name = "filmsByLastUpdate", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByLastUpdate(c.ctx.creds, c.arg(filmRowLastUpdateArg))(td)
    }, filmRowLastUpdateArg),
    unitField(name = "filmsByLastUpdateSeq", desc = None, t = ListType(filmRowType), f = (c, td) => {
      c.ctx.getInstance[services.film.FilmRowService].getByLastUpdateSeq(c.ctx.creds, c.arg(filmRowLastUpdateSeqArg))(td)
    }, filmRowLastUpdateSeqArg)
  )

  val filmRowMutationType = ObjectType(
    name = "FilmRowMutations",
    fields = fields(
      unitField(name = "create", desc = None, t = OptionType(filmRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.FilmRowService].create(c.ctx.creds, c.arg(dataFieldsArg))(td)
      }, dataFieldsArg),
      unitField(name = "update", desc = None, t = OptionType(filmRowType), f = (c, td) => {
        c.ctx.getInstance[services.film.FilmRowService].update(c.ctx.creds, c.arg(filmRowFilmIdArg), c.arg(dataFieldsArg))(td).map(_._1)
      }, filmRowFilmIdArg, dataFieldsArg),
      unitField(name = "remove", desc = None, t = filmRowType, f = (c, td) => {
        c.ctx.getInstance[services.film.FilmRowService].remove(c.ctx.creds, c.arg(filmRowFilmIdArg))(td)
      }, filmRowFilmIdArg)
    )
  )

  val mutationFields = fields(unitField(name = "filmRow", desc = None, t = filmRowMutationType, f = (_, _) => scala.concurrent.Future.successful(())))

  private[this] def toResult(r: GraphQLSchemaHelper.SearchResult[FilmRow]) = {
    FilmRowResult(paging = r.paging, filters = r.args.filters, orderBys = r.args.orderBys, totalCount = r.count, results = r.results, durationMs = r.dur)
  }
}
