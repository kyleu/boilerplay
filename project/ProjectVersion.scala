import sbt._
import sbt.Keys._

object ProjectVersion {
  def writeConfig(projectId: String, projectName: String, projectPort: Int) = Def.task {
    val content =s"""
      |package util
      |
      |object Version {
      |  val projectId = "$projectId"
      |  val projectName = "$projectName"
      |  val projectPort = $projectPort
      |
      |  val version = "${version.value}"
      |}
      |""".stripMargin.trim

    val file = (sourceManaged in Compile).value / "version" / "util" / "Version.scala"
    val current = if(file.exists) { IO.read(file) } else { "" }
    if(current != content) { IO.write(file, content) }
    Seq(file)
  }
}
