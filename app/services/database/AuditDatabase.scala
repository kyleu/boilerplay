package services.database

/// The database used for all application models.
object AuditDatabase extends JdbcDatabase("audit", "database.audit")
