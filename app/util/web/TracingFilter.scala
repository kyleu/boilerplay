package util.web

import akka.stream.Materializer
import com.twitter.zipkin.gen.Span
import play.api.mvc.{Filter, Headers, RequestHeader, Result}
import play.api.routing.Router

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal
import scala.util.Failure
/*
object TracingFilter {
  object HttpHeaders extends Enumeration {
    val traceIdHeaderKey = "X-B3-TraceId"
    val spanIdHeaderKey = "X-B3-SpanId"
    val parentIdHeaderKey = "X-B3-ParentSpanId"

    val all = Seq(traceIdHeaderKey, spanIdHeaderKey, parentIdHeaderKey)
  }

  def apply(zipkinServiceFactory: => ZipkinServiceLike, reqHeaderToSpanName: RequestHeader => String = ParamAwareRequestNamer)(
    implicit mat: Materializer, eCtx: ExecutionContext
  ) = new TracingFilter(zipkinServiceFactory, reqHeaderToSpanName)

  val ParamAwareRequestNamer: RequestHeader => String = { reqHeader =>
    import org.apache.commons.lang3.StringUtils
    val rawPathPattern = reqHeader.attrs.get(Router.Attrs.HandlerDef).map(_.path).getOrElse(reqHeader.path)
    val pathPattern = StringUtils.replace(rawPathPattern, "<[^/]+>", "")
    s"${reqHeader.method} - $pathPattern"
  }
}


class TracingFilter(zipkinServiceFactory: => ZipkinServiceLike, reqHeaderToSpanName: RequestHeader => String)(
  implicit val mat: Materializer, eCtx: ExecutionContext
) extends Filter {
  private implicit lazy val zipkinService = zipkinServiceFactory

  def apply(nextFilter: (RequestHeader) => Future[Result])(req: RequestHeader): Future[Result] = {
    val parentSpan = zipkinService.generateSpan(reqHeaderToSpanName(req), req2span(req))
    val fMaybeServerSpan = zipkinService.serverReceived(parentSpan).recover { case NonFatal(e) => None }
    fMaybeServerSpan flatMap {
      case None => nextFilter(req)
      case Some(serverSpan) => {
        val fResult = nextFilter(addHeadersToReq(req, zipkinService.serverSpanToSpan(serverSpan)))
        fResult.onComplete {
          case Failure(e) => zipkinService.serverSent(serverSpan, "failed" -> s"Finished with exception: ${e.getMessage}")
          case _ => zipkinService.serverSent(serverSpan)
        }
        fResult
      }
    }
  }

  private def addHeadersToReq(req: RequestHeader, span: Span): RequestHeader = {
    val originalHeaderData = req.headers.toMap
    val withSpanData = originalHeaderData ++ zipkinService.spanToIdsMap(span).map { case (key, value) => key -> Seq(value) }
    val newHeaders = new Headers(withSpanData.mapValues(_.headOption.getOrElse("")).toSeq)
    req.withHeaders(newHeaders)
  }
}
*/
