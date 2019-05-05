package models.template

import com.kyleu.projectile.models.config.NavMenu

object ComponentMenu {
  val menu: Seq[NavMenu] = Seq(
    /* Start component menu items */
    /* Projectile export section [boilerplay] */
    NavMenu(key = "sync", title = "Sync", url = None, icon = Some(models.template.Icons.pkg_sync), children = Seq(
      NavMenu(key = "sync_progress", title = "Sync Progresses", url = Some(controllers.admin.sync.routes.SyncProgressRowController.list().url), icon = Some(models.template.Icons.syncProgressRow))
    )),
    NavMenu(key = "task", title = "Task", url = None, icon = Some(models.template.Icons.pkg_task), children = Seq(
      NavMenu(key = "scheduled_task_run", title = "Scheduled Task Runs", url = Some(controllers.admin.task.routes.ScheduledTaskRunRowController.list().url), icon = Some(models.template.Icons.scheduledTaskRunRow))
    ))
  /* End component menu items */
  )
}
