import sbt._

object Dependencies {
  object Play {
    private[this] val version = "2.6.20"
    val lib = "com.typesafe.play" %% "play" % version
    val filters = play.sbt.PlayImport.filters
    val ws = play.sbt.PlayImport.ws
    val guice = play.sbt.PlayImport.guice
    val cache = play.sbt.PlayImport.ehcache
    val json = "com.typesafe.play" %% "play-json" % "2.6.10"
    val test = "com.typesafe.play" %% "play-test" % version % "test"
  }

  object Akka {
    private[this] val version = "2.5.17"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
    val remote = "com.typesafe.akka" %% "akka-remote" % version
    val logging = "com.typesafe.akka" %% "akka-slf4j" % version
    val cluster = "com.typesafe.akka" %% "akka-cluster" % version
    val clusterMetrics = "com.typesafe.akka" %% "akka-cluster-metrics" % version
    val clusterTools = "com.typesafe.akka" %% "akka-cluster-tools" % version
    val protobuf = "com.typesafe.akka" %% "akka-protobuf" % version
    val stream = "com.typesafe.akka" %% "akka-stream" % version
    val visualMailbox = "de.aktey.akka.visualmailbox" %% "collector" % "1.1.0"
    val testkit = "com.typesafe.akka" %% "akka-testkit" % version % "test"
  }

  object Authentication {
    private[this] val version = "5.0.6"
    val silhouette = "com.mohiva" %% "play-silhouette" % version
    val hasher = "com.mohiva" %% "play-silhouette-password-bcrypt" % version
    val persistence = "com.mohiva" %% "play-silhouette-persistence" % version
    val crypto = "com.mohiva" %% "play-silhouette-crypto-jca" % version
  }

  object Database {
    val postgres = "org.postgresql" % "postgresql" % "42.2.5"
    val hikariCp = "com.zaxxer" % "HikariCP" % "3.2.0"

    object Slick {
      val version = "3.2.3"
      val pgVersion = "0.16.3"

      val core = "com.typesafe.slick" %% "slick" % version
      val hikariCp = "com.typesafe.slick" %% "slick-hikaricp" % version
      val pg = "com.github.tminglei" %% "slick-pg" % pgVersion
      val pgCirce = "com.github.tminglei" %% "slick-pg_circe-json" % pgVersion
      val slickless = "io.underscore" %% "slickless" % "0.3.3"
    }

    object Doobie {
      val version = "0.6.0"

      val core = "org.tpolecat" %% "doobie-core" % version
      val hikariCp = "org.tpolecat" %% "doobie-hikari" % version
      val postgres = "org.tpolecat" %% "doobie-postgres" % version
      val testing = "org.tpolecat" %% "doobie-scalatest" % version % "test"
    }

    val flyway = "org.flywaydb" % "flyway-core" % "5.2.1"

    val all = Seq(
      postgres, hikariCp, flyway, Slick.core, Slick.hikariCp, Slick.pg, Slick.pgCirce, Slick.slickless,
      Doobie.core, Doobie.hikariCp, Doobie.postgres, Doobie.testing
    )
  }

  object GraphQL {
    val sangria = "org.sangria-graphql" %% "sangria" % "1.4.2"
    val playJson = "org.sangria-graphql" %% "sangria-play-json" % "1.0.5"
    val circe = "org.sangria-graphql" %% "sangria-circe" % "1.2.1"
  }

  object Serialization {
    val circeVersion = "0.10.0"
    val circeProjects = Seq("circe-core", "circe-generic", "circe-generic-extras", "circe-parser", "circe-java8")
  }

  object WebJars {
    val fontAwesome = "org.webjars" % "font-awesome" % "4.7.0" intransitive()
    val jquery = "org.webjars" % "jquery" % "2.2.4" intransitive()
    val materialize = "org.webjars" % "materializecss" % "1.0.0" intransitive()
    val swaggerUi = "org.webjars" % "swagger-ui" % "3.19.4" intransitive()
  }

  object Metrics {
    val version = "1.0.7"
    val micrometer = "io.micrometer" % "micrometer-registry-prometheus" % version
  }

  object Tracing {
    val version = "0.32.0"
    val jaeger = "io.jaegertracing" % "jaeger-thrift" % version
    val jaegerMetrics = "io.jaegertracing" % "jaeger-micrometer" % version
  }

  object ScalaJS {
    val scalaJSVersion = "0.6.25"
    val jQueryVersion = "0.9.4"
    val jvmStubs = "org.scala-js" %% "scalajs-stubs" % scalaJSVersion % "provided"
  }

  object Utils {
    val scapegoatVersion = "1.3.8"
    val enumeratumCirceVersion = "1.5.17"
    val booPickleVersion = "1.2.5"
    val javaTimeVersion = "2.0.0-M13"

    val scalatagsVersion = "0.6.7"

    val betterFiles = "com.github.pathikrit" %% "better-files" % "3.6.0"
    val commonsIo = "commons-io" % "commons-io" % "2.6"
    val commonsLang = "org.apache.commons" % "commons-lang3" % "3.8.1"
    val csv = "com.github.tototoshi" %% "scala-csv" % "1.3.5"
    val scalaGuice = "net.codingwell" %% "scala-guice" % "4.2.1"
    val scopts = "com.github.scopt" %% "scopt" % "3.7.0"
    val reftree = "org.stanch" %% "reftree" % "1.2.0"
  }

  object Testing {
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  }
}
