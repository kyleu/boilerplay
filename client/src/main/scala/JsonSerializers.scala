import models._
import upickle._

object JsonSerializers {
  private implicit val stringOptionWriter = upickle.Writer[Option[String]] {
    case Some(s) => Js.Str(s)
    case None => Js.Null
  }
  private implicit val intOptionWriter = upickle.Writer[Option[Int]] {
    case Some(i) => Js.Num(i)
    case None => Js.Null
  }
  private implicit val boolOptionWriter = upickle.Writer[Option[Boolean]] {
    case Some(b) => if (b) { Js.True } else { Js.False }
    case None => Js.Null
  }

  private implicit val responseMessageWriter: Writer[ResponseMessage] = upickle.Writer[ResponseMessage] {
    case rm =>
      val jsVal = rm match {
        case vr: VersionResponse => writeJs(vr)
        case p: Pong => writeJs(p)
        case ms: MessageSet => writeJs(ms)
        case _ => throw new IllegalStateException(s"Invalid Message [${rm.getClass.getName}].")
      }
      val jsArray = jsVal match { case arr: Js.Arr => arr; case _ => throw new IllegalArgumentException(jsVal.toString) }
      jsArray.value.toList match {
        case one :: two :: Nil =>
          val oneStr = Js.Str(one match {
            case s: Js.Str => s.value.replace("models.", "")
            case _ => throw new IllegalStateException()
          })
          Js.Obj("c" -> oneStr, "v" -> two)
        case _ => throw new IllegalStateException()
      }
  }

  def write(rm: ResponseMessage) = responseMessageWriter.write(rm)
  def write(j: Js.Value) = json.write(j)
}
