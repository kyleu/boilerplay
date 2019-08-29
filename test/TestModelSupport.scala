/* Generated File */
import com.kyleu.projectile.models.result.data.DataFieldModel
import com.kyleu.projectile.util.Credentials
import com.kyleu.projectile.util.tracing.TraceData

object TestModelSupport {
  private[this] val creds = Credentials.noop
  private[this] implicit val td: TraceData = TraceData.noop

  def insert(m: DataFieldModel) = m match {
    case model: models.film.ActorRow => TestServices.actorRowService.insert(creds, model)
    case model: models.address.AddressRow => TestServices.addressRowService.insert(creds, model)
    case model: models.film.CategoryRow => TestServices.categoryRowService.insert(creds, model)
    case model: models.address.CityRow => TestServices.cityRowService.insert(creds, model)
    case model: models.address.CountryRow => TestServices.countryRowService.insert(creds, model)
    case model: models.customer.CustomerRow => TestServices.customerRowService.insert(creds, model)
    case model: models.film.FilmRow => TestServices.filmRowService.insert(creds, model)
    case model: models.film.FilmActorRow => TestServices.filmActorRowService.insert(creds, model)
    case model: models.film.FilmCategoryRow => TestServices.filmCategoryRowService.insert(creds, model)
    case model: models.store.InventoryRow => TestServices.inventoryRowService.insert(creds, model)
    case model: models.customer.LanguageRow => TestServices.languageRowService.insert(creds, model)
    case model: models.payment.PaymentRow => TestServices.paymentRowService.insert(creds, model)
    case model: models.customer.RentalRow => TestServices.rentalRowService.insert(creds, model)
    case model: models.store.StaffRow => TestServices.staffRowService.insert(creds, model)
    case model: models.store.StoreRow => TestServices.storeRowService.insert(creds, model)
    case model => throw new IllegalStateException(s"Unable to insert unhandled model [$model]")
  }

  def remove(m: DataFieldModel) = m match {
    case model: models.film.ActorRow => TestServices.actorRowService.remove(creds, model.actorId)
    case model: models.address.AddressRow => TestServices.addressRowService.remove(creds, model.addressId)
    case model: models.film.CategoryRow => TestServices.categoryRowService.remove(creds, model.categoryId)
    case model: models.address.CityRow => TestServices.cityRowService.remove(creds, model.cityId)
    case model: models.address.CountryRow => TestServices.countryRowService.remove(creds, model.countryId)
    case model: models.customer.CustomerRow => TestServices.customerRowService.remove(creds, model.customerId)
    case model: models.film.FilmRow => TestServices.filmRowService.remove(creds, model.filmId)
    case model: models.film.FilmActorRow => TestServices.filmActorRowService.remove(creds, model.actorId, model.filmId)
    case model: models.film.FilmCategoryRow => TestServices.filmCategoryRowService.remove(creds, model.filmId, model.categoryId)
    case model: models.store.InventoryRow => TestServices.inventoryRowService.remove(creds, model.inventoryId)
    case model: models.customer.LanguageRow => TestServices.languageRowService.remove(creds, model.languageId)
    case model: models.payment.PaymentRow => TestServices.paymentRowService.remove(creds, model.paymentId)
    case model: models.customer.RentalRow => TestServices.rentalRowService.remove(creds, model.rentalId)
    case model: models.store.StaffRow => TestServices.staffRowService.remove(creds, model.staffId)
    case model: models.store.StoreRow => TestServices.storeRowService.remove(creds, model.storeId)
    case model => throw new IllegalStateException(s"Unable to remove unhandled model [$model]")
  }
}
