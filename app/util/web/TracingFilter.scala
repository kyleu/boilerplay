package util.web
import javax.inject.Inject

import akka.stream.Materializer
import play.api.libs.typedmap.TypedKey
import play.api.mvc.{Filter, RequestHeader, Result}
import play.api.routing.Router
import util.tracing.{TraceData, TracingService}
import zipkin.TraceKeys

import scala.concurrent.Future
import scala.util.{Failure, Success}

object TracingFilter {
  val traceKey = TypedKey[TraceData]("trace")

  val paramAwareRequestNamer: RequestHeader => String = { reqHeader =>
    import org.apache.commons.lang3.StringUtils
    val pathPattern = StringUtils.replace(reqHeader.attrs.get(Router.Attrs.HandlerDef).map(_.path).getOrElse(reqHeader.path), "<[^/]+>", "")
    s"${reqHeader.method} - $pathPattern"
  }
}

class TracingFilter @Inject() (tracingService: TracingService)(implicit val mat: Materializer) extends Filter {
  import tracingService.executionContext
  private val reqHeaderToSpanName: RequestHeader => String = TracingFilter.paramAwareRequestNamer

  def apply(nextFilter: (RequestHeader) => Future[Result])(req: RequestHeader): Future[Result] = if (req.path.startsWith("/assets")) {
    nextFilter(req)
  } else {
    val serverSpan = tracingService.serverReceived(
      spanName = reqHeaderToSpanName(req),
      span = tracingService.newSpan(req.headers)((headers, key) => headers.get(key))
    )
    serverSpan.tag(TraceKeys.HTTP_PATH, req.path)
    serverSpan.tag(TraceKeys.HTTP_METHOD, req.method)
    serverSpan.tag(TraceKeys.HTTP_HOST, req.host)
    if (req.queryString.nonEmpty) {
      serverSpan.tag("http.query.string", req.rawQueryString)
    }
    req.queryString.foreach {
      case (k, v) => serverSpan.tag(s"http.query.$k", v.mkString(", "))
    }

    val result = nextFilter(req.addAttr(TracingFilter.traceKey, TraceData(serverSpan)))
    result.onComplete {
      case Failure(t) => tracingService.serverSend(serverSpan, "failed" -> s"Finished with exception: ${t.getMessage}")
      case Success(x) =>
        serverSpan.tag(TraceKeys.HTTP_STATUS_CODE, x.header.status.toString)
        x.header.headers.get("Content-Type").map(c => serverSpan.tag("http.response.contentType", c))
        x.body.contentLength.map(l => serverSpan.tag(TraceKeys.HTTP_RESPONSE_SIZE, l.toString))
        tracingService.serverSend(serverSpan)
    }
    result
  }
}
