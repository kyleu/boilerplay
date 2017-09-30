import com.lightbend.paradox.sbt.ParadoxPlugin
import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport._
import com.typesafe.sbt.GitPlugin.autoImport.git
import com.typesafe.sbt.sbtghpages.GhpagesPlugin
import com.typesafe.sbt.site.SiteScaladocPlugin
import com.typesafe.sbt.site.paradox.ParadoxSitePlugin
import com.typesafe.sbt.site.paradox.ParadoxSitePlugin.autoImport.Paradox
import sbt.{Project, file}
import sbt.Keys._
import sbt._

object Documentation {
  lazy val doc = Project(id = "doc", base = file("./doc")).enablePlugins(
    ParadoxPlugin, ParadoxSitePlugin, SiteScaladocPlugin, GhpagesPlugin
  ).settings(Shared.commonSettings: _*).settings(
    paradoxTheme := Some(builtinParadoxTheme("generic")),
    sourceDirectory in Paradox := sourceDirectory.value / "main" / "paradox",
    git.remoteRepo := "git@github.com:KyleU/boilerplay.git"
  )
}
