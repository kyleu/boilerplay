import com.typesafe.sbt.packager.Keys._
import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.{Docker, dockerExposedPorts, dockerExposedVolumes}
import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport.{Universal, useNativeZip}
import sbt.Keys._
import sbt._

object Packaging {
  private[this] def isConf(x: (File, String)) = x._1.getAbsolutePath.contains("conf/")

  val settings = useNativeZip ++ Seq(
    topLevelDirectory in Universal := None,
    packageSummary := description.value,
    packageDescription := description.value,

    mappings in Universal := (mappings in Universal).value.filterNot(isConf),

    // Docker
    dockerExposedPorts := Seq(Shared.projectPort),
    dockerLabels ++= Map("project" -> Shared.projectId),
    dockerUpdateLatest := true,
    defaultLinuxInstallLocation in Docker := s"/opt/${Shared.projectId}",
    packageName in Docker := packageName.value,
    dockerExposedVolumes := Seq(s"/opt/${Shared.projectId}"),
    version in Docker := version.value,
    dockerUsername := Some(Shared.projectId),

    javaOptions in Universal ++= Seq("-J-Xmx2g", "-J-Xms256m", s"-Dproject=${Shared.projectId}")
  )
}
