package util

import io.circe.Json

import scala.util.control.NonFatal

class JsonIncludeParser(loadJson: String => Json) {
  private[this] val includeKey = "#include:"

  def parseWithIncludes(key: String): Json = {
    val filename = if (key.endsWith(".json")) { key } else { key + ".json" }
    val (path, fn) = filename.lastIndexOf('/') match {
      case -1 => "" -> filename
      case x => filename.substring(0, x + 1) -> filename.substring(x + 1)
    }
    val jsonContent = loadJson(key)
    modify(path, fn, jsonContent)
  }

  def modify(path: String, fn: String, json: Json): Json = try {
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
          case _ => Seq(k -> modify(path, fn, v))
        }
      }: _*)
      case None => json
    }
  } catch {
    case x: IllegalStateException => throw x
    case NonFatal(x) => throw new IllegalStateException(s"Unable to parse json from [$path$fn].", x)
  }
}
