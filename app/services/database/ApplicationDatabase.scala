package services.database

/// The database used for all application models.
object ApplicationDatabase extends JdbcDatabase("application", "database.application")
