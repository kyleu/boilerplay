/* Generated File */
import com.kyleu.projectile.services.database.JdbcDatabase
import com.kyleu.projectile.services.database.slick.SlickQueryService
import com.kyleu.projectile.util.tracing.TracingService
import scala.concurrent.ExecutionContext

object TestServices {
  private[this] implicit val ec: ExecutionContext = ExecutionContext.global
  val trace = TracingService.noop
  lazy val db = new JdbcDatabase("application", "database.application") {}
  lazy val slick = new SlickQueryService("test", db.source, 30, trace)

  lazy val actorRowService = new services.film.ActorRowService(db, trace)
  lazy val addressRowService = new services.address.AddressRowService(db, trace)
  lazy val categoryRowService = new services.film.CategoryRowService(db, trace)
  lazy val cityRowService = new services.address.CityRowService(db, trace)
  lazy val countryRowService = new services.address.CountryRowService(db, trace)
  lazy val customerRowService = new services.customer.CustomerRowService(db, trace)
  lazy val filmRowService = new services.film.FilmRowService(db, trace)
  lazy val filmActorRowService = new services.film.FilmActorRowService(db, trace)
  lazy val filmCategoryRowService = new services.film.FilmCategoryRowService(db, trace)
  lazy val inventoryRowService = new services.store.InventoryRowService(db, trace)
  lazy val languageRowService = new services.customer.LanguageRowService(db, trace)
  lazy val paymentRowService = new services.payment.PaymentRowService(db, trace)
  lazy val rentalRowService = new services.customer.RentalRowService(db, trace)
  lazy val staffRowService = new services.store.StaffRowService(db, trace)
  lazy val storeRowService = new services.store.StoreRowService(db, trace)
}
