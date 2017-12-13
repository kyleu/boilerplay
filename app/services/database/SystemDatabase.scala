package services.database

/// The database used for system models, such as users, notes, and audits.
object SystemDatabase extends JdbcDatabase("system", "database.system")
