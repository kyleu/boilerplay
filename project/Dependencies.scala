import sbt._

object Dependencies {
  object Play {
    val filters = play.sbt.PlayImport.filters
    val cache = play.sbt.PlayImport.ehcache
    val guice = play.sbt.PlayImport.guice
    val json = "com.typesafe.play" %% "play-json" % "2.6.13"
    val ws = play.sbt.PlayImport.ws
  }

  object Projectile {
    val version = "0.8.0"
    val all = Seq("doobie", "slick", "auth").map(s => "com.kyleu" %% s"projectile-lib-$s" % version)
  }

  object WebJars {
    val fontAwesome = "org.webjars" % "font-awesome" % "4.7.0" intransitive()
    val jquery = "org.webjars" % "jquery" % "2.2.4" intransitive()
    val materialize = "org.webjars" % "materializecss" % "1.0.0" intransitive()
    val swaggerUi = "org.webjars" % "swagger-ui" % "3.20.3" intransitive()
  }

  object Utils {
    val scapegoatVersion = "1.3.8"

    val betterFiles = "com.github.pathikrit" %% "better-files" % "3.7.0"
    val commonsIo = "commons-io" % "commons-io" % "2.6"
    val commonsLang = "org.apache.commons" % "commons-lang3" % "3.8.1"
    val flyway = "org.flywaydb" % "flyway-core" % "5.2.4"
  }

  object Testing {
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  }
}
