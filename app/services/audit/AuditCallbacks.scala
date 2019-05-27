package services.audit

import com.google.inject.Injector
import com.kyleu.projectile.models.audit.AuditCallbackProvider
import com.kyleu.projectile.services.audit.AuditArgs._
import com.kyleu.projectile.util.Logging
import com.kyleu.projectile.util.tracing.TraceData
import play.api.mvc.Call

class AuditCallbacks(override val injector: Injector) extends AuditCallbackProvider with Logging {
  override def getViewRoute(key: String, pk: IndexedSeq[String]) = routeFor(key, getArg(pk, _))

  private[this] def routeFor(key: String, arg: Int => String): Call = key.toLowerCase match {
    /* Start audit calls */
    /* Projectile export section [boilerplay] */
    case "actorrow" => controllers.admin.film.routes.ActorRowController.view(integerArg(arg(0)))
    case "addressrow" => controllers.admin.address.routes.AddressRowController.view(integerArg(arg(0)))
    case "categoryrow" => controllers.admin.film.routes.CategoryRowController.view(integerArg(arg(0)))
    case "cityrow" => controllers.admin.address.routes.CityRowController.view(integerArg(arg(0)))
    case "countryrow" => controllers.admin.address.routes.CountryRowController.view(integerArg(arg(0)))
    case "customerrow" => controllers.admin.customer.routes.CustomerRowController.view(integerArg(arg(0)))
    case "filmactorrow" => controllers.admin.film.routes.FilmActorRowController.view(integerArg(arg(0)), integerArg(arg(1)))
    case "filmcategoryrow" => controllers.admin.film.routes.FilmCategoryRowController.view(integerArg(arg(0)), integerArg(arg(1)))
    case "filmrow" => controllers.admin.film.routes.FilmRowController.view(integerArg(arg(0)))
    case "inventoryrow" => controllers.admin.store.routes.InventoryRowController.view(longArg(arg(0)))
    case "languagerow" => controllers.admin.customer.routes.LanguageRowController.view(integerArg(arg(0)))
    case "paymentrow" => controllers.admin.payment.routes.PaymentRowController.view(longArg(arg(0)))
    case "rentalrow" => controllers.admin.customer.routes.RentalRowController.view(longArg(arg(0)))
    case "staffrow" => controllers.admin.store.routes.StaffRowController.view(integerArg(arg(0)))
    case "storerow" => controllers.admin.store.routes.StoreRowController.view(integerArg(arg(0)))
    /* End audit calls */

    case _ =>
      log.warn(s"Invalid model key [$key].")(TraceData.noop)
      controllers.routes.HomeController.home()
  }
}
