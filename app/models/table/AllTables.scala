/* Generated File */
package models.table

import com.kyleu.projectile.services.database.slick.SlickQueryService.imports._

object AllTables {
  val schema = Seq(
    models.table.address.AddressRowTable.query.schema,
    models.table.address.CityRowTable.query.schema,
    models.table.address.CountryRowTable.query.schema,
    models.table.customer.CustomerRowTable.query.schema,
    models.table.customer.LanguageRowTable.query.schema,
    models.table.customer.RentalRowTable.query.schema,
    models.table.film.ActorRowTable.query.schema,
    models.table.film.CategoryRowTable.query.schema,
    models.table.film.FilmActorRowTable.query.schema,
    models.table.film.FilmCategoryRowTable.query.schema,
    models.table.film.FilmRowTable.query.schema,
    models.table.payment.PaymentRowTable.query.schema,
    models.table.store.InventoryRowTable.query.schema,
    models.table.store.StaffRowTable.query.schema,
    models.table.store.StoreRowTable.query.schema
  )
}
