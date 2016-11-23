package utils

import enumeratum.UPickler
import models.template.Theme
import models.{RequestMessage, ResponseMessage}
import upickle.Js
import upickle.default._

object JsonSerializers {
  // Enumerations
  implicit val themeReader = UPickler.reader(Theme)
  implicit val themeWriter = UPickler.writer(Theme)

  // Wire messages
  def readRequestMessage(json: Js.Value) = readJs[RequestMessage](json)
  def writeRequestMessage(rm: RequestMessage, debug: Boolean = false) = if (debug) {
    write(rm, indent = 2)
  } else {
    write(rm)
  }

  def readResponseMessage(json: String) = read[ResponseMessage](json)
  def writeResponseMessageJs(rm: ResponseMessage) = {
    writeJs(rm)
  }
  def writeResponseMessage(rm: ResponseMessage, debug: Boolean = false) = if (debug) {
    write(rm, indent = 2)
  } else {
    write(rm)
  }
}
