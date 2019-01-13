scalacOptions ++= Seq( "-unchecked", "-deprecation" )

resolvers += Resolver.typesafeRepo("releases")

// Projectile
addSbtPlugin("com.kyleu" % "projectile-sbt" % "1.0.0")

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.6.20")

// SBT-Web plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-web" % "1.4.3")
addSbtPlugin("com.typesafe.sbt" % "sbt-less" % "1.1.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-gzip" % "1.0.2")

// Scala.js
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "0.5.0")
addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.26")
addSbtPlugin("com.vmunier" % "sbt-web-scalajs" % "1.0.8-0.6" exclude("org.scala-js", "sbt-scalajs"))

// Source Control
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

// App Packaging
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.12")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.9")

// Dependency Resolution
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0")

// Code Quality
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0") // scalastyle
addSbtPlugin("com.sksamuel.scapegoat" %% "sbt-scapegoat" % "1.0.7")
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.1") // scalariformFormat
addSbtPlugin("com.github.sbt" % "sbt-cpd" % "2.0.0")

// Utilities
addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.9.0") // dependencyGraph
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.3.4") // dependencyUpdates
addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.7") // stats
addSbtPlugin("com.typesafe.sbt" % "sbt-license-report" % "1.2.0")

// Visualization
addSbtPlugin("com.dwijnand" % "sbt-project-graph" % "0.4.0") // projectsGraphDot
addSbtPlugin("com.github.xuwei-k" % "sbt-class-diagram" % "0.2.1") // classDiagram

// Documentation
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.4.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.2" exclude("com.lightbend.paradox", "sbt-paradox"))
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.2")
