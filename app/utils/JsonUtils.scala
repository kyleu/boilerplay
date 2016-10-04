package utils

import upickle.Js

object JsonUtils {
  def toString(v: Js.Value): String = v match {
    case s: Js.Str => s.value
    case n: Js.Num => n.value.toString
    case a: Js.Arr => "[" + a.value.map(v => toString(v)).mkString(", ") + "]"
    case Js.False => "false"
    case Js.True => "true"
    case o: Js.Obj => "{" + toStringMap(o.value.toMap).map(x => s"${x._1}: ${x._2}").mkString(", ") + "}"
    case x => throw new IllegalStateException(s"Invalid param type [${x.getClass.getName}].")
  }

  def toStringMap(params: Map[String, Js.Value]): Map[String, String] = params.map(p => p._1 -> toString(p._2))
}
