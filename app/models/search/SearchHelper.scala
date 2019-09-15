package models.search

import java.util.UUID

import com.google.inject.Injector
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.util.Credentials
import com.kyleu.projectile.util.tracing.TraceData
import com.kyleu.projectile.services.search.SearchProvider
import play.api.mvc.Call
import play.twirl.api.Html

import scala.concurrent.{ExecutionContext, Future}

class SearchHelper extends SearchProvider {
  override def intSearches(app: Application, injector: Injector, creds: Credentials)(q: String, id: Int)(implicit ec: ExecutionContext, td: TraceData): Seq[Future[Seq[(Call, Html)]]] = {
    /* Start int searches */
    /* Projectile export section [boilerplay] */
    Seq(
      act[services.film.ActorRowService, models.film.ActorRow](injector = injector, creds = creds, perm = ("film", "ActorRow", "view"), f = _.getByActorId(creds, id), v = model => controllers.admin.film.routes.ActorRowController.view(model.actorId), s = model => views.html.admin.film.actorRowSearchResult(model, s"Actor [${model.actorId}] matched actorId [$q]")),
      act[services.address.AddressRowService, models.address.AddressRow](injector = injector, creds = creds, perm = ("address", "AddressRow", "view"), f = _.getByAddressId(creds, id), v = model => controllers.admin.address.routes.AddressRowController.view(model.addressId), s = model => views.html.admin.address.addressRowSearchResult(model, s"Address [${model.addressId}] matched addressId [$q]")),
      act[services.film.CategoryRowService, models.film.CategoryRow](injector = injector, creds = creds, perm = ("film", "CategoryRow", "view"), f = _.getByCategoryId(creds, id), v = model => controllers.admin.film.routes.CategoryRowController.view(model.categoryId), s = model => views.html.admin.film.categoryRowSearchResult(model, s"Category [${model.categoryId}] matched categoryId [$q]")),
      act[services.address.CityRowService, models.address.CityRow](injector = injector, creds = creds, perm = ("address", "CityRow", "view"), f = _.getByCityId(creds, id), v = model => controllers.admin.address.routes.CityRowController.view(model.cityId), s = model => views.html.admin.address.cityRowSearchResult(model, s"City [${model.cityId}] matched cityId [$q]")),
      act[services.address.CountryRowService, models.address.CountryRow](injector = injector, creds = creds, perm = ("address", "CountryRow", "view"), f = _.getByCountryId(creds, id), v = model => controllers.admin.address.routes.CountryRowController.view(model.countryId), s = model => views.html.admin.address.countryRowSearchResult(model, s"Country [${model.countryId}] matched countryId [$q]")),
      act[services.customer.CustomerRowService, models.customer.CustomerRow](injector = injector, creds = creds, perm = ("customer", "CustomerRow", "view"), f = _.getByCustomerId(creds, id), v = model => controllers.admin.customer.routes.CustomerRowController.view(model.customerId), s = model => views.html.admin.customer.customerRowSearchResult(model, s"Customer [${model.customerId}] matched customerId [$q]")),
      act[services.film.FilmActorRowService, models.film.FilmActorRow](injector = injector, creds = creds, perm = ("film", "FilmActorRow", "view"), f = _.getByActorId(creds, id), v = model => controllers.admin.film.routes.FilmActorRowController.view(model.actorId, model.filmId), s = model => views.html.admin.film.filmActorRowSearchResult(model, s"Film Actor [${model.actorId}, ${model.filmId}] matched actorId [$q]")),
      act[services.film.FilmActorRowService, models.film.FilmActorRow](injector = injector, creds = creds, perm = ("film", "FilmActorRow", "view"), f = _.getByFilmId(creds, id), v = model => controllers.admin.film.routes.FilmActorRowController.view(model.actorId, model.filmId), s = model => views.html.admin.film.filmActorRowSearchResult(model, s"Film Actor [${model.actorId}, ${model.filmId}] matched filmId [$q]")),
      act[services.film.FilmCategoryRowService, models.film.FilmCategoryRow](injector = injector, creds = creds, perm = ("film", "FilmCategoryRow", "view"), f = _.getByCategoryId(creds, id), v = model => controllers.admin.film.routes.FilmCategoryRowController.view(model.filmId, model.categoryId), s = model => views.html.admin.film.filmCategoryRowSearchResult(model, s"Film Category [${model.filmId}, ${model.categoryId}] matched categoryId [$q]")),
      act[services.film.FilmCategoryRowService, models.film.FilmCategoryRow](injector = injector, creds = creds, perm = ("film", "FilmCategoryRow", "view"), f = _.getByFilmId(creds, id), v = model => controllers.admin.film.routes.FilmCategoryRowController.view(model.filmId, model.categoryId), s = model => views.html.admin.film.filmCategoryRowSearchResult(model, s"Film Category [${model.filmId}, ${model.categoryId}] matched filmId [$q]")),
      act[services.film.FilmRowService, models.film.FilmRow](injector = injector, creds = creds, perm = ("film", "FilmRow", "view"), f = _.getByFilmId(creds, id), v = model => controllers.admin.film.routes.FilmRowController.view(model.filmId), s = model => views.html.admin.film.filmRowSearchResult(model, s"Film [${model.filmId}] matched filmId [$q]")),
      act[services.film.FilmRowService, models.film.FilmRow](injector = injector, creds = creds, perm = ("film", "FilmRow", "view"), f = _.getByLength(creds, id), v = model => controllers.admin.film.routes.FilmRowController.view(model.filmId), s = model => views.html.admin.film.filmRowSearchResult(model, s"Film [${model.filmId}] matched length [$q]")),
      act[services.film.FilmRowService, models.film.FilmRow](injector = injector, creds = creds, perm = ("film", "FilmRow", "view"), f = _.getByReleaseYear(creds, id.toLong), v = model => controllers.admin.film.routes.FilmRowController.view(model.filmId), s = model => views.html.admin.film.filmRowSearchResult(model, s"Film [${model.filmId}] matched releaseYear [$q]")),
      act[services.store.InventoryRowService, models.store.InventoryRow](injector = injector, creds = creds, perm = ("store", "InventoryRow", "view"), f = _.getByInventoryId(creds, id.toLong), v = model => controllers.admin.store.routes.InventoryRowController.view(model.inventoryId), s = model => views.html.admin.store.inventoryRowSearchResult(model, s"Inventory [${model.inventoryId}] matched inventoryId [$q]")),
      act[services.customer.LanguageRowService, models.customer.LanguageRow](injector = injector, creds = creds, perm = ("customer", "LanguageRow", "view"), f = _.getByLanguageId(creds, id), v = model => controllers.admin.customer.routes.LanguageRowController.view(model.languageId), s = model => views.html.admin.customer.languageRowSearchResult(model, s"Language [${model.languageId}] matched languageId [$q]")),
      act[services.payment.PaymentRowService, models.payment.PaymentRow](injector = injector, creds = creds, perm = ("payment", "PaymentRow", "view"), f = _.getByPaymentId(creds, id.toLong), v = model => controllers.admin.payment.routes.PaymentRowController.view(model.paymentId), s = model => views.html.admin.payment.paymentRowSearchResult(model, s"Payment [${model.paymentId}] matched paymentId [$q]")),
      act[services.customer.RentalRowService, models.customer.RentalRow](injector = injector, creds = creds, perm = ("customer", "RentalRow", "view"), f = _.getByRentalId(creds, id.toLong), v = model => controllers.admin.customer.routes.RentalRowController.view(model.rentalId), s = model => views.html.admin.customer.rentalRowSearchResult(model, s"Rental [${model.rentalId}] matched rentalId [$q]")),
      act[services.store.StaffRowService, models.store.StaffRow](injector = injector, creds = creds, perm = ("store", "StaffRow", "view"), f = _.getByStaffId(creds, id), v = model => controllers.admin.store.routes.StaffRowController.view(model.staffId), s = model => views.html.admin.store.staffRowSearchResult(model, s"Staff [${model.staffId}] matched staffId [$q]")),
      act[services.store.StoreRowService, models.store.StoreRow](injector = injector, creds = creds, perm = ("store", "StoreRow", "view"), f = _.getByStoreId(creds, id), v = model => controllers.admin.store.routes.StoreRowController.view(model.storeId), s = model => views.html.admin.store.storeRowSearchResult(model, s"Store [${model.storeId}] matched storeId [$q]"))
    ) ++
      /* End int searches */
      Nil
  }

