package services.sandbox

import scala.concurrent.Future

object Scratchpad {
  def run() = {
    val ret = "Ok!"
    Future.successful(ret)
  }
}
