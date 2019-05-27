package models.graphql

import com.kyleu.projectile.graphql.GraphQLContext
import com.kyleu.projectile.models.graphql.BaseGraphQLSchema
import sangria.schema._
import util.Version

import scala.concurrent.Future

object Schema extends BaseGraphQLSchema {
  override protected def additionalFetchers = {
    Nil ++
      /* Start model fetchers */
      /* Projectile export section [boilerplay] */
      Seq(
        models.graphql.address.AddressRowSchema.addressRowByCityIdFetcher,
        models.graphql.address.AddressRowSchema.addressRowByPrimaryKeyFetcher,
        models.graphql.address.CityRowSchema.cityRowByCountryIdFetcher,
        models.graphql.address.CityRowSchema.cityRowByPrimaryKeyFetcher,
        models.graphql.address.CountryRowSchema.countryRowByPrimaryKeyFetcher,
        models.graphql.customer.CustomerRowSchema.customerRowByAddressIdFetcher,
        models.graphql.customer.CustomerRowSchema.customerRowByPrimaryKeyFetcher,
        models.graphql.customer.CustomerRowSchema.customerRowByStoreIdFetcher,
        models.graphql.customer.LanguageRowSchema.languageRowByPrimaryKeyFetcher,
        models.graphql.customer.RentalRowSchema.rentalRowByCustomerIdFetcher,
        models.graphql.customer.RentalRowSchema.rentalRowByInventoryIdFetcher,
        models.graphql.customer.RentalRowSchema.rentalRowByPrimaryKeyFetcher,
        models.graphql.customer.RentalRowSchema.rentalRowByStaffIdFetcher,
        models.graphql.film.ActorRowSchema.actorRowByPrimaryKeyFetcher,
        models.graphql.film.CategoryRowSchema.categoryRowByPrimaryKeyFetcher,
        models.graphql.film.FilmActorRowSchema.filmActorRowByActorIdFetcher,
        models.graphql.film.FilmActorRowSchema.filmActorRowByFilmIdFetcher,
        models.graphql.film.FilmActorRowSchema.filmActorRowByPrimaryKeyFetcher,
        models.graphql.film.FilmCategoryRowSchema.filmCategoryRowByCategoryIdFetcher,
        models.graphql.film.FilmCategoryRowSchema.filmCategoryRowByFilmIdFetcher,
        models.graphql.film.FilmCategoryRowSchema.filmCategoryRowByPrimaryKeyFetcher,
        models.graphql.film.FilmRowSchema.filmRowByLanguageIdFetcher,
        models.graphql.film.FilmRowSchema.filmRowByOriginalLanguageIdFetcher,
        models.graphql.film.FilmRowSchema.filmRowByPrimaryKeyFetcher,
        models.graphql.payment.PaymentRowSchema.paymentRowByCustomerIdFetcher,
        models.graphql.payment.PaymentRowSchema.paymentRowByPrimaryKeyFetcher,
        models.graphql.payment.PaymentRowSchema.paymentRowByRentalIdFetcher,
        models.graphql.payment.PaymentRowSchema.paymentRowByStaffIdFetcher,
        models.graphql.store.InventoryRowSchema.inventoryRowByFilmIdFetcher,
        models.graphql.store.InventoryRowSchema.inventoryRowByPrimaryKeyFetcher,
        models.graphql.store.InventoryRowSchema.inventoryRowByStoreIdFetcher,
        models.graphql.store.StaffRowSchema.staffRowByAddressIdFetcher,
        models.graphql.store.StaffRowSchema.staffRowByPrimaryKeyFetcher,
        models.graphql.store.StaffRowSchema.staffRowByStoreIdFetcher,
        models.graphql.store.StoreRowSchema.storeRowByAddressIdFetcher,
        models.graphql.store.StoreRowSchema.storeRowByPrimaryKeyFetcher
      ) ++
        /* End model fetchers */
        Nil
  }

  override protected def additionalQueryFields = fields[GraphQLContext, Unit](
    Field(name = "status", fieldType = StringType, resolve = c => Future.successful("OK")),
    Field(name = "version", fieldType = StringType, resolve = _ => Future.successful(Version.version))
  ) ++
    /* Start query fields */
    /* Projectile export section [boilerplay] */
    models.graphql.film.MpaaRatingTypeSchema.queryFields ++
    models.graphql.address.AddressRowSchema.queryFields ++
    models.graphql.address.CityRowSchema.queryFields ++
    models.graphql.address.CountryRowSchema.queryFields ++
    models.graphql.customer.CustomerRowSchema.queryFields ++
    models.graphql.customer.LanguageRowSchema.queryFields ++
    models.graphql.customer.RentalRowSchema.queryFields ++
    models.graphql.film.ActorRowSchema.queryFields ++
    models.graphql.film.CategoryRowSchema.queryFields ++
    models.graphql.film.FilmActorRowSchema.queryFields ++
    models.graphql.film.FilmCategoryRowSchema.queryFields ++
    models.graphql.film.FilmRowSchema.queryFields ++
    models.graphql.payment.PaymentRowSchema.queryFields ++
    models.graphql.store.InventoryRowSchema.queryFields ++
    models.graphql.store.StaffRowSchema.queryFields ++
    models.graphql.store.StoreRowSchema.queryFields ++
    /* End query fields */
    Nil

  override protected def additionalMutationFields = {
    Nil ++
      /* Start mutation fields */
      /* Projectile export section [boilerplay] */
      models.graphql.address.AddressRowSchema.mutationFields ++
      models.graphql.address.CityRowSchema.mutationFields ++
      models.graphql.address.CountryRowSchema.mutationFields ++
      models.graphql.customer.CustomerRowSchema.mutationFields ++
      models.graphql.customer.LanguageRowSchema.mutationFields ++
      models.graphql.customer.RentalRowSchema.mutationFields ++
      models.graphql.film.ActorRowSchema.mutationFields ++
      models.graphql.film.CategoryRowSchema.mutationFields ++
      models.graphql.film.FilmActorRowSchema.mutationFields ++
      models.graphql.film.FilmCategoryRowSchema.mutationFields ++
      models.graphql.film.FilmRowSchema.mutationFields ++
      models.graphql.payment.PaymentRowSchema.mutationFields ++
      models.graphql.store.InventoryRowSchema.mutationFields ++
      models.graphql.store.StaffRowSchema.mutationFields ++
      models.graphql.store.StoreRowSchema.mutationFields ++
      /* End mutation fields */
      Nil
  }
}
