package services

@javax.inject.Singleton
class ServiceRegistry @javax.inject.Inject() (
    /* Start model service files */

    val auditServices: services.audit.AuditServiceRegistry,
    val noteServices: services.note.NoteServiceRegistry,
    val systemUserServices: services.user.SystemUserServiceRegistry,

    /* End model service files */

    val settingsService: services.settings.SettingsService
)
