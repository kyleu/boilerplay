package util

import io.circe.Json

import scala.util.control.NonFatal

class JsonIncludeParser(loadJson: String => Json) {
  private[this] val includeKey = "#include:"

  def parseWithIncludes(key: String): Json = {
    val filename = if (key.endsWith(".json")) { key } else { key + ".json" }
    val path = filename.lastIndexOf('/') match {
      case -1 => ""
      case x => filename.substring(0, x + 1)
    }
    val jsonContent = loadJson(key)
    modify(path, jsonContent)
  }

  def modify(path: String, json: Json): Json = try {
    json.asObject match {
      case Some(o) => Json.obj(o.toList.flatMap {
        case (k, v) if k.startsWith(includeKey) =>
          val incKey = path + k.drop(includeKey.length)
          v.asString match {
            case Some("*") => parseWithIncludes(incKey).asObject.getOrElse(throw new IllegalStateException(s"File [$incKey] must be a json object.")).toList
            case _ => throw new IllegalStateException("Invalid IncKey value: " + v.spaces2)
          }
        case (k, v) => v.asString match {
          case Some(s) if s.startsWith(includeKey) =>
            val incVal = path + s.drop(includeKey.length)
            Seq(k -> parseWithIncludes(incVal))
          case _ => Seq(k -> modify(path, v))
        }
      }: _*)
      case None => json
    }
  } catch {
    case NonFatal(x) => throw new IllegalStateException(s"Unable to parse json from [$path].", x)
  }
}
