import com.lightbend.paradox.sbt.ParadoxPlugin
import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport._
import sbt.{Project, file}

object Documentation {
  lazy val doc = Project(id = "doc", base = file("./doc")).enablePlugins(ParadoxPlugin).settings(Shared.commonSettings: _*).settings(
    paradoxTheme := Some(builtinParadoxTheme("generic"))
  )
}
