package models

import com.google.inject.Injector
import com.kyleu.projectile.controllers.admin.status.AppStatus
import com.kyleu.projectile.graphql.GraphQLSchema
import com.kyleu.projectile.models.audit.AuditCallbackProvider
import com.kyleu.projectile.models.config.NotificationService
import com.kyleu.projectile.models.{AdminModule, Application}
import com.kyleu.projectile.models.user.Role
import com.kyleu.projectile.services.audit.{AuditHelper, AuditService}
import com.kyleu.projectile.services.database.MigrateTask
import com.kyleu.projectile.util.tracing.TraceData
import models.graphql.Schema
import models.template.UserMenu
import services.audit.AuditCallbacks
import util.Version

class ProjectileModule extends AdminModule(projectName = Version.projectName, allowSignup = true, initialRole = Role.Admin, menuProvider = UserMenu) {
  override protected def onStartup(app: Application, injector: Injector) = {
    MigrateTask.migrate(app.db.source)(TraceData.noop)
    NotificationService.setCallback(f = _ => Nil)
    AuditHelper.init(appName = Version.projectName, service = injector.getInstance(classOf[AuditService]))
    AuditCallbackProvider.init(new AuditCallbacks(injector))
  }

  override protected def appStatus(app: Application, injector: Injector) = {
    AppStatus(version = Version.version, status = "OK!")
  }

  override protected[this] def schema: GraphQLSchema = Schema
}
