package util

import java.nio.ByteBuffer
import java.util.UUID

object UuidUtils {
  def toBytes(uuid: UUID) = {
    val bb = ByteBuffer.wrap(new Array[Byte](16))
    bb.putLong(uuid.getMostSignificantBits)
    bb.putLong(uuid.getLeastSignificantBits)
    bb.array
  }

  def fromBytes(bytes: Array[Byte]) = {
    val byteBuffer = ByteBuffer.wrap(bytes)
    val high = byteBuffer.getLong
    val low = byteBuffer.getLong
    new UUID(high, low)
  }
}
