package models.audit

case class AuditField(key: String, originalValue: Option[String], newValue: Option[String])
