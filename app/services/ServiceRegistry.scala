package services

@javax.inject.Singleton
class ServiceRegistry @javax.inject.Inject() (
    /* Start model service files */
    /* Projectile export section [boilerplay] */
    val auditServices: services.audit.AuditServiceRegistry,
    val ddlServices: services.ddl.DdlServiceRegistry,
    val noteServices: services.note.NoteServiceRegistry,
    val settingsServices: services.settings.SettingsServiceRegistry,
    val syncServices: services.sync.SyncServiceRegistry,
    val taskServices: services.task.TaskServiceRegistry,
    val userServices: services.user.UserServiceRegistry
/* End model service files */
)
