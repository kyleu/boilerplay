package models.search

import java.util.UUID

import com.google.inject.Injector
import com.kyleu.projectile.models.module.Application
import com.kyleu.projectile.services.Credentials
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
      injector.getInstance(classOf[services.film.ActorRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.film.routes.ActorRowController.view(model.actorId) -> views.html.admin.film.actorRowSearchResult(model, s"Actor [${model.actorId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.address.AddressRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.address.routes.AddressRowController.view(model.addressId) -> views.html.admin.address.addressRowSearchResult(model, s"Address [${model.addressId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.film.CategoryRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.film.routes.CategoryRowController.view(model.categoryId) -> views.html.admin.film.categoryRowSearchResult(model, s"Category [${model.categoryId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.address.CityRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.address.routes.CityRowController.view(model.cityId) -> views.html.admin.address.cityRowSearchResult(model, s"City [${model.cityId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.address.CountryRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.address.routes.CountryRowController.view(model.countryId) -> views.html.admin.address.countryRowSearchResult(model, s"Country [${model.countryId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.customer.CustomerRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.customer.routes.CustomerRowController.view(model.customerId) -> views.html.admin.customer.customerRowSearchResult(model, s"Customer [${model.customerId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.film.FilmRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.film.routes.FilmRowController.view(model.filmId) -> views.html.admin.film.filmRowSearchResult(model, s"Film [${model.filmId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.customer.LanguageRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.customer.routes.LanguageRowController.view(model.languageId) -> views.html.admin.customer.languageRowSearchResult(model, s"Language [${model.languageId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.store.StaffRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.store.routes.StaffRowController.view(model.staffId) -> views.html.admin.store.staffRowSearchResult(model, s"Staff [${model.staffId}] matched [$q]")).toSeq),
      injector.getInstance(classOf[services.store.StoreRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.store.routes.StoreRowController.view(model.storeId) -> views.html.admin.store.storeRowSearchResult(model, s"Store [${model.storeId}] matched [$q]")).toSeq)
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
      injector.getInstance(classOf[services.film.ActorRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.film.routes.ActorRowController.view(model.actorId) -> views.html.admin.film.actorRowSearchResult(model, s"Actor [${model.actorId}] matched [$q]"))),
      injector.getInstance(classOf[services.address.AddressRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.address.routes.AddressRowController.view(model.addressId) -> views.html.admin.address.addressRowSearchResult(model, s"Address [${model.addressId}] matched [$q]"))),
      injector.getInstance(classOf[services.film.CategoryRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.film.routes.CategoryRowController.view(model.categoryId) -> views.html.admin.film.categoryRowSearchResult(model, s"Category [${model.categoryId}] matched [$q]"))),
      injector.getInstance(classOf[services.address.CityRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.address.routes.CityRowController.view(model.cityId) -> views.html.admin.address.cityRowSearchResult(model, s"City [${model.cityId}] matched [$q]"))),
      injector.getInstance(classOf[services.address.CountryRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.address.routes.CountryRowController.view(model.countryId) -> views.html.admin.address.countryRowSearchResult(model, s"Country [${model.countryId}] matched [$q]"))),
      injector.getInstance(classOf[services.customer.CustomerRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.customer.routes.CustomerRowController.view(model.customerId) -> views.html.admin.customer.customerRowSearchResult(model, s"Customer [${model.customerId}] matched [$q]"))),
      injector.getInstance(classOf[services.film.FilmActorRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.film.routes.FilmActorRowController.view(model.actorId, model.filmId) -> views.html.admin.film.filmActorRowSearchResult(model, s"Film Actor [${model.actorId}, ${model.filmId}] matched [$q]"))),
      injector.getInstance(classOf[services.film.FilmCategoryRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.film.routes.FilmCategoryRowController.view(model.filmId, model.categoryId) -> views.html.admin.film.filmCategoryRowSearchResult(model, s"Film Category [${model.filmId}, ${model.categoryId}] matched [$q]"))),
      injector.getInstance(classOf[services.film.FilmRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.film.routes.FilmRowController.view(model.filmId) -> views.html.admin.film.filmRowSearchResult(model, s"Film [${model.filmId}] matched [$q]"))),
      injector.getInstance(classOf[services.store.InventoryRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.store.routes.InventoryRowController.view(model.inventoryId) -> views.html.admin.store.inventoryRowSearchResult(model, s"Inventory [${model.inventoryId}] matched [$q]"))),
      injector.getInstance(classOf[services.customer.LanguageRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.customer.routes.LanguageRowController.view(model.languageId) -> views.html.admin.customer.languageRowSearchResult(model, s"Language [${model.languageId}] matched [$q]"))),
      injector.getInstance(classOf[services.payment.PaymentRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.payment.routes.PaymentRowController.view(model.paymentId) -> views.html.admin.payment.paymentRowSearchResult(model, s"Payment [${model.paymentId}] matched [$q]"))),
      injector.getInstance(classOf[services.customer.RentalRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.customer.routes.RentalRowController.view(model.rentalId) -> views.html.admin.customer.rentalRowSearchResult(model, s"Rental [${model.rentalId}] matched [$q]"))),
      injector.getInstance(classOf[services.store.StaffRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.store.routes.StaffRowController.view(model.staffId) -> views.html.admin.store.staffRowSearchResult(model, s"Staff [${model.staffId}] matched [$q]"))),
      injector.getInstance(classOf[services.store.StoreRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.store.routes.StoreRowController.view(model.storeId) -> views.html.admin.store.storeRowSearchResult(model, s"Store [${model.storeId}] matched [$q]")))
    ) ++
      /* End string searches */
      Nil
  }
}
