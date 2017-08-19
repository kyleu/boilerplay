package util.web
import javax.inject.Inject

import akka.stream.Materializer
import brave.Span
import play.api.libs.typedmap.TypedKey
import play.api.mvc.{Filter, Headers, RequestHeader, Result}
import play.api.routing.Router
import util.tracing.{TraceData, TracingService}
import zipkin.{Endpoint, TraceKeys}

import scala.concurrent.Future
import scala.util.{Failure, Success}

object TracingFilter {
  val traceKey = TypedKey[TraceData]("trace")

  val paramAwareRequestNamer: RequestHeader => String = { reqHeader =>
    import org.apache.commons.lang3.StringUtils
    val pathPattern = StringUtils.replace(
      reqHeader.attrs.get(Router.Attrs.HandlerDef).map(_.path).getOrElse(reqHeader.path),
      "<[^/]+>", ""
    )
    s"${reqHeader.method} - $pathPattern"
  }
}

class TracingFilter @Inject() (tracer: TracingService)(implicit val mat: Materializer) extends Filter {
  import tracer.executionContext
  private val reqHeaderToSpanName: RequestHeader => String = TracingFilter.paramAwareRequestNamer

  def apply(nextFilter: (RequestHeader) => Future[Result])(req: RequestHeader): Future[Result] = if (req.path.startsWith("/assets")) {
    nextFilter(req)
  } else {
    val serverSpan = tracer.serverReceived(
      spanName = reqHeaderToSpanName(req),
      span = tracer.newSpan(req.headers)((headers, key) => headers.get(key))
    )
    serverSpan.tag(TraceKeys.HTTP_PATH, req.path)
    serverSpan.tag(TraceKeys.HTTP_METHOD, req.method)
    serverSpan.tag(TraceKeys.HTTP_HOST, req.host)

    val result = nextFilter(req.addAttr(TracingFilter.traceKey, TraceData(serverSpan)))
    result.onComplete {
      case Failure(t) => tracer.serverSend(serverSpan, "failed" -> s"Finished with exception: ${t.getMessage}")
      case Success(x) =>
        serverSpan.tag(TraceKeys.HTTP_STATUS_CODE, x.header.status.toString)
        x.header.headers.get("Content-Type").map(c => serverSpan.tag("http.response.contentType", c))
        x.body.contentLength.map(l => serverSpan.tag(TraceKeys.HTTP_RESPONSE_SIZE, l.toString))
        tracer.serverSend(serverSpan)
    }
    result
  }
}
