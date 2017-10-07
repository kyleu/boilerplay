package services

@javax.inject.Singleton
class ServiceRegistry @javax.inject.Inject() (
  /* Start model service files */
  /* End model service files */

  val settingsService: services.settings.SettingsService,
  val userService: services.user.UserService
)
