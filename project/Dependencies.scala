import sbt._

object Dependencies {
  object Projectile {
    val version = "1.1.4"
    val all = Seq("auth-graphql", "doobie", "graphql", "slick").map(s => "com.kyleu" %% s"projectile-lib-$s" % version)
  }

  object PlayFramework {
    val filters = play.sbt.PlayImport.filters
    val cache = play.sbt.PlayImport.ehcache
    val guice = play.sbt.PlayImport.guice
    val json = "com.typesafe.play" %% "play-json" % "2.6.13"
    val ws = play.sbt.PlayImport.ws

    val all = Seq(filters, guice, ws, json, cache)
  }

  object Utils {
    val betterFiles = "com.github.pathikrit" %% "better-files" % "3.7.0"
    val commonsIo = "commons-io" % "commons-io" % "2.6"
    val commonsLang = "org.apache.commons" % "commons-lang3" % "3.8.1"

    val all = Seq(betterFiles, commonsIo, commonsLang)
  }

  object Testing {
    val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"

    val all = Seq(scalaTest)
  }
}
