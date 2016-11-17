package services.graphql

import java.nio.file.{Files, Paths}

import services.file.FileService

object GraphQLFileService {
  private[this] val dir = FileService.getDir("graphql")

  def list() = {
    if (!dir.isDirectory) {
      throw new IllegalStateException(s"Missing query directory [${dir.getPath}].")
    }
    val roots = dir.listFiles.filter(_.getName.endsWith(".graphql")).map(x => x.getName.stripSuffix(".graphql")).toSeq
    val dirs = dir.listFiles.filter(_.isDirectory).toSeq
    val children = dirs.map { d =>
      d.getName -> d.listFiles.filter(_.getName.endsWith(".graphql")).map(x => x.getName.stripSuffix(".graphql")).toSeq
    }
    roots -> children
  }

  def load(subdir: Option[String], name: String) = {
    import scala.collection.JavaConverters._

    val filename = name + ".graphql"
    val file = subdir match {
      case Some(d) => Paths.get(dir.getPath, d + "/" + filename)
      case None => Paths.get(dir.getPath, filename)
    }
    if (Files.exists(file)) {
      Files.readAllLines(file).asScala.mkString("\n")
    } else {
      throw new IllegalArgumentException(s"Query does not exist in dir [$dir] with name [$name].")
    }
  }

  def save(subdir: Option[String], name: String, query: String) = {
    val filename = name + ".graphql"
    val d = subdir match {
      case Some(dirname) => Paths.get(dir.getPath, dirname)
      case None => Paths.get(dir.getPath)
    }
    if (!Files.exists(d)) {
      Files.createDirectory(d)
    }
    val file = d.resolve(filename)
    Files.write(file, query.getBytes)
  }

  def remove(subdir: Option[String], name: String) = {
    val filename = name + ".graphql"
    val file = subdir match {
      case Some(d) => Paths.get(dir.getPath, d + "/" + filename)
      case None => Paths.get(dir.getPath, filename)
    }
    if (Files.exists(file)) {
      Files.delete(file)
    } else {
      throw new IllegalArgumentException(s"No query available qith name [$name].")
    }
  }
}
