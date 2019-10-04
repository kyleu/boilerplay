scalacOptions ++= Seq( "-unchecked", "-deprecation" )

resolvers += Resolver.typesafeRepo("releases")

// Projectile
addSbtPlugin("com.kyleu" % "projectile-sbt-admin" % "1.28.0")

// Code Quality
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0") // scalastyle
addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.1") // scalariformFormat
