package services.audit

@javax.inject.Singleton
class AuditServiceRegistry @javax.inject.Inject() (
    val auditRecordService: services.audit.AuditRecordService
)
