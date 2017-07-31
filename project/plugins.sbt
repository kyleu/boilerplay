scalacOptions ++= Seq( "-unchecked", "-deprecation" )

resolvers += Resolver.typesafeRepo("releases")

resolvers += Resolver.url("jetbrains-bintray", url("http://dl.bintray.com/jetbrains/sbt-plugins/"))(Resolver.ivyStylePatterns)

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.2")

// SBT-Web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.0.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.2")

addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.1")

// Scala.js
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.19")

addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.5")

// Source Control
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")

// Benchmarking
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.2.25")

addSbtPlugin("io.gatling" % "gatling-sbt" % "2.2.1")

// App Packaging
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.2.0")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.5")

// Dependency Resolution
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC6")

// Code Quality
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0") // scalastyle

addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.4")

addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.5") // stats

addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.8.2") // dependencyGraph

addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.1") // dependencyUpdates

addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0") // scalariformFormat

addSbtPlugin("com.github.xuwei-k" % "sbt-class-diagram" % "0.2.1")

// Broken on Windows
// addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.4.2")

// IDE Integration
addSbtPlugin("org.jetbrains" % "sbt-ide-settings" % "0.1.2")

addSbtPlugin("com.orrsella" % "sbt-sublime" % "1.1.1")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.1.0")
