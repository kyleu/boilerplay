package services

@javax.inject.Singleton
class ServiceRegistry @javax.inject.Inject() (
  /* Start model service files */
  /* End model service files */

  val noteService: services.note.NoteService,
  val settingsService: services.settings.SettingsService,
  val userService: services.user.UserService
)
