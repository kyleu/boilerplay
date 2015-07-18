package services.scheduled

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.audit.DailyMetricService
import utils.DateUtils

object MetricsUpdate {
  def updateMetrics() = {
    val d = DateUtils.today
    DailyMetricService.recalculateMetrics(d).map(m => "metrics" -> Some(s"Updated metrics for [$d]"))
  }
}
