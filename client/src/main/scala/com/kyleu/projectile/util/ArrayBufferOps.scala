package com.kyleu.projectile.util

import java.nio.ByteBuffer

import scala.scalajs.js.JSConverters._
import scala.scalajs.js.typedarray.TypedArrayBufferOps._
import scala.scalajs.js.typedarray.Uint8Array

object ArrayBufferOps {
  def convertArray(arr: Array[Byte]): Uint8Array = {
    scalajs.js.Dynamic.newInstance(scalajs.js.Dynamic.global.Uint8Array)(arr.toJSArray).asInstanceOf[Uint8Array]
  }

  def convertBuffer(data: ByteBuffer) = if (data.hasTypedArray()) {
    data.typedArray().subarray(data.position(), data.limit).buffer
  } else {
    val tempBuffer = ByteBuffer.allocateDirect(data.remaining)
    val origPosition = data.position()
    tempBuffer.put(data)
    data.position(origPosition)
    tempBuffer.typedArray().buffer
  }
}
