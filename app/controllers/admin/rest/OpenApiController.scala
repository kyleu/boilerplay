package controllers.admin.rest

import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application
import com.kyleu.projectile.models.auth.AuthActions
import com.kyleu.projectile.util.tracing.TraceData

import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.util.{JsonIncludeParser, JsonSerializers}

import scala.concurrent.Future
import scala.io.Source
import scala.util.control.NonFatal

@javax.inject.Singleton
class OpenApiController @javax.inject.Inject() (override val app: Application, authActions: AuthActions) extends AuthController("rest") {
  private[this] def loadJson(key: String) = {
    val resource = Option(getClass.getClassLoader.getResourceAsStream(key)).getOrElse(throw new IllegalStateException(s"Cannot load [$key] from classpath."))
    val content = Source.fromInputStream(resource).getLines.filterNot(_.trim.startsWith("//")).mkString("\n")
    JsonSerializers.parseJson(content) match {
      case Left(x) => throw new IllegalStateException(s"Cannot parse json from [$key].", x)
      case Right(json) => json
    }
  }

  val rootFilename = "openapi/openapi.json"

  lazy val jsonContent = try {
    val json = new JsonIncludeParser(loadJson).parseWithIncludes(rootFilename)
    Future.successful(Ok(json))
  } catch {
    case NonFatal(x) =>
      log.error(s"Unable to parse json includes from [$rootFilename].", x)(TraceData.noop)
      Future.successful(InternalServerError(x.getMessage))
  }

  def json() = withSession("openapi.json", admin = true) { implicit request => implicit td =>
    jsonContent
  }

  def ui() = withSession("index", admin = true) { implicit request => implicit td =>
    Future.successful(Ok(views.html.admin.rest.swagger(request.identity, authActions)))
  }
}
