package services.database

/// The database used for authentication, auditing, and system settings.
object SystemDatabase extends JdbcDatabase("system", "database.system")
