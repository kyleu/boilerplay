import sbtcrossproject.CrossPlugin.autoImport.crossProject

val projectVersion = "3.0.0"

lazy val shared = projectileCrossProject(crossProject(JSPlatform, JVMPlatform), "shared").settings(version := projectVersion)

lazy val client = (project in file("client")).settings(version := projectVersion).dependsOn(shared.js).enablePlugins(ProjectileScalaJSProject)

lazy val server = Project(id = "boilerplay", base = file(".")).settings(
  projectileProjectTitle := "Boilerplay",
  projectileProjectPort := 9000,

  play.sbt.routes.RoutesKeys.routesImport += "models.module.ModelBindables._",
  libraryDependencies ++= Seq(projectileLib("slick"), projectileLib("doobie")),

  scalaJSProjects := Seq(client),
  pipelineStages in Assets := Seq(scalaJSPipeline)
).enablePlugins(ProjectilePlayProject).dependsOn(shared.jvm)
