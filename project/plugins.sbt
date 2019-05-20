scalacOptions ++= Seq( "-unchecked", "-deprecation" )

resolvers += Resolver.typesafeRepo("releases")

// Projectile
addSbtPlugin("com.kyleu" % "projectile-sbt-admin" % "1.9.3")

// Source Control
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "1.0.0")

// Dependency Resolution
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0")

// Code Quality
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0") // scalastyle
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.1") // scalariformFormat

// Documentation
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox" % "0.4.2")
addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "1.3.2" exclude("com.lightbend.paradox", "sbt-paradox"))
addSbtPlugin("com.typesafe.sbt" % "sbt-ghpages" % "0.6.2")
