package models.sandbox

import models.Application
import util.tracing.TraceData

import scala.concurrent.Future

object TracingLogic {
  def go(app: Application, argument: Option[String])(implicit trace: TraceData) = {
    app.tracing.trace("td1") { td1 =>
      app.tracing.trace("td2") { td2 =>
        app.tracing.trace("td3") { td3 =>
          app.tracing.trace("td4") { td4 =>
            app.tracing.trace("td5") { _ =>
              Future.successful("All good!")
            }(td4)
          }(td3)
        }(td2)
      }(td1)
    }
  }
}
