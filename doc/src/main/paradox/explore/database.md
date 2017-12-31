# Database

Database connections are regrettably JDBC-based, but an API returning `Future`s and operating in a different ExecutionContext is provided. 
To get started, check out the connection configuration in `application.conf`, the provided `JdbcDatabase` class, and the files in `models.queries`. 

Slick table definitions are also provided, see `ApplicationDatabase.slick.run()`.

Postgres 9.5+ is the preferred database, but earlier versions are [supported](../troubleshooting.md).
