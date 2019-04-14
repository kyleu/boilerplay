package controllers.admin.system

import java.util.UUID

import com.google.inject.Injector
import com.kyleu.projectile.controllers.AuthController
import com.kyleu.projectile.models.Application

import scala.concurrent.ExecutionContext.Implicits.global
import com.kyleu.projectile.models.auth.UserCredentials
import play.twirl.api.Html
import com.kyleu.projectile.util.tracing.TraceData
import play.api.mvc.Call

import scala.concurrent.Future

@javax.inject.Singleton
class SearchController @javax.inject.Inject() (override val app: Application, injector: Injector) extends AuthController("search") {
  def search(q: String) = withSession("admin.search", admin = true) { implicit request => implicit td =>
    val creds = UserCredentials.fromRequest(request)
    val results = try {
      searchInt(creds, q, q.toInt)
    } catch {
      case _: NumberFormatException => try {
        searchUuid(creds, q, UUID.fromString(q))
      } catch {
        case _: IllegalArgumentException => searchString(creds, q)
      }
    }
    results.map {
      case r if r.size == 1 => Redirect(r.head._1)
      case r => Ok(com.kyleu.projectile.views.html.admin.explore.searchResults(q, r.map(_._2), app.cfg(Some(request.identity), admin = true, "Search", q)))
    }
  }

  private[this] def searchInt(creds: UserCredentials, q: String, id: Int)(implicit timing: TraceData) = {
    val intSearches = Seq.empty[Future[Seq[(Call, Html)]]] ++
      /* Start int searches */
      /* End int searches */
      Nil

    Future.sequence(intSearches).map(_.flatten)
  }

  private[this] def searchUuid(creds: UserCredentials, q: String, id: UUID)(implicit timing: TraceData) = {
    val uuidSearches = Seq.empty[Future[Seq[(Call, Html)]]] ++
      /* Start uuid searches */
      /* Projectile export section [boilerplay] */
      Seq(
        injector.getInstance(classOf[services.audit.AuditRecordRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.audit.routes.AuditRecordRowController.view(model.id) -> views.html.admin.audit.auditRecordRowSearchResult(model, s"Audit Record [${model.id}] matched [$q]")).toSeq),
        injector.getInstance(classOf[services.audit.AuditRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.audit.routes.AuditRowController.view(model.id) -> views.html.admin.audit.auditRowSearchResult(model, s"Audit [${model.id}] matched [$q]")).toSeq),
        injector.getInstance(classOf[services.note.NoteRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.note.routes.NoteRowController.view(model.id) -> views.html.admin.note.noteRowSearchResult(model, s"Note [${model.id}] matched [$q]")).toSeq),
        injector.getInstance(classOf[services.task.ScheduledTaskRunRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.task.routes.ScheduledTaskRunRowController.view(model.id) -> views.html.admin.task.scheduledTaskRunRowSearchResult(model, s"Scheduled Task Run [${model.id}] matched [$q]")).toSeq),
        injector.getInstance(classOf[services.auth.SystemUserRowService]).getByPrimaryKey(creds, id).map(_.map(model => controllers.admin.auth.routes.SystemUserRowController.view(model.id) -> views.html.admin.auth.systemUserRowSearchResult(model, s"System User [${model.id}] matched [$q]")).toSeq)
      ) ++
        /* End uuid searches */
        Nil

    Future.sequence(uuidSearches).map(_.flatten)
  }

  private[this] def searchString(creds: UserCredentials, q: String)(implicit timing: TraceData) = {
    val stringSearches = Seq.empty[Future[Seq[(Call, Html)]]] ++
      /* Start string searches */
      /* Projectile export section [boilerplay] */
      Seq(
        injector.getInstance(classOf[services.audit.AuditRecordRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.audit.routes.AuditRecordRowController.view(model.id) -> views.html.admin.audit.auditRecordRowSearchResult(model, s"Audit Record [${model.id}] matched [$q]"))),
        injector.getInstance(classOf[services.audit.AuditRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.audit.routes.AuditRowController.view(model.id) -> views.html.admin.audit.auditRowSearchResult(model, s"Audit [${model.id}] matched [$q]"))),
        injector.getInstance(classOf[services.ddl.FlywaySchemaHistoryRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.ddl.routes.FlywaySchemaHistoryRowController.view(model.installedRank) -> views.html.admin.ddl.flywaySchemaHistoryRowSearchResult(model, s"Flyway Schema History [${model.installedRank}] matched [$q]"))),
        injector.getInstance(classOf[services.note.NoteRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.note.routes.NoteRowController.view(model.id) -> views.html.admin.note.noteRowSearchResult(model, s"Note [${model.id}] matched [$q]"))),
        injector.getInstance(classOf[services.auth.Oauth2InfoRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.auth.routes.Oauth2InfoRowController.view(model.provider, model.key) -> views.html.admin.auth.oauth2InfoRowSearchResult(model, s"OAuth2 Info [${model.provider}, ${model.key}] matched [$q]"))),
        injector.getInstance(classOf[services.auth.PasswordInfoRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.auth.routes.PasswordInfoRowController.view(model.provider, model.key) -> views.html.admin.auth.passwordInfoRowSearchResult(model, s"Password Info [${model.provider}, ${model.key}] matched [$q]"))),
        injector.getInstance(classOf[services.task.ScheduledTaskRunRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.task.routes.ScheduledTaskRunRowController.view(model.id) -> views.html.admin.task.scheduledTaskRunRowSearchResult(model, s"Scheduled Task Run [${model.id}] matched [$q]"))),
        injector.getInstance(classOf[services.settings.SettingService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.settings.routes.SettingController.view(model.k) -> views.html.admin.settings.settingSearchResult(model, s"Setting [${model.k}] matched [$q]"))),
        injector.getInstance(classOf[services.sync.SyncProgressRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.sync.routes.SyncProgressRowController.view(model.key) -> views.html.admin.sync.syncProgressRowSearchResult(model, s"Sync Progress [${model.key}] matched [$q]"))),
        injector.getInstance(classOf[services.auth.SystemUserRowService]).searchExact(creds, q = q, limit = Some(5)).map(_.map(model => controllers.admin.auth.routes.SystemUserRowController.view(model.id) -> views.html.admin.auth.systemUserRowSearchResult(model, s"System User [${model.id}] matched [$q]")))
      ) ++
        /* End string searches */
        Nil

    Future.sequence(stringSearches).map(_.flatten)
  }
}
