# Getting Started

## Prerequisites

* SBT
* PostgreSQL
* Node.js for building (or change Server.scala)

## Running the app

First, either create a Postgres role and database named "boilerplay", or change the application.conf to use your existing database.

```sql
CREATE ROLE boilerplay WITH LOGIN PASSWORD 'boilerplay';
CREATE DATABASE boilerplay;
GRANT ALL PRIVILEGES ON DATABASE boilerplay TO boilerplay;
```

You'll either need Node.js available as "node" on the path, or change project/Server.scala's EngineType to Rhino.

Now, finally,
```shell
$ sbt
> run
$ open http://127.0.0.1:9000
```

As the application starts, it will create database tables and seed data.

The first account to sign up is created as an Admin, all subsequent users will have a normal user role.


## Projects

* `server` Main web application.
* `sharedJvm` Core Scala logic and rules definitions, for JVM projects.
* `sharedJs` Shared classes, compiled to Scala.js JavaScript.
* `client` Barebones Scala.js app.
