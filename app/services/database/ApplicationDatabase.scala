package services.database

/// The database used for all application models.
object ApplicationDatabase extends AsyncDatabase("application", "database.application")
