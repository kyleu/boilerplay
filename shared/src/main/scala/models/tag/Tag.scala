package models.tag

import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}

object Tag {
  implicit val jsonEncoder: Encoder[Tag] = deriveEncoder
  implicit val jsonDecoder: Decoder[Tag] = deriveDecoder

  def seqFromString(s: String) = s.split(",").map(_.split("=").toList match {
    case h :: t :: Nil => Tag(h, t)
    case x => throw new IllegalStateException("Parse error: " + x.toString)
  }).toSeq

  def seqFromJavaMap(m: java.util.HashMap[_, _]) = {
    import scala.collection.JavaConverters._
    m.entrySet().asScala.toSeq.map(e => Tag(e.getKey.toString, e.getValue.toString))
  }
}

case class Tag(k: String, v: String)
