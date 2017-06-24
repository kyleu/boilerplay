import sbt._

object Dependencies {
  object Cache {
    val ehCache = "net.sf.ehcache" % "ehcache-core" % "2.6.11"
  }

  object Logging {
    val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.21"
  }

  object Play {
    private[this] val version = "2.6.0"
    val lib = "com.typesafe.play" %% "play" % version
    val filters = play.sbt.PlayImport.filters
    val ws = play.sbt.PlayImport.ws
    val json = "com.typesafe.play" %% "play-json" % version
    val test = "com.typesafe.play" %% "play-test" % version % "test"
    val mailer = "com.typesafe.play" %% "play-mailer" % "6.0.0"
  }

  object Akka {
    private[this] val version = "2.5.2"
    val actor = "com.typesafe.akka" %% "akka-actor" % version
    val remote = "com.typesafe.akka" %% "akka-remote" % version
    val logging = "com.typesafe.akka" %% "akka-slf4j" % version
    val cluster = "com.typesafe.akka" %% "akka-cluster" % version
    val clusterMetrics = "com.typesafe.akka" %% "akka-cluster-metrics" % version
    val clusterTools = "com.typesafe.akka" %% "akka-cluster-tools" % version
    val testkit = "com.typesafe.akka" %% "akka-testkit" % version % "test"
  }

  object Authentication {
    private[this] val version = "5.0.0-SNAPSHOT"
    val silhouette = "com.mohiva" %% "play-silhouette" % version
    val hasher = "com.mohiva" %% "play-silhouette-password-bcrypt" % version
    val persistence = "com.mohiva" %% "play-silhouette-persistence" % version
    val crypto = "com.mohiva" %% "play-silhouette-crypto-jca" % version
  }

  object Database {
    val postgres = "com.github.mauricio" %% "postgresql-async" % "0.2.21" exclude("io.netty", "netty-all")
  }

  object GraphQL {
    val sangria = "org.sangria-graphql" %% "sangria" % "1.2.1"
    val sangriaPlayJson = "org.sangria-graphql" %% "sangria-play-json" % "1.0.1"
  }

  object Serialization {
    val circeVersion = "0.8.0"
  }

  object WebJars {
    val fontAwesome = "org.webjars" % "font-awesome" % "4.7.0"
    val jquery = "org.webjars" % "jquery" % "2.2.4"
    val materialize = "org.webjars" % "materializecss" % "0.98.2"
    val moment = "org.webjars" % "momentjs" % "2.18.1"
    val mousetrap = "org.webjars" % "mousetrap" % "1.6.0"
  }

  object Metrics {
    val version = "3.2.2"
    val metrics = "nl.grons" %% "metrics-scala" % "3.5.6"
    val jvm = "io.dropwizard.metrics" % "metrics-jvm" % version
    val ehcache = "io.dropwizard.metrics" % "metrics-ehcache" % version intransitive()
    val healthChecks = "io.dropwizard.metrics" % "metrics-healthchecks" % version intransitive()
    val json = "io.dropwizard.metrics" % "metrics-json" % version
    val jettyServlet = "org.eclipse.jetty" % "jetty-servlet" % "9.4.5.v20170502"
    val servlets = "io.dropwizard.metrics" % "metrics-servlets" % version intransitive()
    val graphite = "io.dropwizard.metrics" % "metrics-graphite" % version intransitive()
  }

  object ScalaJS {
    val jQueryVersion = "0.9.1"
    val scalaTagsVersion = "0.6.5"
  }

  object Utils {
    val scapegoatVersion = "1.3.0"
    val enumeratumVersion = "1.5.14"

    val commonsIo = "commons-io" % "commons-io" % "2.5"
    val scalaGuice = "net.codingwell" %% "scala-guice" % "4.1.0"
  }

  object Testing {
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3" % "test"
    val gatlingCore = "io.gatling" % "gatling-test-framework" % "2.1.7" % "test"
    val gatlingCharts = "io.gatling.highcharts" % "gatling-charts-highcharts" % "2.1.7" % "test"
  }
}
