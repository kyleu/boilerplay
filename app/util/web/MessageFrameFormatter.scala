package util.web

import java.nio.ByteBuffer

import io.circe.parser.decode
import models.{RequestMessage, ResponseMessage}
import play.api.mvc.WebSocket.MessageFlowTransformer
import util.{BinarySerializers, Logging}
import util.JsonSerializers._

class MessageFrameFormatter() extends Logging {
  val stringTransformer = MessageFlowTransformer.stringMessageFlowTransformer.map(s => decode[RequestMessage](s) match {
    case Right(x) => x
    case Left(err) => throw err
  }).contramap { rm: ResponseMessage => rm.asJson.spaces2 }

  val binaryTransformer = MessageFlowTransformer.byteArrayMessageFlowTransformer.map { ba =>
    BinarySerializers.readRequestMessage(ByteBuffer.wrap(ba))
  }.contramap { rm: ResponseMessage => BinarySerializers.writeResponseMessage(rm) }

  def transformer(binary: Boolean) = if (binary) { binaryTransformer } else { stringTransformer }
}
