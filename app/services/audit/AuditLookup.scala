package services.audit

import java.util.UUID

import models.result.data.DataFieldModel
import services.ServiceRegistry
import util.tracing.TraceData

import scala.concurrent.Future

@javax.inject.Singleton
class AuditLookup @javax.inject.Inject() (registry: ServiceRegistry) {
  private[this] def uuidArg(s: String) = UUID.fromString(s)
  private[this] def longArg(s: String) = s.toLong
  private[this] def floatArg(s: String) = s.toFloat
  private[this] def byteArg(s: String) = s.toInt.toByte
  private[this] def ldArg(s: String) = util.DateUtils.fromDateString(s)
  private[this] def ldtArg(s: String) = util.DateUtils.fromIsoString(s)
  private[this] def intArg(s: String) = s.toInt

  def getById(key: String, id: Seq[String])(implicit traceData: TraceData) = {
    def arg(i: Int) = {
      if (id.length <= i) { throw new IllegalStateException(s"Needed at least [${i + 1}] id arguments, only have [${id.length}].") }
      id(i)
    }

    getModel(key, arg)
  }

  private[this] def getModel(key: String, arg: (Int) => String)(implicit traceData: TraceData): Future[Option[DataFieldModel]] = key.toLowerCase match {
    /* Start registry lookups */
    /* End registry lookups */

    case "user" => registry.userService.getByPrimaryKey(uuidArg(arg(0)))
    case _ => throw new IllegalStateException(s"Invalid model key [$key].")
  }
}
