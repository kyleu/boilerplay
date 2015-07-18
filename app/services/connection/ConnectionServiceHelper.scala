package services.connection

import models._
import utils.Logging
import utils.metrics.InstrumentedActor

trait ConnectionServiceHelper
    extends InstrumentedActor
    with ConnectionServicePreferenceHelper
    with ConnectionServiceTraceHelper
    with Logging { this: ConnectionService =>

  protected[this] def handleResponseMessage(rm: ResponseMessage) {
    out ! rm
  }
}
