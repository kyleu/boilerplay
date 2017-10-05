package services.audit

case class SlackConfig(
    enabled: Boolean,
    url: String,
    channel: String = "#general",
    username: String = util.Config.projectName,
    iconUrl: String = ""
) {
  def forMessage(message: String) = Map(
    "channel" -> channel,
    "username" -> username,
    "icon_url" -> iconUrl,
    "text" -> message
  )
}
