package util.web

import java.nio.ByteBuffer

import models.ResponseMessage
import play.api.mvc.WebSocket.MessageFlowTransformer
import util.{BinarySerializers, JsonSerializers, Logging}

class MessageFrameFormatter() extends Logging {
  val stringTransformer = MessageFlowTransformer.stringMessageFlowTransformer.map { s =>
    JsonSerializers.readRequestMessage(s)
  }.contramap { m: ResponseMessage => JsonSerializers.writeResponseMessage(m) }

  val binaryTransformer = MessageFlowTransformer.byteArrayMessageFlowTransformer.map { ba =>
    BinarySerializers.readRequestMessage(ByteBuffer.wrap(ba))
  }.contramap { m: ResponseMessage => BinarySerializers.writeResponseMessage(m) }

  def transformer(binary: Boolean) = if (binary) { binaryTransformer } else { stringTransformer }
}
