package models.user

final case class RegistrationData(
    username: String = "",
    email: String = "",
    password: String = "",
    passwordConfirm: String = ""
)
