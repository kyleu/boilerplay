package models.template

import com.kyleu.projectile.models.menu.NavMenu

object ComponentMenu {
  val menu: Seq[NavMenu] = Nil ++
    /* Start component menu items */
    /* Projectile export section [boilerplay] */
    Seq(NavMenu(key = "address", title = "Address", url = None, icon = Some(models.template.Icons.pkg_address), children = Seq(
      NavMenu(key = "address", title = "Addresses", url = Some(controllers.admin.address.routes.AddressRowController.list().url), icon = Some(models.template.Icons.addressRow)),
      NavMenu(key = "city", title = "Cities", url = Some(controllers.admin.address.routes.CityRowController.list().url), icon = Some(models.template.Icons.cityRow)),
      NavMenu(key = "country", title = "Countries", url = Some(controllers.admin.address.routes.CountryRowController.list().url), icon = Some(models.template.Icons.countryRow))
    ))) ++
    Seq(NavMenu(key = "customer", title = "Customer", url = None, icon = Some(models.template.Icons.pkg_customer), children = Seq(
      NavMenu(key = "customer", title = "Customers", url = Some(controllers.admin.customer.routes.CustomerRowController.list().url), icon = Some(models.template.Icons.customerRow)),
      NavMenu(key = "language", title = "Languages", url = Some(controllers.admin.customer.routes.LanguageRowController.list().url), icon = Some(models.template.Icons.languageRow)),
      NavMenu(key = "rental", title = "Rentals", url = Some(controllers.admin.customer.routes.RentalRowController.list().url), icon = Some(models.template.Icons.rentalRow))
    ))) ++
    Seq(NavMenu(key = "film", title = "Film", url = None, icon = Some(models.template.Icons.pkg_film), children = Seq(
      NavMenu(key = "actor", title = "Actors", url = Some(controllers.admin.film.routes.ActorRowController.list().url), icon = Some(models.template.Icons.actorRow)),
      NavMenu(key = "category", title = "Categories", url = Some(controllers.admin.film.routes.CategoryRowController.list().url), icon = Some(models.template.Icons.categoryRow)),
      NavMenu(key = "film", title = "Films", url = Some(controllers.admin.film.routes.FilmRowController.list().url), icon = Some(models.template.Icons.filmRow)),
      NavMenu(key = "film_actor", title = "Film Actors", url = Some(controllers.admin.film.routes.FilmActorRowController.list().url), icon = Some(models.template.Icons.filmActorRow)),
      NavMenu(key = "film_category", title = "Film Categories", url = Some(controllers.admin.film.routes.FilmCategoryRowController.list().url), icon = Some(models.template.Icons.filmCategoryRow))
    ))) ++
    Seq(NavMenu(key = "payment", title = "Payment", url = None, icon = Some(models.template.Icons.pkg_payment), children = Seq(
      NavMenu(key = "payment", title = "Payments", url = Some(controllers.admin.payment.routes.PaymentRowController.list().url), icon = Some(models.template.Icons.paymentRow))
    ))) ++
    Seq(NavMenu(key = "store", title = "Store", url = None, icon = Some(models.template.Icons.pkg_store), children = Seq(
      NavMenu(key = "inventory", title = "Inventories", url = Some(controllers.admin.store.routes.InventoryRowController.list().url), icon = Some(models.template.Icons.inventoryRow)),
      NavMenu(key = "staff", title = "Staff", url = Some(controllers.admin.store.routes.StaffRowController.list().url), icon = Some(models.template.Icons.staffRow)),
      NavMenu(key = "store", title = "Stores", url = Some(controllers.admin.store.routes.StoreRowController.list().url), icon = Some(models.template.Icons.storeRow))
    ))) ++
    /* End component menu items */
    Nil
}
