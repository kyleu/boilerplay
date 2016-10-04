addCompilerPlugin("org.psywerx.hairyfotr" %% "linter" % "0.1.15")

lazy val sharedJs = Shared.sharedJs

lazy val client = Client.client

lazy val sharedJvm = Shared.sharedJvm

lazy val server = Server.server

lazy val metrics = Utilities.metrics

lazy val translation = Utilities.translation

lazy val benchmarking = Utilities.benchmarking
