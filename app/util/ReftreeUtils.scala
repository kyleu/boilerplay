package util

import java.nio.file.Files
import java.time.{LocalDate, LocalDateTime, LocalTime}
import java.util.UUID

import io.circe.Json
import reftree.core._
import reftree.diagram.Diagram
import reftree.render.{Renderer, RenderingOptions}

object ReftreeUtils extends Logging {
  implicit def stringRefTree: ToRefTree[String] = reftree.contrib.SimplifiedInstances.string
  implicit def uuidRefTree: ToRefTree[UUID] = ToRefTree[UUID](uuid => RefTree.Ref(uuid, Nil).rename(uuid.toString))
  implicit def ldtRefTree: ToRefTree[LocalDateTime] = ToRefTree[LocalDateTime](ldt => RefTree.Ref(ldt, Nil).rename(util.DateUtils.niceDateTime(ldt)))
  implicit def ldRefTree: ToRefTree[LocalDate] = ToRefTree[LocalDate](ld => RefTree.Ref(ld, Nil).rename(util.DateUtils.niceDate(ld)))
  implicit def ltRefTree: ToRefTree[LocalTime] = ToRefTree[LocalTime](lt => RefTree.Ref(lt, Nil).rename(util.DateUtils.niceTime(lt)))
  implicit def bdRefTree: ToRefTree[BigDecimal] = ToRefTree[BigDecimal](bd => RefTree.Ref(bd, Nil).rename(bd.toString))
  implicit def jsonRefTree: ToRefTree[Json] = ToRefTree[Json](json => RefTree.Ref(json, Nil).rename(json.spaces2))

  implicit def optionRefTree[A: ToRefTree]: ToRefTree[Option[A]] = reftree.contrib.SimplifiedInstances.option[A]
  implicit def listRefTree[A: ToRefTree]: ToRefTree[List[A]] = reftree.contrib.SimplifiedInstances.list[A]
  implicit def seqRefTree[A: ToRefTree]: ToRefTree[Seq[A]] = reftree.contrib.SimplifiedInstances.seq[A]
  implicit def mapRefTree[K: ToRefTree, V: ToRefTree]: ToRefTree[Map[K, V]] = reftree.contrib.SimplifiedInstances.map[K, V]

  private[this] val dir = Files.createTempDirectory("reftree")

  private[this] val opts = RenderingOptions()
  private[this] val pngRenderer = Renderer(renderingOptions = opts, directory = dir, format = "png")
  private[this] val svgRenderer = Renderer(renderingOptions = opts, directory = dir, format = "svg")

  def renderToPng[A](v: A)(implicit rt: ToRefTree[A]) = {
    import pngRenderer._
    val filename = scala.util.Random.alphanumeric.take(8).mkString
    Diagram(v).render(filename)
    fin(v.getClass.getSimpleName, filename + ".png")
  }

  def renderToSvg[A](v: A)(implicit rt: ToRefTree[A]) = {
    import svgRenderer._
    val filename = scala.util.Random.alphanumeric.take(8).mkString
    Diagram(v).render(filename)
    fin(v.getClass.getSimpleName, filename + ".svg")
  }

  private[this] def fin(t: String, filename: String) = {
    val out = dir.resolve(filename)
    val bytes = Files.readAllBytes(out)
    log.info(s"Generated reftree for [$t] in file [$out].")
    Files.delete(out)
    bytes
  }
}
