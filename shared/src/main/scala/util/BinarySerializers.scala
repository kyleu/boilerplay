package util

import java.nio.ByteBuffer
import java.time.{LocalDate, LocalDateTime, LocalTime}

import boopickle.Default._
import io.circe.Json
import models.{RequestMessage, ResponseMessage}

object BinarySerializers {
  implicit val jsonPickler: Pickler[Json] = transformPickler((s: String) => util.JsonSerializers.parseJson(s).right.get)(x => x.spaces2)
  implicit val ldPickler: Pickler[LocalDate] = transformPickler((s: String) => util.DateUtils.fromDateString(s))(_.toString)
  implicit val ltPickler: Pickler[LocalTime] = transformPickler((s: String) => util.DateUtils.fromTimeString(s))(_.toString)
  implicit val ldtPickler: Pickler[LocalDateTime] = transformPickler((t: Long) => util.DateUtils.fromMillis(t))(x => util.DateUtils.toMillis(x))
  implicit val requestPickler: Pickler[RequestMessage] = generatePickler[RequestMessage]
  implicit val responsePickler: Pickler[ResponseMessage] = generatePickler[ResponseMessage]

  private[this] def toByteArray(bb: ByteBuffer) = {
    val arr = new Array[Byte](bb.remaining)
    bb.get(arr)
    arr
  }

  def readRequestMessage(b: ByteBuffer) = Unpickle[RequestMessage].fromBytes(b)
  def writeRequestMessage(rm: RequestMessage) = toByteArray(Pickle.intoBytes(rm))

  def readResponseMessage(b: ByteBuffer) = Unpickle[ResponseMessage].fromBytes(b)
  def writeResponseMessage(rm: ResponseMessage) = toByteArray(Pickle.intoBytes(rm))
}
