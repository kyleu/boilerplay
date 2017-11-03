package util

import java.nio.ByteBuffer

import boopickle.Default._
import models.user.UserPreferences
import models.{RequestMessage, ResponseMessage}

object BinarySerializers {
  implicit val ldtPickler = transformPickler((t: Long) => util.DateUtils.fromMillis(t))(x => util.DateUtils.toMillis(x))
  implicit val requestPickler: Pickler[RequestMessage] = generatePickler[RequestMessage]
  implicit val responsePickler: Pickler[ResponseMessage] = generatePickler[ResponseMessage]

  private[this] def toByteArray(bb: ByteBuffer) = {
    val arr = new Array[Byte](bb.remaining)
    bb.get(arr)
    arr
  }

  def readPreferences(b: Array[Byte]) = Unpickle[UserPreferences].fromBytes(ByteBuffer.wrap(b))
  def writePreferences(p: UserPreferences) = toByteArray(Pickle.intoBytes(p))

  def readRequestMessage(b: ByteBuffer) = Unpickle[RequestMessage].fromBytes(b)
  def writeRequestMessage(rm: RequestMessage) = toByteArray(Pickle.intoBytes(rm))

  def readResponseMessage(b: ByteBuffer) = Unpickle[ResponseMessage].fromBytes(b)
  def writeResponseMessage(rm: ResponseMessage) = toByteArray(Pickle.intoBytes(rm))
}
