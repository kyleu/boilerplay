package models.database

object DatabaseConfig {
  def fromConfig(cfg: play.api.Configuration, configPrefix: String) = {
    val sectionName = cfg.get[String](configPrefix + ".section")
    val section = configPrefix + "." + sectionName

    def get(k: String) = cfg.get[String](section + "." + k)
    DatabaseConfig(get("host"), get("port").toInt, get("username"), Some(get("password")), Some(get("database")))
  }
}

final case class DatabaseConfig(
    host: String = "localhost",
    port: Int = 5432,
    username: String,
    password: Option[String] = None,
    database: Option[String] = None
) {
  val url: String = s"jdbc:postgresql://$host:$port/${database.getOrElse(util.Config.projectId)}?stringtype=unspecified"
}
