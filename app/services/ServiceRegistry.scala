package services

@javax.inject.Singleton
class ServiceRegistry @javax.inject.Inject() (
    /* Start model service files */
    val auditServices: services.audit.AuditServiceRegistry,
    val noteServices: services.note.NoteServiceRegistry,
    val userServices: services.user.UserServiceRegistry
/* End model service files */
)
