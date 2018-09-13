package models.database

object DatabaseConfig {
  def fromConfig(cfg: play.api.Configuration, configPrefix: String) = {
    val sectionName = cfg.get[String](configPrefix + ".section")
    val section = configPrefix + "." + sectionName

    def get(k: String) = cfg.get[String](section + "." + k)
    DatabaseConfig(
      host = get("host"), port = get("port").toInt, username = get("username"), password = Some(get("password")),
      database = Some(get("database")), enableSlick = get("slick") == "true", enableDoobie = get("doobie") == "true"
    )
  }
}

final case class DatabaseConfig(
    host: String = "localhost",
    port: Int = 5432,
    username: String,
    password: Option[String] = None,
    database: Option[String] = None,
    enableSlick: Boolean,
    enableDoobie: Boolean
) {
  val url: String = s"jdbc:postgresql://$host:$port/${database.getOrElse(util.Config.projectId)}?stringtype=unspecified"
}
