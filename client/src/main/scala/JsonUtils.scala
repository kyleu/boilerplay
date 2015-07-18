import java.util.UUID

import scala.scalajs.js

object JsonUtils {
  def getStringOption(o: js.Dynamic) = if (o.isInstanceOf[Unit]) { None } else { Some(o.toString) }

  def getInt(o: js.Dynamic) = o.toString.toInt
  def getIntOption(o: js.Dynamic) = if (o.isInstanceOf[Unit]) { None } else { Some(getInt(o)) }

  def getLong(o: js.Dynamic) = o.toString.toLong

  def getUuidSeq(o: js.Dynamic) = {
    val a = o.asInstanceOf[js.Array[String]] // Unavoidable
    var idx = 0
    val ret = collection.mutable.ArrayBuffer.empty[UUID]
    while (idx < a.length) {
      ret += UUID.fromString(a(idx))
      idx += 1
    }
    ret.toSeq
  }
}
