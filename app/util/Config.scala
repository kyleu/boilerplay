package util

object Config {
  final val projectId = Version.projectId
  final val projectName = Version.projectName
  final val version = Version.version
  final val metricsId = projectId.replaceAllLiterally("-", "_")
  final val projectUrl = "https://github.com/KyleU/boilerplay"
  final val adminEmail = "admin@boilerplay.com"
  final val pageSize = 100
}