  override def uuidSearches(app: Application, injector: Injector, creds: Credentials)(q: String, id: UUID)(implicit ec: ExecutionContext, td: TraceData): Seq[Future[Seq[(Call, Html)]]] = {
    /* Start uuid searches */
    /* End uuid searches */
    Nil
  }

  override def stringSearches(app: Application, injector: Injector, creds: Credentials)(q: String)(implicit ec: ExecutionContext, td: TraceData): Seq[Future[Seq[(Call, Html)]]] = {
    /* Start string searches */
    /* Projectile export section [boilerplay] */
    Seq(
      act[services.film.ActorRowService, models.film.ActorRow](injector = injector, creds = creds, perm = ("film", "ActorRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.film.routes.ActorRowController.view(model.actorId), s = model => views.html.admin.film.actorRowSearchResult(model, s"Actor [${model.actorId}] matched firstName [$q]")),
      act[services.address.AddressRowService, models.address.AddressRow](injector = injector, creds = creds, perm = ("address", "AddressRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.address.routes.AddressRowController.view(model.addressId), s = model => views.html.admin.address.addressRowSearchResult(model, s"Address [${model.addressId}] matched address [$q]")),
      act[services.film.CategoryRowService, models.film.CategoryRow](injector = injector, creds = creds, perm = ("film", "CategoryRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.film.routes.CategoryRowController.view(model.categoryId), s = model => views.html.admin.film.categoryRowSearchResult(model, s"Category [${model.categoryId}] matched name [$q]")),
      act[services.address.CityRowService, models.address.CityRow](injector = injector, creds = creds, perm = ("address", "CityRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.address.routes.CityRowController.view(model.cityId), s = model => views.html.admin.address.cityRowSearchResult(model, s"City [${model.cityId}] matched city [$q]")),
      act[services.address.CountryRowService, models.address.CountryRow](injector = injector, creds = creds, perm = ("address", "CountryRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.address.routes.CountryRowController.view(model.countryId), s = model => views.html.admin.address.countryRowSearchResult(model, s"Country [${model.countryId}] matched country [$q]")),
      act[services.customer.CustomerRowService, models.customer.CustomerRow](injector = injector, creds = creds, perm = ("customer", "CustomerRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.customer.routes.CustomerRowController.view(model.customerId), s = model => views.html.admin.customer.customerRowSearchResult(model, s"Customer [${model.customerId}] matched firstName [$q]")),
      act[services.film.FilmRowService, models.film.FilmRow](injector = injector, creds = creds, perm = ("film", "FilmRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.film.routes.FilmRowController.view(model.filmId), s = model => views.html.admin.film.filmRowSearchResult(model, s"Film [${model.filmId}] matched title [$q]")),
      act[services.customer.LanguageRowService, models.customer.LanguageRow](injector = injector, creds = creds, perm = ("customer", "LanguageRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.customer.routes.LanguageRowController.view(model.languageId), s = model => views.html.admin.customer.languageRowSearchResult(model, s"Language [${model.languageId}] matched name [$q]")),
      act[services.store.StaffRowService, models.store.StaffRow](injector = injector, creds = creds, perm = ("store", "StaffRow", "view"), f = _.searchExact(creds, q = q, limit = Some(5)), v = model => controllers.admin.store.routes.StaffRowController.view(model.staffId), s = model => views.html.admin.store.staffRowSearchResult(model, s"Staff [${model.staffId}] matched firstName [$q]"))
    ) ++
      /* End string searches */
      Nil
  }
}
