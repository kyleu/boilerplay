package services.connection

import models._
import models.database.queries.auth.UserQueries
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import services.database.Database
import utils.cache.UserCache
import utils.metrics.InstrumentedActor

trait ConnectionServicePreferenceHelper extends InstrumentedActor { this: ConnectionService =>
  protected[this] def handleSetPreference(sp: SetPreference): Unit = {
    val newPrefs = sp.name match {
      case "background-color" => userPreferences.copy(color = sp.value)
      case _ => log.errorThenThrow(s"Unhandled preference [${sp.name}] for user [$user.id] with value [${sp.value}].")
    }
    if (newPrefs != userPreferences) {
      Database.execute(UserQueries.SetPreferences(user.id, newPrefs)).map { x =>
        userPreferences = newPrefs
        UserCache.removeUser(user.id)
      }
    }
  }
}
