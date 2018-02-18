package services

import services.foo.{BarMutationService, BarQueryService}

@javax.inject.Singleton
class ServiceRegistry @javax.inject.Inject() (
    /* Start model service files */
    val auditServices: services.audit.AuditServiceRegistry,
    val noteServices: services.note.NoteServiceRegistry,
    val userServices: services.user.SystemUserServiceRegistry
/* End model service files */
)
